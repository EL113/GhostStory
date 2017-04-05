package com.example.ghoststory.homepage;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.db.DbContentList;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public interface RecommendationsContract {
    interface View extends BaseView<Presenter> {
        void showError();

        void showLoading();

        void stopLoading();

        void showResults(List<DbContentList> list);

        void showAnotherError();

    }

    interface Presenter extends BasePresenter {
        void loadPosts(int page,String type, boolean clearing);

        void loadMore();

        void startReading(int position);
    }
}
