package com.appsnado.haippNew.Applocakpacakges;

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
    String pacakagename;
    private String launcher;
}


