package tech.jpco.nazztimesheets.mainscreen

import tech.jpco.nazztimesheets.model.Repository
import tech.jpco.nazztimesheets.model.WorkSession
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.jvm.javaClass

/**
 * Created by Dave - Work on 9/8/2017.
 */

class MainPresenter internal constructor(
        private val mView: MainContract.View,
        private val mRepo: Repository
) : MainContract.Presenter {
    init {
        setAheadState()

        val recent = mRepo.mostRecentSession
        setWhichTypeActive(if(recent == null || recent.end != null) null else recent.type)
    }

    override fun signIn(type: WorkSession.WorkType) {
        mRepo.addNewSession(WorkSession(Date(),type))
        setWhichTypeActive(type)
    }

    private fun setWhichTypeActive(type: WorkSession.WorkType?){
        when (type){
            WorkSession.WorkType.MINION -> mView.setActive(true)
            WorkSession.WorkType.PERSONAL -> mView.setActive(false)
            null -> mView.setInactive()
        }
    }

    override fun signOut() {
        //TODO rewrite this to accommodate model of entire log
        val current = mRepo.mostRecentSession
        if (current.end != null) throw IllegalStateException()

        current.end = Date()
        mRepo.completeSession(current)

        mView.setInactive()
        setAheadState()
    }

    override fun rvSwiped() {
        //TODO write this later once RV is in use
    }

    private fun setAheadState(){
        var log: List<WorkSession> = mutableListOf()
        mRepo.getLocalLog { log = it }
        val minionAhead = log.fold(0,{minionAhead, next -> minionAhead + next.minutesElapsed()*
                (if (next.type == WorkSession.WorkType.PERSONAL) -1 else 1)})

        val minionFracHours = minutesToFractionalHours(minionAhead)
        Logger.getLogger(MainPresenter::class.java.canonicalName).logp(
                Level.INFO,
                this::class.java.canonicalName,
                "setAheadState",
                "$minionFracHours"
        )

        when {
            minionFracHours > 0f -> mView.setMinionAhead(minionFracHours)
            minionFracHours < 0f -> mView.setPersonalAhead(-minionFracHours)
            else -> mView.setHoursEven()
        }
    }

    private fun minutesToFractionalHours(minutes: Int) : Float{
        val fractionDenominator = 4

        return (minutes / 60) +
                (((minutes%60)*fractionDenominator)/60).toFloat()/fractionDenominator
    }
}
