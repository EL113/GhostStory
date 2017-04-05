package com.example.ghoststory.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;

import com.example.ghoststory.bean.DetailBody;
import com.example.ghoststory.bean.DetailResult;
import com.example.ghoststory.db.DbContentList;
import com.example.ghoststory.util.API;
import com.example.ghoststory.util.HttpUtil;
import com.example.ghoststory.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CacheService extends Service {
    private String now;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.ghoststory.recommendations");
        filter.addAction("com.example.ghoststory.storylist");
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new LocalReceiver(), filter);
    }

    public CacheService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            API api = new API();
            getNow();
            String type = intent.getStringExtra("TYPE");
            List<DbContentList> list = DataSupport.where("typeName=?", type).find(DbContentList.class);

            for (DbContentList item : list) {
                final String idContent = item.getIdContent();
                String url = api.STORY_CONTENT + "id=" + idContent + api.STORY_CONTENT_API_ID + now + api.API_SIGN;
                HttpUtil.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        DetailResult detailResult = Utility.handleStoryDetailData(responseText);
                        String text = detailResult.getDetailBody().getText();
                        DbContentList item = new DbContentList();
                        item.setText(text);
                        item.updateAll("idContent=?", idContent);
                    }
                });
            }
        }
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
}
