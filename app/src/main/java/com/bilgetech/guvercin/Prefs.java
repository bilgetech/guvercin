package com.bilgetech.guvercin;

import io.paperdb.Paper;

public class Prefs {
    private static final String KEY = Prefs.class.getSimpleName();
    private static Prefs instance;

    // Instance Fields
    private boolean showNotif;
    private String url;
    private String phone;

    public boolean isShowNotif() {
        return showNotif;
    }

    public Prefs setShowNotif(boolean showNotif) {
        this.showNotif = showNotif;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Prefs setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Prefs setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public static Prefs get() {
        if(instance == null) {
            instance = Prefs.load();
        }

        if(instance == null) {
            instance = new Prefs();
        }

        return instance;
    }

    private static Prefs load() {
        return Paper.book().read(KEY);
    }

    public void save() {
        instance = this;
        Paper.book().write(KEY, this);
    }

    public void delete() {
        instance = new Prefs();
        Paper.book().delete(KEY);
    }
}
