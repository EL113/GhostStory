package com.yesong.ghoststory.search;

import com.yesong.ghoststory.BasePresenter;
import com.yesong.ghoststory.BaseView;
import com.yesong.ghoststory.db.DbContentList;

import java.util.List;


public interface SearchContract {
    interface Presenter extends BasePresenter {
        void loadResults(String queryText);

        void startReading(int position);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();

        void stopLoading();

        void showResults(List<DbContentList> list);

    }

}
