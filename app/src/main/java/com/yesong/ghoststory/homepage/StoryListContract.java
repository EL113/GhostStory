package com.yesong.ghoststory.homepage;

import com.yesong.ghoststory.BasePresenter;
import com.yesong.ghoststory.BaseView;
import com.yesong.ghoststory.db.DbContentList;

import java.util.List;

public interface StoryListContract {
    interface View extends BaseView<Presenter> {
        void showError(String error);

        void showLoading();

        void stopLoading();

        void showResults(List<DbContentList> list);
    }

    interface Presenter extends BasePresenter {
        void loadMore();

        void startReading(int position);
    }
}
