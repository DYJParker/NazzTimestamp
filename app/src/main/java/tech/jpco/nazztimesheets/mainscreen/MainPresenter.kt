package tech.jpco.nazztimesheets.mainscreen

import tech.jpco.nazztimesheets.model.Repository
import tech.jpco.nazztimesheets.model.WorkSession
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Dave - Work on 9/8/2017.
 */

class MainPresenter internal constructor(
        private val mView: MainContract.View,
        private val mRepo: Repository
) : MainContract.Presenter {
    init {
        setAheadState()

        mRepo.getMostRecentSession { current ->
            setWhichTypeActive(if (current == null || current.end != null) null else current.type,
                    current.start)
        }

    }

    private var mCurrent: WorkSession.WorkType? = null

    //TODO sign out any active session
    override fun signIn(type: WorkSession.WorkType) {
        if (mCurrent != null) signOut()
        val date = Date()
        mRepo.addNewSession(WorkSession(date, type))
        setWhichTypeActive(type, date)
    }

    private fun setWhichTypeActive(type: WorkSession.WorkType?, date: Date?) {
        mCurrent = type
        val sdf: SimpleDateFormat by lazy {SimpleDateFormat("h:mm a")}
        val timestamp = if (type == null || date == null) "" else "Started at " + sdf.format(date)
        when (type) {
            WorkSession.WorkType.MINION -> mView.setActive(true, timestamp)
            WorkSession.WorkType.PERSONAL -> mView.setActive(false, timestamp)
            null -> mView.setInactive()
        }
    }

    //TODO rewrite this to accommodate model of entire log
    override fun signOut() {
        mRepo.getMostRecentSession { current ->
            if (current.end != null) throw IllegalStateException()

            current.end = Date()
            mRepo.completeSession(current)

            mCurrent = null
            mView.setInactive()
            setAheadState()
        }
    }

    override fun rvSwiped() {
        TODO("write this later once RV is in use")
    }

    //TODO pass this as a unicode-fraction-powered string to the View
    private fun setAheadState() {
        mRepo.getLocalLog { log ->
            val minionAhead = log.fold(0, { minionAhead, next ->
                minionAhead + next.minutesElapsed() *
                        (if (next.type == WorkSession.WorkType.PERSONAL) -1 else 1)
            })

            val minionFracHours = minutesToFractionalHours(minionAhead)

            when {
                minionFracHours > 0.0f -> mView.setMinionAhead(minionFracHours)
                minionFracHours < 0.0f -> mView.setPersonalAhead(-minionFracHours)
                else -> mView.setHoursEven()
            }
        }
    }

    private fun minutesToFractionalHours(minutes: Int): Float {
        val fractionDenominator = 4

        return (minutes / 60) +
                (((minutes % 60) * fractionDenominator) / 60).toFloat() / fractionDenominator
    }
}
