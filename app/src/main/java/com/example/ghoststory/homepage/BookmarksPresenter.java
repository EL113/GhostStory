package com.example.ghoststory.homepage;

import android.content.Context;
import android.content.Intent;

import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel hunt on 2017/3/27.
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {
    private Context context;
    private BookmarksContract.View view;
    private List<DbContentList> list;


    public BookmarksPresenter(Context context, BookmarksContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        list = new ArrayList<>();
    }

    @Override
    public void start() {
        loadResults();
    }

    @Override
    public void startReading(int position) {
        DbContentList item = list.get(position);
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", item.getIdContent());
        context.startActivity(intent);
    }

    @Override
    public void loadResults() {
        view.showLoading();

        list.clear();

        List<DbContentList> queryList = DataSupport.where("isBookmarked=?","1").find(DbContentList.class);
        //不能用赋值的方法把一个list的元素加入另一个元素中，只能通过如下遍历的方式，一个一个的加入
        for (DbContentList item : queryList) {
            list.add(item);
        }

        view.showResults(list);
        view.stopLoading();
    }

    @Override
    public void checkForFreshData() {
        loadResults();
    }
}
