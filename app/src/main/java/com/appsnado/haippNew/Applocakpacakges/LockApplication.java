package com.appsnado.haippNew.Applocakpacakges;

import android.content.Intent;

import com.appsnado.haippNew.Activities.GestureServicesact;
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil;
import com.appsnado.haippNew.BuildConfig;
import com.appsnado.haippNew.Monitorapp.AppService;
import com.appsnado.haippNew.Monitorapp.data.AppItem;
import com.appsnado.haippNew.Monitorapp.data.DataManager;
import com.appsnado.haippNew.Monitorapp.db.DbHistoryExecutor;
import com.appsnado.haippNew.Monitorapp.db.DbIgnoreExecutor;
import com.appsnado.haippNew.Monitorapp.util.PreferenceManager;
import com.appsnado.haippNew.baseactivity.BaseActivity;


import org.litepal.LitePalApplication;
import java.util.ArrayList;
import java.util.List;




public class LockApplication extends LitePalApplication {

    private static LockApplication application;
    private static List<BaseActivity> activityList;





    public static LockApplication getInstance() {
        return application;
    }








    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //Crash reporter utility
      //  CrashReporter.initialize(this, getCacheDir().getPath());

        SpUtil.getInstance().init(application);
        activityList = new ArrayList<>();


        PreferenceManager.init(this);
        getApplicationContext().startService(new Intent(getApplicationContext(), AppService.class));
        DbIgnoreExecutor.init(getApplicationContext());
        DbHistoryExecutor.init(getApplicationContext());
        DataManager.init();
        addDefaultIgnoreAppsToDB();

    }

    private void addDefaultIgnoreAppsToDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> mDefaults = new ArrayList<>();
                mDefaults.add("com.android.settings");
                mDefaults.add(BuildConfig.APPLICATION_ID);
                for (String packageName : mDefaults) {
                       AppItem item = new AppItem();
                        item.mPackageName = packageName;
                        item.mEventTime = System.currentTimeMillis();
                        DbIgnoreExecutor.getInstance().insertItem(item);
                }
            }
        }).run();
    }
    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof GestureServicesact;
    }
}
