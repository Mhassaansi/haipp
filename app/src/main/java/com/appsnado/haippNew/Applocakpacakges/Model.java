package com.appsnado.haippNew.Applocakpacakges;

import android.content.pm.ApplicationInfo;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class Model extends HashMap {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(@Nullable String packageName) {
        this.packageName = packageName;
    }

    @Nullable
    public String getAppName() {
        return appName;
    }

    public void setAppName(@Nullable String appName) {
        this.appName = appName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isFaviterApp() {
        return isFaviterApp;
    }

    public void setFaviterApp(boolean faviterApp) {
        isFaviterApp = faviterApp;
    }

    @Nullable
    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(@Nullable ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public boolean isSysApp() {
        return isSysApp;
    }

    public void setSysApp(boolean sysApp) {
        isSysApp = sysApp;
    }

    @Nullable
    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(@Nullable String topTitle) {
        this.topTitle = topTitle;
    }

    public boolean isSetUnLock() {
        return isSetUnLock;
    }

    public void setSetUnLock(boolean setUnLock) {
        isSetUnLock = setUnLock;
    }

    @Nullable
    private String packageName;
    @Nullable
    private String appName;
    private boolean isLocked;
    private boolean isFaviterApp;
    @Nullable
    private ApplicationInfo appInfo;
    private boolean isSysApp;
    @Nullable
    private String topTitle;
    private boolean isSetUnLock;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String icon;

}

