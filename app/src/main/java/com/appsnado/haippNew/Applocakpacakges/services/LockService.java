package com.appsnado.haippNew.Applocakpacakges.services;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.appsnado.haippNew.Activities.GestureServicesact;
import com.appsnado.haippNew.Applocakpacakges.LockApplication;

import com.appsnado.haippNew.Applocakpacakges.Model;
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants;
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haippNew.Applocakpacakges.receiver.LockRestarterBroadcastReceiver;
import com.appsnado.haippNew.Applocakpacakges.utils.NotificationUtil;
import com.appsnado.haippNew.Applocakpacakges.utils.SpUtil;
import com.appsnado.haippNew.DevAdminReceiver;
import com.appsnado.haippNew.data.SharedPreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import static com.appsnado.haippNew.Firebase.FirebaseDataReceiver.firebasestatus;


public class LockService extends IntentService implements LocationListener, GoogleApiClient.ConnectionCallbacks,  NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,lanListener {

    public static final String UNLOCK_ACTION = "UNLOCK_ACTION";
    public static final String LOCK_SERVICE_LASTTIME = "LOCK_SERVICE_LASTTIME";
    public static final String LOCK_SERVICE_LASTAPP = "LOCK_SERVICE_LASTAPP";
    private static final String TAG = "LockService";
    public static boolean isActionLock = false;
    public boolean threadIsTerminate = false;
    public static boolean settinglockpermission = false;

    @Nullable
    public String savePkgName;
    UsageStatsManager sUsageStatsManager;
    Timer timer = new Timer();
    //private boolean isLockTypeAccessibility;
    private long lastUnlockTimeSeconds = 0;
    private String lastUnlockPackageName = "";
    private boolean lockState;
    private ServiceReceiver mServiceReceiver;
    private CommLockInfoManager mLockInfoManager;

    private LocationRequest mLocationRequest;
    private   GoogleApiClient googleApiClient;
    @Nullable
    private ActivityManager activityManager;
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminRcvr;

    private  Location LastLocation;
    private int counter = 0;

    private double latitude, longitude;
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private  boolean firecall = false;
    public LockService() {
        super("LockService");
    }
    private SharedPreferenceManager preferenceManager;

    private SharedPreferences prefs;
    private TimerCounter tc;
    public static List<CommLockInfo> listdata = new ArrayList<CommLockInfo>();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Map<String, ArrayList<Model>> order = new HashMap();
    private  ArrayList<Model> selectedItems = null;
    private lanListener listener;
    private ValueEventListener mListener;

    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        super.onCreate();
       // if(SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
       // lockState = true;
        mLockInfoManager = new CommLockInfoManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        preferenceManager =new  SharedPreferenceManager(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(UNLOCK_ACTION);
        registerReceiver(mServiceReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        }

        threadIsTerminate = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createNotification(this, "Haipp Lock", "Haipp Lock running in background");
        }


        tc = new TimerCounter();
        tc.startTimer(counter,this,this);

