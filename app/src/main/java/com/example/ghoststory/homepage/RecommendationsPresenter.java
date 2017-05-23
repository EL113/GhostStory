package com.example.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.bean.RequestResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;
import com.example.ghoststory.util.API;
import com.example.ghoststory.util.HttpUtil;
import com.example.ghoststory.util.NetworkState;
import com.example.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class RecommendationsPresenter implements RecommendationsContract.Presenter{

    private String now;
    private String url;
    private Context context;
    private RecommendationsContract.View view;
    private int page;
    private String[] typeNames = {"dp", "cp" , "xy" , "yy" , "jl" , "mj" , "ly" , "yc" , "neihanguigushi"};
    ;
    private String typeName;
    private List<DbContentList> list = new ArrayList<>();
    private DbContentList content;

    public RecommendationsPresenter(Context context, RecommendationsContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        Random random1 = new Random();
        page = random1.nextInt(50)+1;
        Random random2 = new Random();
        int typeNumber = random2.nextInt(8);
        typeName = typeNames[typeNumber];
        loadPosts(page, typeName, true);
        view.stopLoading();
    }

    @Override
    public void loadPosts(int page, String type,final boolean clearing) {

        if (clearing) {
            view.showLoading();
        }

        getNow();

        url = API.STORY_LIST + "page=" + String.valueOf(page) + API.API_ID + now + "&type=" + type + API.API_SIGN;

        if (NetworkState.networkConnected(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtil.sendOkHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            view.showError();
                            view.stopLoading();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseText = response.body().string();

                            final RequestResult requestResult = Utility.handleStoryListData(responseText);

                            if (clearing) {
                                list.clear();
                            }
                            if (requestResult == null) {
                                view.stopLoading();
                                view.showAnotherError();
                                return;
                            }

                            List<ContentList> responseList = new ArrayList<>();
                            try {
                                responseList = requestResult.getResponseBody().getPagebean().getContentlist();
                            } catch (EmptyStackException e) {
                                e.printStackTrace();
                                view.stopLoading();
                                view.showError();
                            }

                                for (ContentList item:responseList) {
                                    if (!queryIfIDExists(item.getId())) {
                                        //每次循环都要新建一次DbContentList对象，被填充过的对象不能被再次被填充
                                        content = new DbContentList();
                                        content.setTitle(item.getTitle());
                                        content.setIdContent(item.getId());
                                        content.setImg(item.getImg());
                                        content.setDesc(item.getDesc());
                                        content.setLink(item.getLink());
                                        content.setIsBookmarked("0");
                                        content.setTypeName("recommendations");
                                        content.setTime(Double.valueOf(now));
                                        content.save();
                                        list.add(content);
                                    } else {
                                        content = new DbContentList();
                                        content = DataSupport.where("idContent=?", item.getId()).find(DbContentList.class).get(0);
                                        list.add(content);
                                    }
                                }
                            view.showResults(list);
                            view.stopLoading();
                        }
                    });
                }
            }).start();
        } else {
            if (clearing) {
                list.clear();
                list = DataSupport.where("typeName = ?","recommendations").find(DbContentList.class);
                view.showResults(list);
                view.stopLoading();
            } else {
                view.showError();
            }
        }
    }

    @Override
    public void loadMore () {
        Random random = new Random();
        int typeNumber = random.nextInt(8);
        typeName = typeNames[typeNumber];
        loadPosts(page + 1, typeName, false);
    }

    @Override
    public void startReading ( int position){

        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("idContent", list.get(position).getIdContent())
                .putExtra("title", list.get(position).getTitle())
                .putExtra("image", list.get(position).getImg())
                .putExtra("link", list.get(position).getLink()));

    }

    public boolean queryIfIDExists(String id) {
        List<DbContentList> dbContentList = DataSupport.where("idContent = ?", id).find(DbContentList.class);
        if (dbContentList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void getNow() {
        Calendar c = Calendar.getInstance();
        String second =String.valueOf(c.get(Calendar.SECOND)) ;
        int secondNumber=c.get(Calendar.SECOND);
        if (secondNumber >= 0 && secondNumber < 10) {
            second = "0" + second;
        }
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        int monthNumber = c.get(Calendar.MONTH)+1;
        if (monthNumber >= 0 && monthNumber < 10) {
            month = "0" + month;
        }
        String date = String.valueOf(c.get(Calendar.DATE));
        int dateNumber=c.get(Calendar.DATE);
        if (dateNumber >= 0 && dateNumber < 10) {
            date = "0" + date;
        }
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        int hourNumber=c.get(Calendar.HOUR_OF_DAY);
        if (hourNumber >= 0 && hourNumber < 10) {
            hour = "0"+hour;
        }
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        int minuteNumber=c.get(Calendar.MINUTE);
        if (minuteNumber >= 0 && minuteNumber < 10) {
            minute = "0" + minute;
        }

        now = year + month + date + hour + minute + second;
    }
}
