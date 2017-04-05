package com.example.ghoststory.bean;

/**
 * Created by Daniel hunt on 2017/3/26.
 */

public class ContentList {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String id;
    private String title;
    private String desc;
    private String link;
    private String img;

    @Override
    public String toString() {
        return "ContentList [id=" + id + ",title=" + title + ",desc=" + desc + ",link=" + link + ",img=" + img + "]";
    }
}
