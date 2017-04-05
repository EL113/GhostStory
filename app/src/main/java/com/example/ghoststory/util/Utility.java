package com.example.ghoststory.util;

import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.bean.RequestResult;
import com.google.gson.Gson;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class Utility {
    public static RequestResult handleStoryListData(String response) {

        try {
            return new Gson().fromJson(response,RequestResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DetailResult handleStoryDetailData(String response) {
        try {
            return new Gson().fromJson(response, DetailResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
