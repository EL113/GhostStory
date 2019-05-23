package com.yesong.ghoststory.homepage;

import com.yesong.ghoststory.BasePresenter;
import com.yesong.ghoststory.BaseView;
import com.yesong.ghoststory.bean.StoryType;

import java.util.List;

public interface StoryTypesContract {

    interface View extends BaseView<Presenter> {
        void setTypes(List<StoryType> list);
    }

    interface Presenter extends BasePresenter {
        void chooseType(int position);
    }
}
