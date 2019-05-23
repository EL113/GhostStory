package com.yesong.ghoststory.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class API {
    private static String STORY_LIST = "https://route.showapi.com/955-1?";
    private static String STORY_CONTENT = "https://route.showapi.com/955-2?";
    private static String STORY_PAGE = "&page=";
    private static String STORY_CONTENT_API_ID="&showapi_appid=33642&showapi_timestamp=";
    private static String API_ID = "&showapi_appid=33642&showapi_timestamp=";
    private static String API_SIGN = "&showapi_sign=b8f27f88131240c8b8c39b2b8c6e6691";

    public static String getStoryListUrl(int page, String type) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        return API.STORY_LIST + "page=" + page + API.API_ID + timestamp + "&type=" + type + API.API_SIGN;
    }

    public static String getStoryContentUrl(String page, String idContent) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        return API.STORY_CONTENT + "id=" + idContent + API.STORY_PAGE + page + API.STORY_CONTENT_API_ID + timestamp + API.API_SIGN;
    }
}
