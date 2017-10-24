package tech.jpco.nazztimesheets.model

import android.content.Context
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Dave - Work on 10/7/2017.
 */
class MyRepo private constructor(context: Context) : Repository, AnkoLogger {
    companion object {
        private var sInstance: MyRepo? = null

        @Synchronized
        fun getInstance(context: Context): MyRepo {
            if (sInstance == null) {
                sInstance = MyRepo(context)
            }
            return sInstance!!
        }
    }


    private var mIsQueryingDatabase = false

    private val mDbHelper = LocalDbHelper.getInstance(context)
    private val mCache = CacheOnlyRepo.getInstance()

    override fun getLocalLog(callback: Repository.GetLogCallback) {
        var localLog = WorkLog()
        mCache.getLocalLog { localLog = it }
        debug("localLog is $localLog")
        if (localLog.isNotEmpty()) {
            callback.onLogLoaded(localLog)
        } else {
            mIsQueryingDatabase = true
            doAsync {
                val log = mDbHelper.getLog()
                mCache.setCache(log)
                uiThread {
                    debug("Returned localLog is $localLog")
                    callback.onLogLoaded(localLog)
                    mIsQueryingDatabase = false
                }
            }
        }
    }

    override fun addNewSession(session: WorkSession) {
        mCache.addNewSession(session)
        doAsync {
            mDbHelper.addSessionToDB(session)
        }
    }

    override fun completeSession(session: WorkSession) {
        mCache.completeSession(session)
        doAsync {
            mDbHelper.completeExSessionInDB(session)
        }
    }
}