package tech.jpco.nazztimesheets.model

/**
 * Created by Dave - Work on 10/15/2017.
 */
class WorkLog : ArrayList<WorkSession>() {
    fun getAheadState(fractionDenominator: Int): Double {
        val minionAhead = fold(0, { minionAhead, next ->
            minionAhead + next.minutesElapsed() *
                    (if (next.type == WorkSession.WorkType.PERSONAL) -1 else 1)
        })

        return minutesToFractionalHours(minionAhead, fractionDenominator)
    }

    private fun minutesToFractionalHours(minutes: Int, denom: Int): Double {
        return (minutes / 60) +
                (((minutes % 60) * denom) / 60).toDouble() / denom
    }
}