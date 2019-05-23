package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.yesong.ghoststory.adapter.RecommendationsAdapter;
import com.yesong.ghoststory.bean.ContentList;
import com.yesong.ghoststory.bean.RecommendResponse;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;
import com.yesong.ghoststory.util.APIUtil;
import com.yesong.ghoststory.util.HttpUtil;
import com.yesong.ghoststory.util.NetworkState;
import com.yesong.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecommendationsPresenter implements StoryListContract.Presenter{
    private Context context;
    private StoryListContract.View view;
    private int startIndex = 0;
    private List<DbContentList> list = new ArrayList<>();

    RecommendationsPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        startIndex = 0;
        view.showLoading();
        loadStory();
        view.stopLoading();
    }

    private void loadStory() {
        if (startIndex == 0) {
            view.showLoading();
            list.clear();
        }

        boolean isNetworkAvailable = NetworkState.networkConnected(context);
        if (!isNetworkAvailable) {
            networkError();
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("startIndex", String.valueOf(startIndex))
                .build();

        HttpUtil.post(APIUtil.getRecommendListUrl(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                networkError();
                view.stopLoading();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                RecommendResponse requestResult = Utility.handleStoryListData(responseText, RecommendResponse.class);

                if (requestResult == null) {
                    networkError();
                    view.stopLoading();
                    return;
                }

                if (requestResult.getCode() != 0){
                    networkError();
                    view.stopLoading();
                    return;
                }

                List<ContentList> responseList =  requestResult.getResult();
                List<DbContentList> contentList = new LinkedList<>();

                Date date = new Date();
                String time = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
                for (ContentList item:responseList) {
                    DbContentList content = new DbContentList();
                    content.setIdContent(item.getId());
                    content.setAuthor(item.getAuthor());
                    content.setTitle(item.getTitle());
                    content.setDesc(item.getDesc());
                    content.setTypeName(item.getType());
                    content.setCreateTime(time);
                    content.setItemType(RecommendationsAdapter.TYPE_NORMAL);

                    boolean exist = Utility.queryIfIDExists(item.getId(), item.getType());
                    if (!exist) {
                        content.save();
                    }

                    //数据的处理还需要再封装一下，第一页和不是第一页的判断
                    contentList.add(content);
                }

                addRequestData(contentList);
                view.stopLoading();
            }
        });
    }

    @Override
    public void loadMore () {
        startIndex += 15;
        loadStory();
        view.stopLoading();
    }

    @Override
    public void startReading (int position){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", list.get(position).getIdContent())
                .putExtra("typeName", list.get(position).getTypeName());
        context.startActivity(intent);
    }

    private void networkError() {
        //查询数据库中的数据
        List<DbContentList> dbList = DataSupport.findAll(DbContentList.class);
        if (startIndex == 0 && dbList.size() < 15){
            //第一页,完全没有数据
            addFirstPageNetworkError();
        } else if (startIndex == 0){
            //第一页,有足够的数据填充页面，加载新的数据库中的数据
            addFirstPageData(dbList);
            view.showError("网络异常");
        } else {
            //不是第一页，直接提示网络异常
            addNetworkError();
            view.showError("网络异常");
        }

        view.showResults(list);
        view.stopLoading();
    }

    //第一页没有数据，清空数据，加载网络错误页面
    private void addFirstPageNetworkError(){
        list.clear();
        DbContentList errorItem = new DbContentList();
        errorItem.setItemType(RecommendationsAdapter.TYPE_NETWORK_ERROR);
        errorItem.setTypeMessage("没有网络，请刷新重试");

        list.add(errorItem);
        view.showResults(list);
    }

    //加载第一页数据，加载缓存数据
    private void addFirstPageData(List<DbContentList> newData){
        list.clear();
        list.addAll(newData);

        DbContentList loadingItem = new DbContentList();
        loadingItem.setItemType(RecommendationsAdapter.TYPE_LOADING);
        list.add(loadingItem);

        view.showResults(list);
    }

    //加载失败错误，去掉末尾提示项
    private void addNetworkError(){
        DbContentList item = list.get(list.size() - 1);
        if (item.getItemType() != 0){
            list.remove(list.size() - 1);
            view.showResults(list);
        }
    }

    //加载请求到的数据
    private void addRequestData(List<DbContentList> contentList) {
        if (contentList.isEmpty() && startIndex == 0){
            addFirstPageEmptyError();
        } else if (contentList.isEmpty()){
            noMoreNewData();
        } else {
            addNewData(contentList);
        }
    }

    //加载新数据，再提示项前加数据
    private void addFirstPageEmptyError(){
        list.clear();
        DbContentList firstPageEmptyError = new DbContentList();
        firstPageEmptyError.setItemType(RecommendationsAdapter.TYPE_EMPTY_ERROR);
        list.add(firstPageEmptyError);
        view.showResults(list);
    }

    //没有更多数据，直接替换最后一条提示信息的数据为没有更多
    private void noMoreNewData(){
        DbContentList item = list.get(list.size() - 1);
        if (item.getItemType() != 0){
            list.remove(list.size() - 1);
        }

        DbContentList messageItem = new DbContentList();
        messageItem.setItemType(RecommendationsAdapter.TYPE_NO_MORE);
        list.add(messageItem);

        view.showResults(list);
    }

    private void addNewData(List<DbContentList> contentList) {
        if (startIndex == 0){
            addFirstPageData(contentList);
            return;
        }

        DbContentList item = list.get(list.size() - 1);
        DbContentList loadingItem = new DbContentList();
        loadingItem.setItemType(RecommendationsAdapter.TYPE_LOADING);
        if (item.getItemType() != 0){
            list.remove(list.size() - 1);
        }

        list.addAll(contentList);
        list.add(loadingItem);
        view.showResults(list);
    }
}