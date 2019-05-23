package com.yesong.ghoststory.detail;

import android.content.Context;

import com.yesong.ghoststory.bean.CommonResult;
import com.yesong.ghoststory.bean.DetailResult;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.homepage.CommonCallback;
import com.yesong.ghoststory.util.APIUtil;
import com.yesong.ghoststory.util.HttpUtil;
import com.yesong.ghoststory.util.NetworkState;
import com.yesong.ghoststory.util.Utility;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailPresenter implements DetailContract.Presenter{
    private DetailContract.View view;
    private Context context;
    private String idContent;
    private DbContentList item;
    private String textDetail = " ";
    private int allPages;
    private int currentPage = 0;


    DetailPresenter(Context context, DetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        boolean isContentCached = item != null && item.getText() != null;
        if (isContentCached) {
            textDetail = item.getText();
            currentPage = item.getCurrentPage();
            allPages = item.getMaxPage();
            view.setData(item);
        } else {
            view.showLoading();
            loadPost("0");
            view.stopLoading();
        }
    }

    @Override
    public void loadPost(String page) {
        boolean isNetworkAvailable = NetworkState.networkConnected(context);
        if (!isNetworkAvailable){
            textDetail = "网络异常";
            item.setText(textDetail);
            view.setData(item);
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("id", item.getIdContent())
                .add("type", item.getTypeName())
                .add("page", String.valueOf(currentPage))
                .build();

        HttpUtil.post(APIUtil.content(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                textDetail = "Error";
                item.setText(textDetail);
                view.showError();
                //这里需要一个错误标题和错误图片，出现错误的文字
                view.setData(item);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseText = response.body().string();
                    DetailResult detailResult = Utility.resolveResponse(responseText, DetailResult.class);
                    if (detailResult == null) {
                        view.showError();
                        return;
                    }
                    allPages = detailResult.getMaxPage();
                    currentPage = detailResult.getCurrentPage();
                    List<String> contentList = detailResult.getContent();
                    StringBuilder contentBuffer = new StringBuilder();
                    contentBuffer.append(item.getText());
                    for (String contentItem : contentList)
                        contentBuffer.append(contentItem);

                    item.setText(contentBuffer.toString());
                    DbContentList updateItem = new DbContentList();
                    updateItem.setCurrentPage(currentPage);
                    updateItem.setMaxPage(allPages);
                    updateItem.setText(contentBuffer.toString());
                    updateItem.updateAll("idContent=?", idContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    textDetail = "Parse Error";
                    view.showError();
                    view.setData(item);
                }
                view.setData(item);
            }
        });
    }

    @Override
    public void loadMore() {
        ++currentPage;
        if (currentPage <= allPages) {
            loadPost(String.valueOf(currentPage));
        } else {
            view.showMaxPage();
        }
    }

    @Override
    public void setThumbUp(DbContentList item) {
        item.save();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", item.getIdContent())
                .add("type", item.getTypeName())
                .add("operation", item.getIsThumbUp() == 0 ? "1" : "0")
                .build();

        HttpUtil.post(APIUtil.thumbUp(), requestBody, new CommonCallback(view));
    }

    @Override
    public void setThumbDown(DbContentList item) {
        item.save();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", item.getIdContent())
                .add("type", item.getTypeName())
                .add("operation", item.getIsThumbDown() == 0 ? "1" : "0")
                .build();

        HttpUtil.post(APIUtil.thumbDown(), requestBody, new CommonCallback(view));
    }

    @Override
    public void setCollection(DbContentList item) {
        item.save();
        RequestBody requestBody = new FormBody.Builder()
                .add("id", item.getIdContent())
                .add("type", item.getTypeName())
                .add("operation", item.getIsBookmarked() == 0 ? "1" : "0")
                .build();

        HttpUtil.post(APIUtil.collect(), requestBody, new CommonCallback(view));
    }

    @Override
    public void setIdContent(DbContentList item) {
        this.idContent = item.getIdContent();
        this.item = item;
    }

    @Override
    public void deleteStory() {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", item.getIdContent())
                .add("type", item.getTypeName())
                .add("authorId", item.getAuthorId())
                .build();

        HttpUtil.post(APIUtil.deleteStory(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                CommonResult result = Utility.resolveResponse(responseText, CommonResult.class);
                if (result.getCode() == null || result.getCode() != 0){
                    view.showError();
                }
                item.delete();
            }
        });
    }
}
