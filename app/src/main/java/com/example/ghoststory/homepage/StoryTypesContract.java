package com.example.ghoststory.homepage;

import com.example.ghoststory.BasePresenter;
import com.example.ghoststory.BaseView;
import com.example.ghoststory.bean.StoryType;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public interface StoryTypesContract {

    interface View extends BaseView<Presenter> {
        void setTypes(List<StoryType> list);

    }

    interface Presenter extends BasePresenter {
        void chooseType(int position);
    }
}
