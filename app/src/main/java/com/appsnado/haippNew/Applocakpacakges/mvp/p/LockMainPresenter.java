package com.appsnado.haippNew.Applocakpacakges.mvp.p;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.appsnado.haippNew.Applocakpacakges.base.AppConstants;
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haippNew.Applocakpacakges.mvp.contract.LockMainContract;
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.appsnado.haippNew.Applocakpacakges.base.AppConstants.commLockInfoslist;

/**
 * Created by xian on 2017/2/17.
 */

public class LockMainPresenter implements LockMainContract.Presenter {

    private LockMainContract.View mView;
    private PackageManager mPackageManager;
    private CommLockInfoManager mLockInfoManager;
    private Context mContext;
    private LoadAppInfo mLoadAppInfo;

    public LockMainPresenter(LockMainContract.View view, Context mContext) {
        mView = view;
        this.mContext = mContext;
        mPackageManager = mContext.getPackageManager();
        mLockInfoManager = new CommLockInfoManager(mContext);
    }

    /**
     * 加载所有app
     */
    @Override
    public void loadAppInfo(Context context) {
        mLoadAppInfo = new LoadAppInfo();
        mLoadAppInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void searchAppInfo(String search, ISearchResultListener listener) {
        new SearchInfoAsyncTask(listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, search);
    }


    @Override
    public void onDestroy() {
        if (mLoadAppInfo != null && mLoadAppInfo.getStatus() != AsyncTask.Status.FINISHED) {
            mLoadAppInfo.cancel(true);
        }
    }

    public interface ISearchResultListener {
        void onSearchResult(List<CommLockInfo> commLockInfos);
    }

    public void saveArrayList(List<CommLockInfo> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }
    private class LoadAppInfo extends AsyncTask<Void, String, List<CommLockInfo>> {

        @Override
        protected List<CommLockInfo> doInBackground(Void... params) {
            try {


            List<CommLockInfo> commLockInfos = mLockInfoManager.getAllCommLockInfos();
            Iterator<CommLockInfo> infoIterator = commLockInfos.iterator();
            commLockInfoslist = mLockInfoManager.getAllCommLockInfos();;
                saveArrayList(commLockInfoslist,"app");

            int favoriteNum = 0;

            while (infoIterator.hasNext()) {
                CommLockInfo info = infoIterator.next();
                try {
                    ApplicationInfo appInfo = mPackageManager.getApplicationInfo(info.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                    if (appInfo == null || mPackageManager.getApplicationIcon(appInfo) == null) {
                        infoIterator.remove();
                        continue;
                    } else {
                        info.setAppInfo(appInfo);
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            info.setSysApp(true);
                            info.setTopTitle("System Applications");
                        } else {
                            info.setSysApp(false);
                            info.setTopTitle("User Application");
                        }
                    }
                    if (info.isLocked()) {
                        favoriteNum++;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                          e.printStackTrace();
                          infoIterator.remove();
                }
            }
            SpUtil.getInstance().putInt(AppConstants.LOCK_FAVITER_NUM, favoriteNum);
            return commLockInfos;

            }catch (Exception e){
                e.getCause();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CommLockInfo> commLockInfos) {
            super.onPostExecute(commLockInfos);
            mView.loadAppInfoSuccess(commLockInfos);
        }
    }

    private class SearchInfoAsyncTask extends AsyncTask<String, Void, List<CommLockInfo>> {
        private ISearchResultListener mSearchResultListener;

        public SearchInfoAsyncTask(ISearchResultListener searchResultListener) {
            mSearchResultListener = searchResultListener;
        }

        @Override
        protected List<CommLockInfo> doInBackground(String... params) {
            List<CommLockInfo> commLockInfos = mLockInfoManager.queryBlurryList(params[0]);
            Iterator<CommLockInfo> infoIterator = commLockInfos.iterator();



            while (infoIterator.hasNext()) {
                CommLockInfo info = infoIterator.next();
                try {
                    ApplicationInfo appInfo = mPackageManager.getApplicationInfo(info.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);


                    if (appInfo == null || mPackageManager.getApplicationIcon(appInfo) == null) {
                        infoIterator.remove(); //将有错的app移除
                    } else {
                        info.setAppInfo(appInfo);
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            info.setSysApp(true);
                            info.setTopTitle("System Applications");
                        } else {
                            info.setSysApp(false);
                            info.setTopTitle("User Application");
                        }
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    infoIterator.remove();
                }
            }
            return commLockInfos;
        }

        @Override
        protected void onPostExecute(List<CommLockInfo> commLockInfos) {
            super.onPostExecute(commLockInfos);
            mSearchResultListener.onSearchResult(commLockInfos);
        }
    }
}
