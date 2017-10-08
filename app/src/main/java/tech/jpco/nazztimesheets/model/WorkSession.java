package tech.jpco.nazztimesheets.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dave - Work on 9/8/2017.
 */

public class WorkSession {
    private Date mStart, mEnd;
    private WorkType mType;

    public enum WorkType{
        MINION,
        PERSONAL
    }

    public WorkSession(Date start, WorkType type) {
        mStart = start;
        mType = type;
    }

    public WorkSession(Date start, Date end, WorkType type){
        mStart = start;
        mEnd = end;
        mType = type;
    }

    public void setEnd(Date end) {
        mEnd = end;
    }

    public Date getStart() {
        return mStart;
    }

    public int getStartSecs() { return (int) mStart.getTime();}

    public Date getEnd() {
        return mEnd;
    }

    public int getEndSecs() { return (int) mEnd.getTime();}

    public WorkType getType(){
        return mType;
    }

    public int minutesElapsed(){
        if (mStart == null || mEnd == null) return 0;
        return (int) TimeUnit.MILLISECONDS.toMinutes(mEnd.getTime() - mStart.getTime());
    }
}
