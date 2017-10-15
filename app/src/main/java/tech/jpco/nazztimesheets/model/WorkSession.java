package tech.jpco.nazztimesheets.model;

import android.nfc.Tag;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dave - Work on 9/8/2017.
 */

public class WorkSession {
    //TODO !! Create overall log, pass that from model to presenter

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

    public long getStartSecs() { return mStart.getTime();}
    //TODO rewrite this as an extension in SQL repo

    public Date getEnd() {
        return mEnd;
    }

    public long getEndSecs() { return mEnd.getTime();}
    //TODO rewrite this as an extension in SQL repo

    public WorkType getType(){
        return mType;
    }

    public int minutesElapsed(){
        if (mEnd == null) return 0;
        if (mStart == null || mEnd.getTime() - mStart.getTime() < 0) throw new IllegalStateException();
        return (int) TimeUnit.MILLISECONDS.toMinutes(mEnd.getTime() - mStart.getTime());
    }
}
