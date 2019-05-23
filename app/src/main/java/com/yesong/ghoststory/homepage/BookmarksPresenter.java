package com.yesong.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;

import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class BookmarksPresenter implements StoryListContract.Presenter {
    private Context context;
    private StoryListContract.View view;
    private List<DbContentList> list;
    private int page;

    BookmarksPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        list = new ArrayList<>();
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
        int storyIndex = (page - 1) * 10;
        List<DbContentList> queryList = DataSupport.where("isBookmarked = ?", "1")
                .limit(10)
                .offset(storyIndex)
                .find(DbContentList.class);
        list.addAll(queryList);
        view.showResults(list);
        view.stopLoading();
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
