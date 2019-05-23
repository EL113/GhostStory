package com.yesong.ghoststory.db;

import org.litepal.crud.DataSupport;

public class DbContentList extends DataSupport {
    private int id;
    private String idContent;
    private String title;
    private String desc;
    private String text;
    private int collectionCount = 0;
    private int isBookmarked = 0;
    private int thumbUp = 0;
    private int isThumbUp = 0;
    private int thumbDown = 0;
    private int isThumbDown = 0;
    private String typeName;
    private int maxPage;
    private int currentPage;
    private String createTime;
    private int itemType = 0;   //列表元素的类型，默认为0正常，1 正在加载，2 没有更多，3 没有数据错误，4 什么都没找到
    private String typeMessage;
    private String author;
    private String authorId = "-1";

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setIsThumbUp(int isThumbUp) {
        this.isThumbUp = isThumbUp;
    }

    public int getIsThumbUp() {
        return isThumbUp;
    }

    public void setIsThumbDown(int isThumbDown) {
        this.isThumbDown = isThumbDown;
    }

    public int getIsThumbDown() {
        return isThumbDown;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
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

    public void setIsBookmarked(int isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public int getIsBookmarked() {
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

    public void increaseThumbUp(){
        if (isThumbUp == 0){
            isThumbUp = 1;
            thumbUp++;
        }
    }

    public void increaseThumbDown(){
        if (isThumbDown == 0){
            isThumbDown = 1;
            thumbDown++;
        }
    }

    public void decreaseThumbUp(){
        if (isThumbUp == 1){
            thumbUp--;
            isThumbUp = 0;
        }
    }

    public void decreaseThumbDown(){
        if (isThumbDown == 1){
            isThumbDown = 0;
            thumbDown--;
        }
    }

    public void bookmark(){
        if (isBookmarked == 0){
            isBookmarked = 1;
            collectionCount++;
        }
    }

    public void cancelBookmark(){
        if (isBookmarked == 1){
            isBookmarked = 0;
            collectionCount--;
        }
    }

    @Override
    public String toString() {
        return "DbContentList{" +
                "id=" + id +
                ", idContent='" + idContent + '\'' +
                ", title='" + title + '\'' +
                ", collectionCount=" + collectionCount +
                ", isBookmarked=" + isBookmarked +
                ", thumbUp=" + thumbUp +
                ", isThumbUp=" + isThumbUp +
                ", thumbDown=" + thumbDown +
                ", isThumbDown=" + isThumbDown +
                ", typeName='" + typeName + '\'' +
                ", maxPage=" + maxPage +
                ", currentPage=" + currentPage +
                ", createTime='" + createTime + '\'' +
                ", itemType=" + itemType +
                ", typeMessage='" + typeMessage + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
