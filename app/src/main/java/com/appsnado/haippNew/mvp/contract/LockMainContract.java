package com.appsnado.haippNew.mvp.contract;

import android.content.Context;


import com.appsnado.haippNew.Applocakpacakges.base.BasePresenter;
import com.appsnado.haippNew.Applocakpacakges.base.BaseView;
import com.appsnado.haippNew.model.CommLockInfo;
import com.appsnado.haippNew.mvp.p.LockMainPresenter;

import java.util.List;

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
