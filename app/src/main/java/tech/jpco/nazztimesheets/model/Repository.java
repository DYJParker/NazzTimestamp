package tech.jpco.nazztimesheets.model;

import java.util.List;

/**
 * Created by Dave - Work on 9/8/2017.
 */

public interface Repository {
    interface GetLocalLogCallback{
        void onLocalLoaded(List<WorkSession> log);
    }


}
