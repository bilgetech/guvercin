package com.bilgetech.guvercin;

import com.bilgetech.guvercin.model.Message;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MessageQueue {
    private static final String KEY = MessageQueue.class.getSimpleName();
    private static MessageQueue instance;

    // Instance Fields
    public ArrayList<Message> messages;

    public MessageQueue addMessage(Message message) {
        getMessages().add(message);
        return this;
    }

    public ArrayList<Message> getMessages() {
        if(messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public MessageQueue clear() {
        getMessages().clear();
        return this;
    }

    public static MessageQueue get() {
        if (instance == null) {
            instance = MessageQueue.load();
        }

        if (instance == null) {
            instance = new MessageQueue();
        }

        return instance;
    }

    private static MessageQueue load() {
        return Paper.book().read(KEY);
    }

    public void save() {
        instance = this;
        Paper.book().write(KEY, this);
    }

    public void delete() {
        instance = new MessageQueue();
        Paper.book().delete(KEY);
    }
}
