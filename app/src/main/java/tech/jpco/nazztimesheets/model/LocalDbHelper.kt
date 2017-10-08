package tech.jpco.nazztimesheets.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.jetbrains.anko.db.*

/**
 * Created by Dave - Work on 10/7/2017.
 */

class LocalDbHelper(context: Context) :
        ManagedSQLiteOpenHelper(context, "workLog.db") {
    companion object {
        const private val TABLE_NAME = "logged_sessions"
        const private val COL_ID = "_id"
        const private val COL_TYPE = "type"
        const private val COL_START = "started"
        const private val COL_END = "ended"

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
                .exec { output = parseList(classParser()) }

        return output
    }

    fun addSessionToDB(session: WorkSession) {
        writableDatabase.insert(TABLE_NAME,
                COL_TYPE to session.type.name,
                COL_START to session.startSecs)
    }

    fun completeExSessionInDB(session: WorkSession){
        writableDatabase.update(TABLE_NAME, COL_END to session.endSecs)
                .whereArgs("($COL_START = {start}) and ($COL_TYPE = {type}",
                        "start" to session.startSecs,
                        "type" to session.type.name)
    }
}