package com.bilgetech.guvercin.model;

import android.telephony.SmsMessage;

import com.bilgetech.guvercin.Prefs;

/**
 * Created by safa on 5.03.2018.
 */

public class Message {
    private String to;
    private String from;
    private String body;
    private long sentAt;

    public Message() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }

    public static Message fromSms(SmsMessage sms) {
        Message message = new Message();
        message.setFrom(sms.getOriginatingAddress());
        message.setBody(sms.getMessageBody());
        message.setSentAt(sms.getTimestampMillis());
        message.setTo(Prefs.get().getPhone());
        return message;
    }
}
