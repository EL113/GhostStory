package com.yesong.ghoststory;

import android.view.View;

public interface BaseView<T> {
    void initView(View view);

    void setPresenter(T presenter);

}
