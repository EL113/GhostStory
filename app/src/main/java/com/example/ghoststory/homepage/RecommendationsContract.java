package com.example.ghoststory.homepage;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.db.DbContentList;

import java.util.List;

public interface RecommendationsContract {
    interface View extends BaseView<Presenter> {
        void showError();

        void showLoading();

        void stopLoading();

        void showResults(List<DbContentList> list);

        void showAnotherError();

    }

    interface Presenter extends BasePresenter {
        void loadMore();

        void startReading(int position);
    }
}
