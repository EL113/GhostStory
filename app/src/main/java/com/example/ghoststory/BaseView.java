package com.example.ghoststory;

import android.view.View;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public interface BaseView<T> {
    void initView(View view);

    void setPresenter(T presenter);

}
