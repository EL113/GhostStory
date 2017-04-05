package com.example.ghoststory.bean;

import android.widget.ImageView;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class StoryType {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String title;
    private String img;
}
