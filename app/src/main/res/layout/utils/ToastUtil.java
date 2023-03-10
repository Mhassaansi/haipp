package com.appsnado.haipp.Applocakpacakges.utils;

import android.widget.Toast;

import com.appsnado.haipp.Applocakpacakges.LockApplication;

/**
 * Created by xian on 2017/2/17.
 */

public class ToastUtil {
    @Nullable
    private static Toast mToast = null;

    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(LockApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showLoginToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(LockApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
