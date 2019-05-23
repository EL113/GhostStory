package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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

        list.addAll(queryList);
        view.showResults(list);
        view.stopLoading();
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
