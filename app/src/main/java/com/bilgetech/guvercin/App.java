package com.bilgetech.guvercin;

import android.app.Application;

import com.bilgetech.guvercin.network.Api;
import com.bilgetech.guvercin.network.ApiGenerator;

public class App extends Application {
    private static App instance;
    private static Api api;
    private static String lastUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public static Api getApi() {
        String url = Prefs.get().getUrl();

        if (api == null || !lastUrl.equals(url)) {
            api = ApiGenerator.create(url);
            lastUrl = url;
        }
        return api;
    }
}
