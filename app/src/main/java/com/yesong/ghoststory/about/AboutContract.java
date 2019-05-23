package com.yesong.ghoststory.about;

import com.yesong.ghoststory.BasePresenter;
import com.yesong.ghoststory.BaseView;

public interface AboutContract {
    interface Presenter extends BasePresenter{
        void rate();

        void feedback();

        void followOnGithub();

        void donate();
    }

    interface View extends BaseView<Presenter> {
        void showRateError();

        void showFeedbackError();

        void showBrowserNotFoundError();
    }

}
