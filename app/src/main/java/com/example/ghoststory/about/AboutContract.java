package com.example.ghoststory.about;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;

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
