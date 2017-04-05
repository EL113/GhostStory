package com.example.ghoststory.search;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.db.DbContentList;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/4/1.
 */

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
