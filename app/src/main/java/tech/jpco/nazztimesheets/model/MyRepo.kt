package tech.jpco.nazztimesheets.model

import android.content.Context

/**
 * Created by Dave - Work on 10/7/2017.
 */
class MyRepo : Repository {
    companion object {
        private var sInstance: MyRepo? = null

        @Synchronized
        fun getInstance(): MyRepo {
            if (sInstance == null) {
                sInstance = MyRepo()
            }
            return sInstance!!
        }
    }
    override fun getLocalLog(callback: Repository.GetLogCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewSession(session: WorkSession) {
        TODO("not implemented")
    }

    override fun completeSession(session: WorkSession) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMostRecentSession(): WorkSession {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}