package tech.jpco.nazztimesheets.model

import android.content.Context

/**
 * Created by Dave - Work on 10/7/2017.
 */
class MyRepo : Repository {
    companion object {
        private var sInstance: LocalDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): LocalDbHelper {
            if (sInstance == null) {
                sInstance = LocalDbHelper(context.applicationContext)
            }
            return sInstance!!
        }
    }
    override fun getLocalLog(callback: Repository.GetLogCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewSession(session: WorkSession) {
        LocalDbHelper.
    }

    override fun completeSession(session: WorkSession) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}