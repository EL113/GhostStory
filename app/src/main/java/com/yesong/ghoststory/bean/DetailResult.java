package com.yesong.ghoststory.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/30.
 */

public class DetailResult {

    private int thumbUp = 0;

    private int thumbDown = 0;

    private int maxPage = 0;

    private int currentPage = 0;

    private List<String> content;

    public int getThumbUp() {
        return thumbUp;
    }

    public void setThumbUp(int thumbUp) {
        this.thumbUp = thumbUp;
    }

    public int getThumbDown() {
        return thumbDown;
    }

    public void setThumbDown(int thumbDown) {
        this.thumbDown = thumbDown;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
