package com.appsnado.haipp.Applocakpacakges.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appsnado.haipp.Applocakpacakges.base.AppConstants;
import com.appsnado.haipp.Applocakpacakges.services.BackgroundManager;
import com.appsnado.haipp.Applocakpacakges.services.LoadAppListService;
import com.appsnado.haipp.Applocakpacakges.services.LockService;
import com.appsnado.haipp.Applocakpacakges.utils.LogUtil;
import com.appsnado.haipp.Applocakpacakges.utils.SpUtil;


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
