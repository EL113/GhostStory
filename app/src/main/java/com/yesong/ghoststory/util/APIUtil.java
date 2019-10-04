package com.yesong.ghoststory.util;

public class APIUtil {
    private static final String host = "http://192.168.1.111:8888/ghoststory/openapi";
    private static final String recommendList = "/recommend?pageNo=%s&pageSize=%s";
    private static final String thumbUp = "/thumbUp";
    private static final String thumbDown = "/thumbDown";
    private static final String collect = "/collect";
    private static final String content = "/content?id=%s&type=%s&pageNo=%s&pageSize=5";
    private static final String typeStory = "/story/list?pageNo=%s&pageSize=%s";
    private static final String publishStory = "/publish";
    private static final String deleteStory = "/delete";
    private static final String auditResult = "/audit/list?ids=%s";
    private static final String storySearch = "/story/search?keyword=%s&pageNo=%s&pageSize=%s";

    public static String getRecommendListUrl(int pageNo, int pageSize){
        return String.format(host + recommendList, pageNo, pageSize);
    }

    public static String thumbUp(){
        return host + thumbUp;
    }

    public static String thumbDown(){
        return host + thumbDown;
    }

    public static String collect(){
        return host + collect;
    }

    public static String content(String id, String type, String pageNo){
        return String.format(host + content, id, type, pageNo);
    }

    public static String storyList(int pageNo, int pageSize){
        return String.format(host + typeStory, pageNo, pageSize);
    }

    public static String publishStory(){
        return host + publishStory;
    }

    public static String deleteStory(){
        return host + deleteStory;
    }

    public static String auditResult(String ids){
        return String.format(host + auditResult, ids);
    }

    public static String storySearch(String keyword, int pageNo, int pageSize){
        return String.format(host + storySearch, keyword, pageNo, pageSize);
    }
}
