package com.bilgetech.guvercin.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bilgetech.guvercin.App;

import java.io.IOException;

public class ConnectivityHelper {
    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return "There is a connectivity problem right now.";
        }
    }
}
