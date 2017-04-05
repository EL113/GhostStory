package com.example.ghoststory.bean;

import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/26.
 */

public class Pagebean {
    private String allPages;
    private List<ContentList> contentlist;

    private int currentPage;
    private String maxResult;

    @Override
    public String toString() {
        return "Pagebean [allPages=" + allPages + ",contentlist=" + contentlist + ",currentPage=" + currentPage
                + ",maxResult=" + maxResult + "]";
    }

    public String getAllPages() {
        return allPages;
    }

    public void setAllPages(String allPages) {
        this.allPages = allPages;
    }

    public List<ContentList> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<ContentList> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(String maxResult) {
        this.maxResult = maxResult;
    }
}
