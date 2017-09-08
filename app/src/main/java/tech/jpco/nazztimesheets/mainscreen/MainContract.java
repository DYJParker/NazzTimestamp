package tech.jpco.nazztimesheets.mainscreen;

import java.util.List;

import tech.jpco.nazztimesheets.BasePresenter;
import tech.jpco.nazztimesheets.BaseView;
import tech.jpco.nazztimesheets.model.WorkSession;

/**
 * Created by Dave - Work on 9/8/2017.
 */

interface MainContract {

    interface View extends BaseView<Presenter>{
        void setMinionActive();

        void setPersonalActive();

        void setInactive();

        void setRvList(List<WorkSession> log);

        void notifyRvUpdated();

        void setMinionAhead(float hours);

        void setPersonalAhead(float hours);

        void setHoursEven();
    }

    interface Presenter extends BasePresenter{
        void minionSignIn();

        void personalSignIn();

        void signOut();

        void rvSwiped();

    }
}
