package com.example.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecommendationsPresenter implements StoryListContract.Presenter{
    private Context context;
    private StoryListContract.View view;
    private int page;
    private String[] typeNames = {"dp", "cp" , "xy" , "yy" , "jl" , "mj" , "ly" , "yc" , "neihanguigushi"};
    private List<DbContentList> list = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;

    //包内可访问
    RecommendationsPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    }

    @Override
    public void start() {
        Random random = new Random();
        page = random.nextInt(100);
        String typeName = typeNames[page % 8];
        loadStory(typeName, true);
        view.stopLoading();
    }

    private void loadStory(String type, boolean clearing) {
        if (clearing) {
            view.showLoading();
            list.clear();
        }

        boolean isNetworkAvailable = NetworkState.networkConnected(context);
        //没有网络并且是第一页就加载内存中的数据
        if (!isNetworkAvailable && list.size() == 0) {
            list = DataSupport.where("typeName = ?","recommendations").find(DbContentList.class);
            view.showResults(list);
            view.stopLoading();
            view.showError("网络异常");
            return;
        } else if (!isNetworkAvailable){
            view.showError("网络异常");
            return;
        }

        HttpUtil.sendOkHttpRequest(API.getStoryListUrl(page, type), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showError("网络请求失败");
                view.stopLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                RequestResult requestResult = Utility.handleStoryListData(responseText);

                if (requestResult == null) {
                    view.stopLoading();
                    view.showError("数据解析异常");
                    return;
                }

                if (requestResult.getResponseCode() != 0){
                    view.showError(requestResult.getResponseError());
                    view.stopLoading();
                    return;
                }

                List<ContentList> responseList = new ArrayList<>();
                try {
                    responseList = requestResult.getResponseBody().getPagebean().getContentlist();
                } catch (EmptyStackException e) {
                    e.printStackTrace();
                    view.stopLoading();
                    view.showError("network request error");
                }

                String now = simpleDateFormat.format(new Date());
                for (ContentList item:responseList) {
                    DbContentList content = new DbContentList();
                    content.setTitle(item.getTitle());
                    content.setIdContent(item.getLink());
                    content.setImg(item.getImg());
                    content.setDesc(item.getDesc());
                    content.setLink(item.getLink());
                    content.setIsBookmarked("0");
                    content.setTypeName("recommendations");
                    content.setTime(Double.valueOf(now));

                    if (!Utility.queryIfIDExists(item.getLink())) {
                        content.save();
                    }

                    list.add(content);
                }
                view.showResults(list);
                view.stopLoading();
            }
        });
    }

    @Override
    public void loadMore () {
        ++page;
        Random random = new Random();
        int typeNumber = random.nextInt(8);
        String typeName = typeNames[typeNumber];
        loadStory(typeName, false);
        view.stopLoading();
    }

    @Override
    public void startReading (int position){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", list.get(position).getIdContent())
                .putExtra("title", list.get(position).getTitle());
        context.startActivity(intent);
    }
}