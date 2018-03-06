package com.bilgetech.guvercin;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTaskRoot()) {
            Intent i = new Intent(this, SplashActivity.class);
            startActivity(i);
        }

        finish();
    }

    public static PendingIntent getPendingIntent() {
        Context context = App.getInstance();
        Intent intent = new Intent(context, NotificationActivity.class);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
