package tech.jpco.nazztimesheets.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Dave - Work on 9/8/2017.
 */

public interface Repository {
    interface GetLogCallback {
        void onLogLoaded(List<WorkSession> log);
    }

    void getLocalLog(@NonNull GetLogCallback callback);

    void addNewSession(WorkSession session);

    void completeSession(WorkSession session);

    WorkSession getMostRecentSession();
}
