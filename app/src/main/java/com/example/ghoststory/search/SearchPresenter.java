package com.example.ghoststory.search;

import android.content.Context;
import android.content.Intent;

import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel hunt on 2017/4/1.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private Context context;
    private List<DbContentList> lists = new ArrayList<>();


    public SearchPresenter(Context context, SearchContract.View view) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
    }

    @Override
    public void loadResults(String queryText) {
        lists.clear();
        List<DbContentList> queryList = DataSupport.where("isBookmarked = ? and title like ?","1", "%"+queryText+"%").find(DbContentList.class);
        for (DbContentList item : queryList) {
            if (item != null) {
                lists.add(item);
            }
        }
        view.showResults(lists);
    }

    @Override
    public void startReading(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", lists.get(position).getIdContent());
        context.startActivity(intent);
    }

    @Override
    public void start() {

    }
}
