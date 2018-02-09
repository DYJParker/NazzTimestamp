package tech.jpco.nazztimesheets.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.*
import org.jetbrains.anko.debug
import java.util.*

/**
 * Created by Dave - Work on 10/7/2017.
 */

class LocalDbHelper(context: Context) :
        ManagedSQLiteOpenHelper(context, "workLog.db"), AnkoLogger {
    companion object {
        private const val TABLE_NAME = "logged_sessions"
        private const val COL_ID = "_id"
        private const val COL_TYPE = "type"
        private const val COL_START = "started"
        private const val COL_END = "ended"

        private const val TAG = "DB"

        private var sInstance: LocalDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): LocalDbHelper {
            if (sInstance == null) {
                sInstance = LocalDbHelper(context.applicationContext)
            }
            return sInstance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_NAME, false,
                COL_ID to INTEGER + PRIMARY_KEY,
                //TODO change this to combined key on type and start, maybe just start?
                COL_TYPE to TEXT,
                COL_START to INTEGER,
                COL_END to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TABLE_NAME, true)
    }

    fun getLog(): List<WorkSession> {
        var output: List<WorkSession> = emptyList()

        readableDatabase.select(TABLE_NAME, COL_TYPE, COL_START, COL_END)
                .exec {
                    output = parseList(object : MapRowParser<WorkSession> {
                        override fun parseRow(columns: Map<String, Any?>): WorkSession {
                            val current = WorkSession(
                                    Date(columns[COL_START] as Long),
                                    if (columns[COL_TYPE] == WorkSession.WorkType.MINION.name) WorkSession.WorkType.MINION
                                        else WorkSession.WorkType.PERSONAL
                            )
                            if(columns[COL_END] != null) current.end = Date(columns[COL_END] as Long)

                            val sb = StringBuilder()
                            columns.toList().forEach { sb.append(String.format("%s: %s%n",it.first,it.second)) }
                            Log.d(TAG,sb.toString())

                            return current
                        }
                    })
                }

        return output
    }

    fun addSessionToDB(session: WorkSession) {
        Log.d(TAG,session.start.time.toString())
        writableDatabase.insert(TABLE_NAME,
                COL_TYPE to session.type.name,
                COL_START to session.start.time)
    }

    fun completeExSessionInDB(session: WorkSession) {
        writableDatabase.update(TABLE_NAME, COL_END to session.end.time)
                .whereArgs("($COL_START = {start}) and ($COL_TYPE = {type})",
                        "start" to session.start.time,
                        "type" to session.type.name)
                .exec()
    }
}