        buildGoogleApiClient();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        runForever();
    }
    private void datacall() {

        mDPM = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
        mDPM.removeActiveAdmin(mDeviceAdminRcvr);

    }
    private void calfirebase() {
        Toast.makeText(this,"Apps Permission",Toast.LENGTH_LONG).show();
            listdata = getArrayList("app");
            firebasestatus = false;
            String name = preferenceManager.gettype();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(name != "parent") {
                String usern = preferenceManager.getUser().getUserEmail();
                String[] separated = usern.split("@");
                String username = separated[0]; // this will contain "Fruit"
                String deviceTitle= preferenceManager.getdevicestitle();
                String devicesid =   preferenceManager.getdevicesid();
                String userid = preferenceManager.getUser().getUserID();
                double ui  = Double.parseDouble(userid);
                int i = (int) ui;
                double  vi = Double.parseDouble(devicesid);
                int v = (int) vi;
                DatabaseReference myRef1 = database.getReference("Devicesdata");
                DatabaseReference myRefoneparent = myRef1.child("Parent_"+username+"_"+i);
                DatabaseReference Childdevices = myRefoneparent.child("Childdevices");
                DatabaseReference myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v);
                DatabaseReference myRefoneapp = myRefone.child("Appsdata");
                applistner(myRefoneapp);
            }


    }

    private void callref() {

    }

    private void applistner(DatabaseReference myRef) {
        listdata = getArrayList("app");
        mListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check for null
                if (dataSnapshot.getValue() == null) {
                    Log.e("TAG", "User data is null!");
                    return;
                }
                        order = (Map<String, ArrayList<Model>>) dataSnapshot.getValue();
                        selectedItems = new ArrayList(order.get("Apps"));
                        Log.i("TAG", "onDataChange: " + selectedItems);
                        DataSnapshot dataSnapshot1;
                        dataSnapshot.getValue();

                        for (int i = 0; i < selectedItems.size(); i++) {
                            HashMap item = selectedItems.get(i);
                            for (int j = 0; j < listdata.size(); j++) {
                                if (item.get("pacakagename").toString().equals(listdata.get(j).getPackageName())) {
                                    final CommLockInfo lockInfo = listdata.get(j);
                                    changeItemLockStatus(item.get("isLocked").toString(), item.get("pacakagename").toString());
                                    break;
                                }
                            }
                        }

                      myRef.removeEventListener(mListener);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("TAG", "Failed to read user", error.toException());
            }
        });





    }

    public void changeItemLockStatus(@NonNull String checkBox, @NonNull String info) {
            mLockInfoManager = new CommLockInfoManager(this);
        if (checkBox.equals("true")) {
            //info.setLocked(true);
            mLockInfoManager.lockCommApplication(info);
        } else {
           // info.setLocked(false);
            mLockInfoManager.unlockCommApplication(info);
        }
        //notifyItemChanged(position);
    }
    public ArrayList<CommLockInfo> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<CommLockInfo>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean hasPermission() {
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void runForever() {

       // calfirebase();
        while (threadIsTerminate) {

            if (preferenceManager.getscreenlock() == true){
                ///preferenceManager.setscreenlock(false);
                mDPM = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
                mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
                boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                if (active) {
                    mDPM.lockNow();
                }
            }





                    if(preferenceManager.getJsonupdate() == true){
                        preferenceManager.setJsonupdate(false);
                        jsondatalistner();
                    }


            if (preferenceManager.getsmarkschlock() == true) {
                if (preferenceManager.getstarttime()) {
                    try {


                        Long currenttime = System.currentTimeMillis();
                        String totaltime = preferenceManager.gettimecount();
                        Long totaltimeview = Long.parseLong(totaltime);
                        if (totaltimeview > currenttime) {
                            Long time = Long.parseLong(totaltime) - currenttime;
                            ///startcounter(time);
                            //unlock()
                        } else {
                            preferenceManager.setstarttime(false);
                            stoptime(0);
                            //lock();

                        }
                    }catch (Exception e){

                    }

                }
            }


//                if(preferenceManager.getAppupdate() == true){
//                      preferenceManager.setAppupdate(false);
//                       calfirebase();
//                   }




//            if(firebasestatus) {
//                firebasestatus = false;
//                listdata = getArrayList("app");
//                String name = preferenceManager.gettype();
//                if(name != "parent") {
//                    if(!lockpacakname.equals("")) {
//
//                        for (int j = 0; j < listdata.size(); j++) {
//                            if (lockpacakname.equals(listdata.get(j).getPackageName())) {
//                                final CommLockInfo lockInfo = listdata.get(j);
//                                changeItemLockStatus(statuslock, lockInfo, j);
//                                lockpacakname = "";
//                                break;
//                            }
//                        }
//                     }else if(!lockphonepermissoin.equals("")){
//
//                           if(statuslock.equals("true")){
//                               preferenceManager.setscreenlock(true);
//
//
//
////Lock device
//                               DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//                               mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
//                               boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
//                               if (active) {
//                                   mDPM.lockNow();
//                               }
//
//                           }else {
//                               preferenceManager.setscreenlock(false);
//                           }
//                     }else if(!Installpermission.equals("")){
//
//                        if(statuslock.equals("true")){
//                            preferenceManager.setscreenlock(true);
//
//                            DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//                            mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
//                            boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
//
//
//                        }else {
//                            DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//                            mDeviceAdminRcvr = new ComponentName(this, DevAdminReceiver.class);
//                            mDPM.removeActiveAdmin(mDeviceAdminRcvr);
//                        }
//                    }
//
//                }
//            }


            if(!hasPermission()) {
                Toast.makeText(this,"Permissions Required",Toast.LENGTH_LONG).show();
                settinglockpermission  = true;
            }else {
                settinglockpermission  = false;
            }

            String packageName = getLauncherTopApp(LockService.this, activityManager);

            if (!preferenceManager.getstarttime()) {
                if (preferenceManager.gettype() != "parent"){
                    // preferenceManager.setsmartschlock(true);
                    if (lockState && !TextUtils.isEmpty(packageName) && !inWhiteList(packageName)) {
                        boolean isLockOffScreenTime = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);
                        boolean isLockOffScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
                        savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, "");

                        if (isLockOffScreenTime && !isLockOffScreen) {
                            long time = SpUtil.getInstance().getLong(AppConstants.LOCK_CURR_MILLISECONDS, 0);
                            long leaverTime = SpUtil.getInstance().getLong(AppConstants.LOCK_APART_MILLISECONDS, 0);
                            if (!TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        if (System.currentTimeMillis() - time > leaverTime) {
                                            mLockInfoManager.lockCommApplication(savePkgName);
                                        }
                                    }
                                }
                            }
                        }

                        if (isLockOffScreenTime && isLockOffScreen) {
                            long time = SpUtil.getInstance().getLong(AppConstants.LOCK_CURR_MILLISECONDS, 0);
                            long leaverTime = SpUtil.getInstance().getLong(AppConstants.LOCK_APART_MILLISECONDS, 0);
                            if (!TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        if (System.currentTimeMillis() - time > leaverTime) {
                                            mLockInfoManager.lockCommApplication(savePkgName);
                                        }
                                    }
                                }
                            }
                        }


                        if (!isLockOffScreenTime && isLockOffScreen && !TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName)) {
                            if (!savePkgName.equals(packageName)) {
                                isActionLock = false;
                                if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                    boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                    if (!isSetUnLock) {
                                        mLockInfoManager.lockCommApplication(savePkgName);
                                    }
                                }
                            } else {
                                isActionLock = true;
                            }
                        }
                        if (!isLockOffScreenTime && !isLockOffScreen && !TextUtils.isEmpty(savePkgName) && !TextUtils.isEmpty(packageName) && !savePkgName.equals(packageName)) {
                            if (getHomes().contains(packageName) || packageName.contains("launcher")) {
                                boolean isSetUnLock = mLockInfoManager.isSetUnLock(savePkgName);
                                if (!isSetUnLock) {
                                    mLockInfoManager.lockCommApplication(savePkgName);
                                }
                            }
                        }
                        if (mLockInfoManager.isLockedPackageName(packageName)) {
                            passwordLock(packageName);
                            continue;
                        }
                    }
                }
            }
            try {
                Thread.sleep(210);
            } catch (Exception ignore) {

            }
        }
    }

    private void stoptime(int time) {
        String name = preferenceManager.gettype();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(name != "parent") {
            String usern = preferenceManager.getUser().getUserEmail();
            String[] separated = usern.split("@");
            String username = separated[0]; // this will contain "Fruit"
            String deviceTitle= preferenceManager.getdevicestitle();
            String devicesid =   preferenceManager.getdevicesid();
            String userid = preferenceManager.getUser().getUserID();
            double ui  = Double.parseDouble(userid);
            int i = (int) ui;
            double  vi = Double.parseDouble(devicesid);
            int v = (int) vi;
            DatabaseReference myRef1 = database.getReference("Devicesdata");
            DatabaseReference myRefoneparent = myRef1.child("Parent_"+username+"_"+i);
            DatabaseReference Childdevices = myRefoneparent.child("Childdevices");
            DatabaseReference myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v);
            DatabaseReference myRefoneapp = myRefone.child("claimpoint");
            myRefoneapp.setValue(time);
        }


    }



    private void jsondatalistner() {
        String name = preferenceManager.gettype();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(name != "parent") {
            String usern = preferenceManager.getUser().getUserEmail();
            String[] separated = usern.split("@");
            String username = separated[0]; // this will contain "Fruit"
            String deviceTitle= preferenceManager.getdevicestitle();
            String devicesid =   preferenceManager.getdevicesid();
            String userid = preferenceManager.getUser().getUserID();
            double ui  = Double.parseDouble(userid);
            int i = (int) ui;
            double  vi = Double.parseDouble(devicesid);
            int v = (int) vi;
            DatabaseReference myRef1 = database.getReference("Devicesdata");
            DatabaseReference myRefoneparent = myRef1.child("Parent_"+username+"_"+i);
            DatabaseReference Childdevices = myRefoneparent.child("Childdevices");
            DatabaseReference myRefone = Childdevices.child("Child_"+deviceTitle +"_" +v);
            DatabaseReference myRefoneapp = myRefone.child("smartsch");
            appjsonviewer(myRefoneapp);
        }
    }

    private void appjsonviewer(DatabaseReference myRefoneapp) {
        mListener = myRefoneapp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check for null
                if (dataSnapshot.getValue() == null) {
                    Log.e("TAG", "User data is null!");
                    return;
                }

                String smartjson = dataSnapshot.getValue().toString();
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                editor.putString("smartmodule", smartjson);
                editor.apply();
                myRefoneapp.removeEventListener(mListener);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("TAG", "Failed to read user", error.toException());
            }
        });


    }


