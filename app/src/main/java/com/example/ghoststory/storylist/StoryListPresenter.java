package com.example.ghoststory.storylist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.ghoststory.R;
import com.example.ghoststory.bean.ContentList;
import com.example.ghoststory.bean.RequestResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.detail.DetailActivity;
import com.example.ghoststory.util.API;
import com.example.ghoststory.util.HttpUtil;
import com.example.ghoststory.util.NetworkState;
import com.example.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Daniel hunt on 2017/3/28.
 */

public class StoryListPresenter implements StoryListContract.Presenter{
    private Context context;
    private StoryListContract.View view;
    private String typeId;
    private int page;
    private String now;
    private List<DbContentList> dataList = new ArrayList<>();//请求的所有数据
    private DbContentList content;//请求的一条数据

    public StoryListPresenter(Context context, StoryListContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
    }

    /**
     * 用于请求数据
     * @param typeId 故事类型
     * @param page 请求的页码
     * @param clearing 是否刷新，刷新就会清除掉之前的所有数据
     */
    @Override
    public void loadPost(final String typeId, int page,final boolean clearing) {
        if (clearing) {
            view.startLoading();
        }
        //获取当前时间
        getNow();

        final String url = API.STORY_LIST + "page=" + page + API.API_ID + now + "&type=" + typeId + API.API_SIGN;
        if (NetworkState.networkConnected(context)) {
            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    view.showError();
                    view.stopLoading();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    //解析数据
                    RequestResult requestResult = Utility.handleStoryListData(responseText);
                    //如果要刷新，就清除之前数据
                    if (clearing) {
                        dataList.clear();
                    }
                    //解析出的数据为空，就报错
                    if (requestResult == null) {
                        view.stopLoading();
                        view.showError();
                        return;
                    }

                    List<ContentList> responseList = new ArrayList<ContentList>();
                    try {
                        responseList = requestResult.getResponseBody().getPagebean().getContentlist();
                    } catch (EmptyStackException e) {
                        e.printStackTrace();
                        view.stopLoading();
                        view.showError();
                    }

                    for (ContentList item : responseList) {
                        //先查看这条数据是否在本地数据库存在，如果存在就从本地数据库取出，如果不存在就添加进本地数据库
                        if (!queryIfIDExists(item.getId())) {
                            content = new DbContentList();
                            content.setIdContent(item.getId());
                            content.setTitle(item.getTitle());
                            content.setLink(item.getLink());
                            content.setDesc(item.getDesc());
                            content.setImg(item.getImg());
                            content.setIsBookmarked("0");
                            content.setTypeName(typeId);
                            content.setTime(Double.valueOf(now));
                            content.save();
                            dataList.add(content);
                        } else {
                            content = new DbContentList();
                            content = DataSupport.where("idContent=?", item.getId()).find(DbContentList.class).get(0);
                            dataList.add(content);
                        }
                    }
                    view.stopLoading();
                    view.showResults(dataList);
                }
            });
        }else {
            //如果请求数据失败
            //如果是下拉刷新，就清除掉之前数据，并数据库请求所有数据
            if (clearing) {
                dataList.clear();
                dataList = DataSupport.where("typeName=?", typeId).find(DbContentList.class);
            }
            //显示出错
            view.showError();
        }
    }
    //刷新动作
    @Override
    public void refresh() {
        start();
    }

    @Override
    public void start() {
        //随机加载某一页
        Random random = new Random();
        page = random.nextInt(50)+1;
        loadPost(typeId,page,true);
    }

    //进入详情页面时，要传递信息过去
    @Override
    public void startReading(int position) {
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("idContent", dataList.get(position).getIdContent())
                .putExtra("title", dataList.get(position).getTitle())
                .putExtra("image", dataList.get(position).getImg())
                .putExtra("link", dataList.get(position).getLink()));
    }

    //加载下一页
    @Override
    public void loadMore() {
        loadPost(typeId, ++page, false);
    }

    public void getNow() {
        Calendar c = Calendar.getInstance();
        String second =String.valueOf(c.get(Calendar.SECOND)) ;
        int secondNumber=c.get(Calendar.SECOND);
        if (secondNumber >= 0 && secondNumber < 10) {
            second = "0" + second;
        }
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH)+1);
        int monthNumber = c.get(Calendar.MONTH)+1;
        if (monthNumber >= 0 && monthNumber < 10) {
            month = "0" + month;
        }
        String date = String.valueOf(c.get(Calendar.DATE));
        int dateNumber=c.get(Calendar.DATE);
        if (dateNumber >= 0 && dateNumber < 10) {
            date = "0" + date;
        }
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        int hourNumber=c.get(Calendar.HOUR_OF_DAY);
        if (hourNumber >= 0 && hourNumber < 10) {
            hour = "0"+hour;
        }
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        int minuteNumber=c.get(Calendar.MINUTE);
        if (minuteNumber >= 0 && minuteNumber < 10) {
            minute = "0" + minute;
        }

        now = year + month + date + hour + minute + second;
    }

    public boolean queryIfIDExists(String idContent) {
        List<DbContentList> dbContentList = DataSupport.where("idContent = ?", idContent).find(DbContentList.class);
        if (dbContentList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
