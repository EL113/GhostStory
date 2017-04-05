package com.example.ghoststory.homepage;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.db.DbContentList;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public interface BookmarksContract {
    interface View extends BaseView<Presenter> {

        void showResults(List<DbContentList> list);

        void showLoading();

        void stopLoading();

        void dataError();

    }

    interface Presenter extends BasePresenter {

        void loadResults();

        void startReading(int position);

        void checkForFreshData();

    }
}
