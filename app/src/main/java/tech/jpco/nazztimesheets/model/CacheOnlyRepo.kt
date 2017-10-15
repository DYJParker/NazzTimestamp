package tech.jpco.nazztimesheets.model

/**
 * Created by Dave - Work on 10/8/2017.
 */
class CacheOnlyRepo : Repository {
    companion object {
        private var sInstance: CacheOnlyRepo? = null

        @Synchronized
        fun getInstance(): CacheOnlyRepo {
            if (sInstance == null) {
                sInstance = CacheOnlyRepo()
            }
            return sInstance!!
        }
    }

    private var mCache: MutableList<WorkSession> = mutableListOf()

    override fun getLocalLog(callback: Repository.GetLogCallback) {
        callback.onLogLoaded(mCache)
    }

    override fun addNewSession(session: WorkSession) {
        mCache.add(session)
    }

    override fun completeSession(session: WorkSession) {
        val target = mCache.single { it.type == session.type && it.start == session.start }

        mCache[mCache.indexOf(target)] = session
    }

    override fun getMostRecentSession(callback: Repository.GetRecentCallback) {
        callback.onRecentLoaded(mCache.lastOrNull())
    }

    internal fun setCache(log: List<WorkSession>) {
        mCache = log.toMutableList()
    }
}