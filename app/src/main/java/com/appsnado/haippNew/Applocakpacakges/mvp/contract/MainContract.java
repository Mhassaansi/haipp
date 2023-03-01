package com.appsnado.haippNew.Applocakpacakges.mvp.contract;

import android.content.Context;

import com.appsnado.haippNew.Applocakpacakges.base.BasePresenter;
import com.appsnado.haippNew.Applocakpacakges.base.BaseView;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context, boolean isSort);

        void loadLockAppInfo(Context context);

        void onDestroy();
    }
}
