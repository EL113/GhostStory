package com.example.ghoststory.detail;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;

public interface DetailContract {
    interface Presenter extends BasePresenter {
        void setIdContent(String id);

        void addToOrDeleteFromBookmarks();

        void copyText();

        boolean queryIfIsBookmarked();

        void loadMore();

        void loadPost(String page);
    }

    interface View extends BaseView<Presenter> {
        void showError();

        void setData(String title,String image,String text);

        void showCopyTextError();

        void showTextCopied();

        void showAddedToBookmarks();

        void showDeletedFromBookmarks();

        void showLinkError();

        void showLoading();

        void stopLoading();

        void showMaxPage();
    }

}
