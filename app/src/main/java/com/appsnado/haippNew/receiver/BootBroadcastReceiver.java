package com.appsnado.haippNew.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.appsnado.haippNew.Applocakpacakges.base.AppConstants;
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager;
import com.appsnado.haippNew.Applocakpacakges.services.LoadAppListService;
import com.appsnado.haippNew.Applocakpacakges.services.LockService;
import com.appsnado.haippNew.utils.LogUtil;
import com.appsnado.haippNew.utils.SpUtil;


public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        LogUtil.i("Boot service....");
        //TODO: pie compatable done
       // context.startService(new Intent(context, LoadAppListService.class));
        BackgroundManager.getInstance().init(context).startService(LoadAppListService.class);
        if (SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE, false)) {
            BackgroundManager.getInstance().init(context).startService(LockService.class);
            BackgroundManager.getInstance().init(context).startAlarmManager();
        }
    }
}
