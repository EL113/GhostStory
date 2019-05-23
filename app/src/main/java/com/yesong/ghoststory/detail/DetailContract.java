package com.yesong.ghoststory.detail;

import com.yesong.ghoststory.BasePresenter;
import com.yesong.ghoststory.BaseView;
import com.yesong.ghoststory.db.DbContentList;

public interface DetailContract {
    interface Presenter extends BasePresenter {
        void setIdContent(DbContentList item);

        void loadMore();

        void loadPost(String page);

        void setThumbUp(DbContentList item);

        void setThumbDown(DbContentList item);

        void setCollection(DbContentList item);

        void deleteStory();
    }

    interface View extends BaseView<Presenter> {
        void showError();

        void setData(DbContentList content);

        void showLoading();

        void stopLoading();

        void showMaxPage();
    }

}
