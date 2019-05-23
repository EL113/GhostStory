package com.yesong.ghoststory.bean;

/**
 * Created by Daniel hunt on 2017/3/26.
 */

public class ContentList {
    private String id;
    private String title;
    private String desc;
    private String type;
    private String createTime;
    private String author;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ContentList [id=" + id + ",title=" + title + ",desc=" + desc;
    }
}
