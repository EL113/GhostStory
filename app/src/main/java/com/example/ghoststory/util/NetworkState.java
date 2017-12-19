package com.example.ghoststory.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkState {
    public static boolean networkConnected(Context context){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                return info.isAvailable();
            }
        }
        return false;
    }

    // 检查WiFi是否连接
    // if wifi connect
    public static boolean wifiConnected(Context context){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null){
                if (info.getType() == ConnectivityManager.TYPE_WIFI)
                    return info.isAvailable();
            }
        }
        return false;
    }

    // 检查移动网络是否连接
    // if mobile data connect
    public static boolean mobileDataConnected(Context context){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null){
                if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        }
        return false;
    }
}
