package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yesong.ghoststory.bean.AuditResponse;
import com.yesong.ghoststory.bean.ContentList;
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
import okhttp3.Response;

public class ArticlePresenter implements StoryListContract.Presenter {
    private Context context;
    private StoryListContract.View view;
    private List<DbContentList> list;
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
        loadResults(true);
    }

    private void loadResults(boolean refresh) {
        if (refresh) {
            list.clear();
        }

        view.showLoading();
        List<DbContentList> queryList = getDbList();

        updateAuditStatus(queryList);

        view.stopLoading();
    }

    private void updateAuditStatus(final List<DbContentList> contentList){
        StringBuilder ids = new StringBuilder();
        int index = 0;
        for (DbContentList contentItem: contentList){
            ids.append(contentItem.getIdContent());
            if (index != contentList.size() - 1) {
                ids.append(",");
            }
        }

        HttpUtil.get(APIUtil.auditResult(ids.toString()), new Callback() {
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

                //修改状态
                List<ContentList> auditResult = auditResponse.getResult();
                for (int i = 0; i < contentList.size(); i++) {
                    DbContentList dbContentItem = contentList.get(i);
                    for (ContentList contentItem: auditResult) {
                        if (dbContentItem.getIdContent().equals(contentItem.getId())){
                            dbContentItem.setAuditStatus(contentItem.getStatus());
                            dbContentItem.save();
                            list.add(dbContentItem);
                        }
                    }
                }
                view.showResults(list);
            }
        });
    }

    //数据库中文章作者id号的文章属于文集中的内容
    private List<DbContentList> getDbList() {
        if (authorId != null){
            return DataSupport.where("authorId=?", authorId)
                    .find(DbContentList.class);
        }
        return new ArrayList<>();
    }

    @Override
    public void loadMore() {
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
