package com.appsnado.haipp.Applocakpacakges.mvp.contract;

import android.content.Context;

import com.appsnado.haipp.Applocakpacakges.base.BasePresenter;
import com.appsnado.haipp.Applocakpacakges.base.BaseView;
import com.appsnado.haipp.Applocakpacakges.model.CommLockInfo;

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
