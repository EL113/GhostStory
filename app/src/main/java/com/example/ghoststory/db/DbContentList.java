package com.example.ghoststory.db;

import org.litepal.crud.DataSupport;

public class DbContentList extends DataSupport {

    private int id;
    private String idContent;
    private String title;
    private String desc;
    private String link;
    private String img;
    private String text;
    private String isBookmarked;
    private String typeName;
    private String maxPage;
    private String currentPage;
    private double time;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setIdContent(String idContent) {
        this.idContent = idContent;
    }

    public String getIdContent() {
        return idContent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setIsBookmarked(String isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public String getIsBookmarked() {
        return isBookmarked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
