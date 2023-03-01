package com.appsnado.haipp.Applocakpacakges.mvp.contract;

import android.content.Context;

import com.appsnado.haipp.Applocakpacakges.base.BasePresenter;
import com.appsnado.haipp.Applocakpacakges.base.BaseView;
import com.appsnado.haipp.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haipp.Applocakpacakges.mvp.p.LockMainPresenter;

/**
 * Created by xian on 2017/2/17.
 */

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
