package com.bilgetech.guvercin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import com.bilgetech.guvercin.model.ErrorResponse;
import com.bilgetech.guvercin.model.Message;
import com.bilgetech.guvercin.network.SimpleCallback;
import com.squareup.otto.Subscribe;

import retrofit2.Call;

public class SmsService extends Service {
    private static final String TAG = SmsService.class.getSimpleName();
    private static final String EXTRA_IS_FOREGROUND = "isForeground";
    SmsReceiver smsReceiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra(EXTRA_IS_FOREGROUND)) {
            throw new IllegalArgumentException("You must provide EXTRA_IS_FOREGROUND. " +
                    "Use SmsService.start() to start this service.");
        }

        boolean isForeground = intent.getBooleanExtra(EXTRA_IS_FOREGROUND, false);

        if (isForeground) {
            startForeground(NotificationHelper.SERVICE_NOTIF_ID, NotificationHelper.getNotification());
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        App.getInstance().getBus().register(this);
        registerSmsReceiver();
    }

    @Override
    public void onDestroy() {
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        App.getInstance().getBus().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onSmsReceived(SmsMessage sms) {
        final Message message = Message.fromSms(sms);
        MessageQueue.get().addMessage(message).save();
        App.getApi().sendMessages(MessageQueue.get().getMessages()).enqueue(new SimpleCallback<Object>() {
            @Override
            public void onSuccess(Object data) {
                MessageQueue.get().clear().save();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                MessageQueue.get().addMessage(message).save();
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                MessageQueue.get().addMessage(message).save();
            }
        });
    }

    private void registerSmsReceiver() {
        smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }

    private static void startOnForeground(Context ctx) {
        Intent intent = new Intent(ctx, SmsService.class);
        intent.putExtra(EXTRA_IS_FOREGROUND, true);
        ctx.startService(intent);
    }

    private static void startOnBackground(Context ctx) {
        Intent intent = new Intent(ctx, SmsService.class);
        intent.putExtra(EXTRA_IS_FOREGROUND, false);
        ctx.startService(intent);
    }

    public static void start() {
        SmsService.startOnForeground(App.getInstance());
    }

    public static void stop() {
        Context ctx = App.getInstance();
        Intent intent = new Intent(ctx, SmsService.class);
        ctx.stopService(intent);
    }
}
