package com.example.ghoststory.about;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;

/**
 * Created by Daniel hunt on 2017/4/1.
 */

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
