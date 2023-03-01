package com.appsnado.haippNew.Firebase;

import android.graphics.drawable.Drawable;

public class AppInfo {

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPacakagename() {
        return pacakagename;
    }

    public void setPacakagename(String pacakagename) {
        this.pacakagename = pacakagename;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    private String appname;
    private String hide;
    private Drawable icon;
    private String pacakagename;
    private String launcher;

    public String getToltaltime() {
        return toltaltime;
    }

    public void setToltaltime(String toltaltime) {
        this.toltaltime = toltaltime;
    }

    private String toltaltime;
}
