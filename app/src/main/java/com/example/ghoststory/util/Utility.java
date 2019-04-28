package com.example.ghoststory.util;

import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.bean.RequestResult;
import com.example.ghoststory.db.DbContentList;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static RequestResult handleStoryListData(String response) {
        String regEx_html = "<[^>]+>";
        String regEx_special = "\\&[a-zA-Z]{1,10};";

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(response);
        response = m_html.replaceAll(""); // 过滤html标签
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(response);
        response = m_special.replaceAll(""); // 过滤特殊标签

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
