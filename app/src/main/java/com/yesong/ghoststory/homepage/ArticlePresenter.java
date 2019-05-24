package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yesong.ghoststory.bean.AuditResponse;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;
import com.yesong.ghoststory.util.APIUtil;
import com.yesong.ghoststory.util.HttpUtil;
import com.yesong.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticlePresenter implements StoryListContract.Presenter {
    private Context context;
    private StoryListContract.View view;
    private List<DbContentList> list;
    private int page;
    private String authorId;

    ArticlePresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        list = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        authorId = pref.getString("authorId", null);
    }

    @Override
    public void start() {
        page = 1;
        loadResults(true);
    }

    private void loadResults(boolean refresh) {
        if (refresh) {
            list.clear();
        }

        view.showLoading();
        List<DbContentList> queryList = new ArrayList<>();
        if (authorId != null) {
            queryList = getDbList();
        }

        updateAuditStatus(queryList);

        view.stopLoading();
    }

    private void updateAuditStatus(final List<DbContentList> contentList){
        StringBuilder ids = new StringBuilder();
        for (DbContentList contentItem: contentList){
            ids.append(contentItem.getIdContent()).append(",");
        }
        String idStr = ids.toString();
        String requestIds = idStr.substring(0, idStr.length() - 1);
        RequestBody requestBody = new FormBody.Builder()
                .add("ids", requestIds).build();

        HttpUtil.post(APIUtil.auditResult(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.showError("请求错误");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                AuditResponse auditResponse = Utility.resolveResponse(responseText, AuditResponse.class);
                if (auditResponse == null || auditResponse.getCode() != 0) {
                    view.showError("解析错误");
                    return;
                }

                List<String> auditResult = auditResponse.getResultList();
                for (int i = 0; i < auditResult.size(); i++) {
                    DbContentList contentItem = contentList.get(i);
                    contentItem.setAuditStatus(auditResult.get(i));
                    contentItem.save();
                }

                list.addAll(contentList);
                view.showResults(list);
            }
        });
    }

    private List<DbContentList> getDbList() {
        int storyIndex = (page - 1) * 10;
        return DataSupport.where("authorId = ?", authorId)
                .limit(10)
                .offset(storyIndex)
                .find(DbContentList.class);
    }

    @Override
    public void loadMore() {
        ++page;
        loadResults(false);
    }

    @Override
    public void startReading(int position) {
        DbContentList item = list.get(position);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", item.getIdContent());
        intent.putExtra("typeName", item.getTypeName());
        context.startActivity(intent);
    }
}
