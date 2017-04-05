package com.example.ghoststory.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Daniel hunt on 2017/3/25.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(final String address,final okhttp3.Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(address).get().build();
                client.newCall(request).enqueue(callback);
            }
        }).start();
    }
}
