package com.example.ghoststory.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.ghoststory.R;
import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.util.API;
import com.example.ghoststory.util.HttpUtil;
import com.example.ghoststory.util.NetworkState;
import com.example.ghoststory.util.Utility;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static android.content.Context.CLIPBOARD_SERVICE;

public class DetailPresenter implements DetailContract.Presenter{
    private DetailContract.View view;
    private Context context;
    private String idContent;
    private DbContentList item;
    private String textDetail = " ";
    private int allPages;
    private String currentPage = "1";


    DetailPresenter(Context context, DetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        List<DbContentList> queryList = DataSupport.where("idContent=?", idContent).find(DbContentList.class);
        item = queryList.get(0);
        boolean isContentCached = item.getText() != null;
        if (isContentCached) {
            textDetail = item.getText();
            currentPage = item.getCurrentPage();
            allPages = Integer.valueOf(item.getMaxPage());
            view.setData(item.getTitle(), item.getImg(), textDetail);
        } else {
            view.showLoading();
            loadPost("1");
            view.stopLoading();
        }
    }

    @Override
    public void loadPost(String page) {
        boolean isNetworkAvailable = NetworkState.networkConnected(context);
        if (!isNetworkAvailable){
            textDetail = "网络异常";
            view.showLinkError();
            view.setData(item.getTitle(),item.getImg(),textDetail);
            return;
        }

        HttpUtil.sendOkHttpRequest(API.getStoryContentUrl(page, idContent), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                textDetail = "Error";
                view.showError();
                //这里需要一个错误标题和错误图片，出现错误的文字
                view.setData(item.getTitle(),item.getImg(),textDetail);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseText = response.body().string();
                    DetailResult detailResult = Utility.handleStoryDetailData(responseText);
                    if (detailResult == null) {
                        view.showError();
                        return;
                    }
                    allPages = detailResult.getDetailBody().getAllPages();
                    currentPage = detailResult.getDetailBody().getCurrentPage();
                    String queryText = detailResult.getDetailBody().getText();
                    StringBuilder textBuilder = new StringBuilder(textDetail);
                    textDetail = textBuilder.append(queryText).toString();

                    DbContentList updateItem = new DbContentList();
                    updateItem.setCurrentPage(currentPage);
                    updateItem.setMaxPage(String.valueOf(allPages));
                    updateItem.setText(textDetail);
                    updateItem.updateAll("idContent=?", idContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    textDetail = "Parse Error";
                    view.showError();
                    view.setData(item.getTitle(), item.getImg(), textDetail);
                }
                view.setData(item.getTitle(), item.getImg(), textDetail);
            }
        });
    }

    @Override
    public void loadMore() {
        int nowPage = Integer.valueOf(currentPage);
        if (nowPage < allPages) {
            loadPost(String.valueOf(++nowPage));
        } else {
            view.showMaxPage();
        }
    }

    @Override
    public void setIdContent(String idContent) {
        this.idContent = idContent;
    }

    @Override
    public void addToOrDeleteFromBookmarks() {
        DbContentList updateItem = new DbContentList();
        if (item.getIsBookmarked().equals("1")) {
            updateItem.setIsBookmarked("0");
            updateItem.updateAll("idContent = ?", idContent);

            view.showDeletedFromBookmarks();
        } else {
            updateItem.setIsBookmarked("1");
            updateItem.updateAll("idContent = ?", idContent);

            view.showAddedToBookmarks();
        }
    }

    @Override
    public void copyText() {
        if (item.getText().equals("")) {
            view.showCopyTextError();
        } else {
            ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData;
            clipData = ClipData.newPlainText("text", item.getText());
            if (manager != null) {
                manager.setPrimaryClip(clipData);
            }
            view.showTextCopied();
        }
    }

    @Override
    public boolean queryIfIsBookmarked() {
        return item.getIsBookmarked().equals("1");
    }
}
