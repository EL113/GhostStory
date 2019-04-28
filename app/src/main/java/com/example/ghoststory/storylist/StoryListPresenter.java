package com.example.ghoststory.storylist;

import android.content.Context;
import android.content.Intent;

import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.bean.RequestResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;
import com.example.ghoststory.homepage.StoryListContract;
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


public class StoryListPresenter implements StoryListContract.Presenter{
    private Context context;
    private StoryListContract.View view;
    private String typeId;
    private int page;
    private List<DbContentList> dataList = new ArrayList<>();//请求的所有数据

    StoryListPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    private void loadPost(final String typeId, boolean clearing) {
        if (clearing) {
            view.showLoading();
            dataList.clear();
        }

        boolean isNetworkAvailable = NetworkState.networkConnected(context);
        if (!isNetworkAvailable){
            dataList = DataSupport.where("typeName = ?","recommendations").find(DbContentList.class);
            view.showResults(dataList);
            view.stopLoading();
            view.showError("网络异常");
        }

        //获取当前时间
        HttpUtil.sendOkHttpRequest(API.getStoryListUrl(page, typeId), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showError("request error");
                view.stopLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                RequestResult requestResult = Utility.handleStoryListData(responseText);
                if (requestResult == null) {
                    view.showError("解析错误");
                    view.stopLoading();
                    return;
                }

                List<ContentList> responseList = new ArrayList<>();
                try {
                    responseList = requestResult.getResponseBody().getPagebean().getContentlist();
                } catch (EmptyStackException e) {
                    e.printStackTrace();
                    view.stopLoading();
                    view.showError("parse error");
                }

                String now = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                        .format(new Date());
                for (ContentList item : responseList) {
                    DbContentList content = new DbContentList();
                    //先查看这条数据是否在本地数据库存在，如果不存在就添加进本地数据库
                    if (!Utility.queryIfIDExists(item.getLink())) {
                        content = new DbContentList();
                        content.setIdContent(item.getLink());
                        content.setTitle(item.getTitle());
                        content.setLink(item.getLink());
                        content.setDesc(item.getDesc());
                        content.setImg(item.getImg());
                        content.setIsBookmarked("0");
                        content.setTypeName(typeId);
                        content.setTime(Double.valueOf(now));
                        content.save();
                    }
                    dataList.add(content);
                }
                view.stopLoading();
                view.showResults(dataList);
            }
        });
    }

    @Override
    public void start() {
        //随机加载某一页
        Random random = new Random();
        page = random.nextInt(50)+1;
        loadPost(typeId, true);
        view.stopLoading();
    }

    //进入详情页面时，要传递信息过去
    @Override
    public void startReading(int position) {
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("idContent", dataList.get(position).getIdContent())
                .putExtra("title", dataList.get(position).getTitle())
                .putExtra("image", dataList.get(position).getImg())
                .putExtra("link", dataList.get(position).getLink()));
    }

    //加载下一页
    @Override
    public void loadMore() {
        ++page;
        loadPost(typeId, false);
        view.stopLoading();
    }

    void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
