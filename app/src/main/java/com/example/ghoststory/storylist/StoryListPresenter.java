package com.example.ghoststory.storylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.ghoststory.R;
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
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Daniel hunt on 2017/3/28.
 */

public class StoryListPresenter implements StoryListContract.Presenter{
    private Context context;
    private StoryListContract.View view;
    private String typeId;
    private String page;
    private String now;
    private List<DbContentList> dataList = new ArrayList<>();
    private DbContentList content;

    public StoryListPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }
    @Override
    public void loadPost(final String typeId, String page,final boolean clearing) {
        if (clearing) {
            view.startLoading();
        }

        API api = new API();
        getNow();

        final String url = api.STORY_LIST + "page=" + page + api.API_ID + now + "&type=" + typeId + api.API_SIGN;
        if (NetworkState.networkConnected(context)) {
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    view.showError();
                    view.startLoading();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();

                    RequestResult requestResult = Utility.handleStoryListData(responseText);
                    if (clearing) {
                        dataList.clear();
                    }

                    if (requestResult == null) {
                        view.stopLoading();
                        view.showError();
                        return;
                    }

                    List<ContentList> responseList = new ArrayList<ContentList>();
                    try {
                        responseList = requestResult.getResponseBody().getPagebean().getContentlist();
                    } catch (EmptyStackException e) {
                        e.printStackTrace();
                        view.stopLoading();
                        view.showError();
                    }

                    for (ContentList item : responseList) {
                        if (!queryIfIDExists(item.getId())) {
                            content = new DbContentList();
                            content.setIdContent(item.getId());
                            content.setTitle(item.getTitle());
                            content.setLink(item.getLink());
                            content.setDesc(item.getDesc());
                            content.setImg(item.getImg());
                            content.setIsBookmarked("0");
                            content.setTypeName(typeId);
                            content.setTime(Double.valueOf(now));
                            content.save();
                            dataList.add(content);
                        } else {
                            content = new DbContentList();
                            content = DataSupport.where("idContent=?", item.getId()).find(DbContentList.class).get(0);
                            dataList.add(content);
                        }
                    }
                    view.stopLoading();
                    view.showResults(dataList);
                }
            });
        }else {
        if (clearing) {
            dataList.clear();
            dataList = DataSupport.where("typeName=?",typeId).find(DbContentList.class);
        }
        }
    }

    @Override
    public void refresh() {
        start();
    }

    @Override
    public void start() {
        Random random = new Random();
        page = String.valueOf(random.nextInt(50)+1);
        loadPost(typeId,page,true);
    }

    @Override
    public void startReading(int position) {
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("idContent", dataList.get(position).getIdContent())
                .putExtra("title", dataList.get(position).getTitle())
                .putExtra("image", dataList.get(position).getImg())
                .putExtra("link", dataList.get(position).getLink()));

    }

    @Override
    public void loadMore() {
        loadPost(typeId,page+1,false);
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

    public boolean queryIfIDExists(String idContent) {
        List<DbContentList> dbContentList = DataSupport.where("idContent = ?", idContent).find(DbContentList.class);
        if (dbContentList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
