package tech.jpco.nazztimesheets.mainscreen

import tech.jpco.nazztimesheets.model.Repository
import tech.jpco.nazztimesheets.model.WorkLog
import tech.jpco.nazztimesheets.model.WorkSession
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Created by Dave - Work on 9/8/2017.
 */

class MainPresenter internal constructor(
        private val mView: MainContract.View,
        private val mRepo: Repository
) : MainContract.Presenter {
    init {
        mRepo.getLocalLog {
            mCache = it
            val current = mCache.lastOrNull()

            setWhichTypeActive(if (current == null || current.end != null) null else current.type,
                    current?.start)
            setAheadState()
        }
    }

    private lateinit var mCache: WorkLog

    override fun signIn(type: WorkSession.WorkType) {
        if (mCache.isNotEmpty() && mCache.last().end == null) signOut()
        val date = Date()
        mRepo.addNewSession(WorkSession(date, type))
        setWhichTypeActive(type, date)
    }

    private fun setWhichTypeActive(type: WorkSession.WorkType?, date: Date?) {
        val sdf = SimpleDateFormat("h:mm a")
        val timestamp = if (type == null || date == null) "" else "Started at " + sdf.format(date)
        when (type) {
            WorkSession.WorkType.MINION -> mView.setActive(true, timestamp)
            WorkSession.WorkType.PERSONAL -> mView.setActive(false, timestamp)
            null -> mView.setInactive()
        }
    }

    override fun signOut() {
        val current = mCache.last()
        if (current.end != null) throw IllegalStateException()

        current.end = Date()
        mRepo.completeSession(current)

        mView.setInactive()
        setAheadState()

    }

    override fun rvSwiped() {
        TODO("write this later once RV is in use")
    }

    //TODO pass this as a unicode-fraction-powered string to the View
    private fun setAheadState() {
        val denominator = 4
        val minionFracHours = mCache.getAheadState(denominator)
        val absFracHours = minionFracHours.absoluteValue
        val formattedHours = absFracHours.toInt().toString() +
                decimalToUnicodeFraction(absFracHours % 1, denominator)

        when {
            minionFracHours > 0f ->
                mView.setMinionAhead(formattedHours)
            minionFracHours < 0f -> mView.setPersonalAhead(formattedHours)
            else -> mView.setHoursEven()
        }

    }

    private fun decimalToUnicodeFraction(fractional : Double, denom : Int): String{
        if (fractional == 0.0) return ""
        if (denom != 4) return fractional.toString()
        return when ((fractional * denom).roundToInt()){
            1 -> "\u00BC"
            2 -> "\u00BD"
            3 -> "\u00BE"
            else -> fractional.toString()
        }
    }
}
