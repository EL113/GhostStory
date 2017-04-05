package com.example.ghoststory.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel hunt on 2017/3/30.
 */

public class DetailBody {
    private int allPages;

    @SerializedName("ret_code")
    private int bodyCode;

    private String text;

    private String currentPage;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getBodyCode() {
        return bodyCode;
    }

    public void setBodyCode(int bodyCode) {
        this.bodyCode = bodyCode;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
