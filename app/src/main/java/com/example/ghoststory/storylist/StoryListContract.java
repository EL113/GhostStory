package com.example.ghoststory.storylist;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.db.DbContentList;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/28.
 */

public class StoryListContract {
    interface View extends BaseView<Presenter> {
        void startLoading();

        void stopLoading();

        void showError();

        void showResults(List<DbContentList> lists);

    }

    interface Presenter extends BasePresenter{

        void refresh();

        void loadPost(String id,int page,final boolean clearing);

        void startReading(int position);

        void loadMore();

        void setTypeId(String typeId);

    }

}
