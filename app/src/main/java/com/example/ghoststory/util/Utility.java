package com.example.ghoststory.util;

import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.bean.RequestResult;
import com.example.ghoststory.db.DbContentList;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

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

    public static boolean queryIfIDExists(String idContent) {
        List<DbContentList> dbContentList = DataSupport.where("idContent = ?", idContent).find(DbContentList.class);
        return dbContentList.size() > 0;
    }
}
