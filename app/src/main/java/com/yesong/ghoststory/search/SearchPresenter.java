package com.yesong.ghoststory.search;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.yesong.ghoststory.adapter.RecommendationsAdapter;
import com.yesong.ghoststory.bean.ContentList;
import com.yesong.ghoststory.bean.RecommendResponse;
import com.yesong.ghoststory.db.DbContentList;
import com.yesong.ghoststory.detail.DetailActivity;
import com.yesong.ghoststory.util.APIUtil;
import com.yesong.ghoststory.util.HttpUtil;
import com.yesong.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View view;
    private Context context;
    private List<DbContentList> list = new ArrayList<>();

    SearchPresenter(Context context, SearchContract.View view) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
    }

    @Override
    public void loadResults(String queryText) {
        list.clear();
//        list = DataSupport.where("isBookmarked = ? and title like ?","1", "%"+queryText+"%").find(DbContentList.class);
        HttpUtil.get(APIUtil.storySearch(queryText, 0, 25), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    view.showError("请求失败");
                    return;
                }

                RecommendResponse recommendResponse = Utility.resolveResponse(response.body().string(), RecommendResponse.class);
                if (recommendResponse == null || recommendResponse.getCode() != 0 || recommendResponse.getResult() == null) {
                    view.showError("请求失败");
                    return;
                }

                String time = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                for (ContentList item : recommendResponse.getResult()) {
                    DbContentList content = new DbContentList();
                    content.setIdContent(item.getId());
                    content.setAuthor(item.getAuthor());
                    content.setTitle(item.getTitle());
                    content.setDesc(item.getBrief());
                    content.setTypeName(item.getType());
                    content.setCreateTime(time);
                    content.setItemType(RecommendationsAdapter.TYPE_NORMAL);
                    boolean exist = Utility.queryIfIDExists(item.getId(), item.getType());
                    if (!exist) {
                        content.save();
                    }
                    list.add(content);
                }
                view.showResults(list);
            }
        });
    }

    @Override
    public void startReading(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("idContent", list.get(position).getIdContent());
        intent.putExtra("typeName", list.get(position).getTypeName());
        context.startActivity(intent);
    }

    @Override
    public void start() {
    }
}
