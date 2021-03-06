package com.bilgetech.guvercin;

import android.app.Application;

import com.bilgetech.guvercin.network.Api;
import com.bilgetech.guvercin.network.ApiGenerator;
import com.squareup.otto.Bus;

import io.paperdb.Paper;

public class App extends Application {
    private static App instance;
    private static Api api;
    private static String lastUrl;
    private Bus bus = new Bus();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Paper.init(this);

        if(Prefs.get().isActive()) {
            SmsService.start();
        }
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

    public Bus getBus() {
        return bus;
    }
}
