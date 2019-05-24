package com.yesong.ghoststory.util;

public class APIUtil {
    private static final String host = "http://192.168.1.111:7080/ghostServer";
    private static final String recommendList = "/recommend";
    private static final String thumbUp = "/thumbUp";
    private static final String thumbDown = "/thumbDown";
    private static final String collect = "/collect";
    private static final String content = "/content";
    private static final String typeStory = "/type/list";
    private static final String publishStory = "/publish";
    private static final String deleteStory = "/delete";
    private static final String auditResult = "/audit/result";

    public static String getRecommendListUrl(){
        return host + recommendList;
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

    public static String content(){
        return host + content;
    }

    public static String storyList(){
        return host + typeStory;
    }

    public static String publishStory(){
        return host + publishStory;
    }

    public static String deleteStory(){
        return host + deleteStory;
    }

    public static String auditResult(){
        return host + auditResult;
    }
}
