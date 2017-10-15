package tech.jpco.nazztimesheets.model

import android.content.Context
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Dave - Work on 10/7/2017.
 */
class MyRepo(context: Context) : Repository {
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
        var localLog: List<WorkSession> = emptyList()
        mCache.getLocalLog { localLog = it }
        if (localLog.isNotEmpty()) {
            callback.onLogLoaded(localLog)
        } else {
            mIsQueryingDatabase = true
//            doAsync {
                val log = mDbHelper.getLog()
                mCache.setCache(log)
//                uiThread {
                    callback.onLogLoaded(log)
                    mIsQueryingDatabase = false
//                }
//            }
        }
    }

    override fun addNewSession(session: WorkSession) {
        mCache.addNewSession(session)
//        doAsync {
            mDbHelper.addSessionToDB(session)
//        }
    }

    override fun completeSession(session: WorkSession) {
        mCache.completeSession(session)
//        doAsync {
            mDbHelper.completeExSessionInDB(session)
//        }
    }

    override fun getMostRecentSession(callback: Repository.GetRecentCallback) {
        var localLog: List<WorkSession> = emptyList()
//        doAsync {
            //TODO replace this with a coroutine or a promise
            while (mIsQueryingDatabase) { }
            mCache.getLocalLog { localLog = it }
//            uiThread {
                callback.onRecentLoaded(localLog.lastOrNull())
//            }
//        }
    }
}