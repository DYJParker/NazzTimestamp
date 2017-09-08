package tech.jpco.nazztimesheets.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dave - Work on 9/8/2017.
 */

public abstract class WorkSession {
    private Date mStart, mEnd;

    public WorkSession(Date start) {
        mStart = start;
    }

    public void setEnd(Date end) {
        mEnd = end;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getEnd() {
        return mEnd;
    }

    public int minutesElapsed(){
        if (mStart == null || mEnd == null) return 0;
        return (int) TimeUnit.MILLISECONDS.toMinutes(mEnd.getTime() - mStart.getTime());
    }
}
