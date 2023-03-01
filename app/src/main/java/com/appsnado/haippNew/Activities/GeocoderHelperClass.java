package com.appsnado.haippNew.Activities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocoderHelperClass {
    private Handler mHandler;
    private int mMaxResults = 1;
    private SetResult mSetResultInterface;
    private Context mContext;

    public GeocoderHelperClass(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        mContext = context;
    }

    public GeocoderHelperClass setMaxResults(int maxResult) {
        mMaxResults = maxResult;
        return this;
    }

    public void execute(String location) {
        execute((Object[]) new String[] { location });
    }

    public void execute(double lat, double lon) {
        execute((Object[]) new Double[] { lat, lon });
    }

    private void execute(final Object... objects) {
        try {
            if (objects != null && Geocoder.isPresent()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Address> list = new ArrayList<Address>();
                        try {
                            if (objects.length >= 2 && objects[0] instanceof Double) {
                                double lat = (Double) objects[0];
                                double lon = (Double) objects[1];
                                list = new Geocoder(mContext, Locale.UK).getFromLocation(lat, lon, mMaxResults);
                            } else if (objects.length >= 1 && objects[0] instanceof String) {
                                String location = (String) objects[0];
                                list = new Geocoder(mContext, Locale.UK).getFromLocationName(location, mMaxResults);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            final List<Address> result = list;
                            if (result != null && result.size() > 0) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (mSetResultInterface != null)
                                            mSetResultInterface.onGetResult(result);
                                    }
                                });
                            }

                        }
                    }
                }, "GeoCoderRunner").start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GeocoderHelperClass setResultInterface(SetResult result) {
        mSetResultInterface = result;
        return this;
    }

    public interface SetResult {
        public abstract void onGetResult(List<Address> list);
    }

}