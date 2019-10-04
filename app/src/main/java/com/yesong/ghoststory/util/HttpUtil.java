package com.yesong.ghoststory.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    public static void post(final String address, final RequestBody requestBody, final okhttp3.Callback callback) {
        Log.d("yesongdh", "post: "+address);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void get(final String address, final okhttp3.Callback callback) {
        Log.d("yesongdh", "get: "+address);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(address).get().build();
        client.newCall(request).enqueue(callback);
    }
}