//    private void showDialog() {
//        // If you do not have access to view usage rights and the phone exists to view usage this interface
//        if (!LockUtil.isStatAccessPermissionSet(this) && LockUtil.isNoOption(this)) {
//            DialogPermission dialog = new DialogPermission(this);
//            dialog.show();
//            dialog.setOnClickListener(new DialogPermission.onClickListener() {
//                @Override
//                public void onClick() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = null;
//                        intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
//                    }
//                }
//            });
//        }
//
//    }


    private boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.APP_PACKAGE_NAME);
    }

    public String getLauncherTopApp(@NonNull Context context, @NonNull ActivityManager activityManager) {
        //isLockTypeAccessibility = SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    @NonNull
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    private void passwordLock(String packageName) {
        LockApplication.getInstance().clearAllActivity();
        Intent intent = new Intent(this, GestureServicesact.class);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_FINISH);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadIsTerminate = false;
        timer.cancel();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             NotificationUtil.cancelNotification(this);
        }

        //  //  lockState = true;
       lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
        if (lockState) {
            Intent intent = new Intent(this, LockRestarterBroadcastReceiver.class);
            intent.putExtra("type", "lockservice");
            sendBroadcast(intent);
        }
        unregisterReceiver(mServiceReceiver);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        threadIsTerminate = false;
        timer.cancel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.cancelNotification(this);
        }
        lockState = SpUtil.getInstance().getBoolean(AppConstants.LOCK_STATE);
       // lockState = true;
        if (lockState) {
            Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
            restartServiceTask.setPackage(getPackageName());
            PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1495, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            myAlarmService.set(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1500,
                    restartPendingIntent);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{


            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(15000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //

                return;
            }
            if(googleApiClient !=null)
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {

        LastLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMovieClicked(int count) {

    }

    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String action = intent.getAction();

         //   boolean isLockOffScreen = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN, false);
       //     boolean isLockOffScreenTime = SpUtil.getInstance().getBoolean(AppConstants.LOCK_AUTO_SCREEN_TIME, false);



            boolean isLockOffScreen = false;
            boolean isLockOffScreenTime = false;


            switch (action) {
                case UNLOCK_ACTION:
                    lastUnlockPackageName = intent.getStringExtra(LOCK_SERVICE_LASTAPP);
                    lastUnlockTimeSeconds = intent.getLongExtra(LOCK_SERVICE_LASTTIME, lastUnlockTimeSeconds);
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    SpUtil.getInstance().putLong(AppConstants.LOCK_CURR_MILLISECONDS, System.currentTimeMillis());

                    if (!isLockOffScreenTime && isLockOffScreen) {
                        String savePkgName = SpUtil.getInstance().getString(AppConstants.LOCK_LAST_LOAD_PKG_NAME, "");
                        if (!TextUtils.isEmpty(savePkgName)) {
                            if (isActionLock) {
                                mLockInfoManager.lockCommApplication(lastUnlockPackageName);
                            }
                        }
                    }
                    break;
            }
        }
    }
}
