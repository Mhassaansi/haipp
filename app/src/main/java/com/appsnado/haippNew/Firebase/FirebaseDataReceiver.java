package com.appsnado.haippNew.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.appsnado.haippNew.Applocakpacakges.Model;
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haippNew.Applocakpacakges.services.BackgroundManager;
import com.appsnado.haippNew.Applocakpacakges.services.LockService;
import com.appsnado.haippNew.Applocakpacakges.services.lanListener;
import com.appsnado.haippNew.Applocakpacakges.utils.ToastUtil;
import com.appsnado.haippNew.DevAdminReceiver;
import com.appsnado.haippNew.MainActivity;
import com.appsnado.haippNew.R;
import com.appsnado.haippNew.Screenlock.MyBroadCastReciever;
import com.appsnado.haippNew.Smartschedule.SmartschedulModelJava;
import com.appsnado.haippNew.data.SharedPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public static boolean firebasestatus = false;
    public static String lockpacakname = "";
    public static String statuslock = "";
    public static String lockphonepermissoin = "";
    public static String Installpermission = "";


    ///
    public static List<CommLockInfo> listdata = new ArrayList<CommLockInfo>();

    public static final String ACTION_FIRE_BASE_BROADCAST = FirebaseMessagingService.class.getName() + "fcm";
    public static final String NOTIFICATION_TYPE_SEND_REQUEST = "1";
    public static final String NOTIFICATION_TYPE_ACCEPT_REQUEST = "2";
    public static final String NOTIFICATION_TYPE_CANCELLED_REQUEST = "3";
    public static final String NOTIFICATION_TYPE_SCHEDULE_START = "12";
    public static final String NOTIFICATION_TYPE_GENERAL = "11";
    public static final String NOTIFICATION_TYPE_DISCARD_REQUEST = "4";
    public static final String NOTIFICATION_TYPE_ARRIVAL = "5";
    public static final String NOTIFICATION_TYPE_LOADING = "6";
    public static final String NOTIFICATION_TYPE_START = "7";
    public static final String NOTIFICATION_TYPE_UNLOADING = "8";
    public static final String NOTIFICATION_TYPE_END = "9";
    /*
    Push Notifications Intent Keys
    */
    public static final String KEY_MESSAGE = "message";
    //public static final String KEY_FROM = "from";
    public static final String TRIP_ID = "trip_id";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String RIDE_TYPE = "type";
    String notificationType = "";

    private String pacakegename = "";
    private String boleankey = "";
    private String bodytxt = "";
    private String smartjson = "";
    private int keyint = 0;
    private String titlebody = "";
    private boolean lockkeystate = false;

    private SharedPreferenceManager preferenceManager ;

    private CommLockInfoManager mLockInfoManager;

    private  ArrayList<Model> selectedItems = null;
    private lanListener listener;
    private ValueEventListener mListener;

    private Map<String, ArrayList<Model>> order = new HashMap();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");
        mLockInfoManager = new CommLockInfoManager(context);
        preferenceManager = new SharedPreferenceManager(context);
        if(!BackgroundManager.getInstance().init(context).isServiceRunning(LockService.class)){
            BackgroundManager.getInstance().init(context).startService(LockService.class);
        }
        BackgroundManager.getInstance().init(context).startAlarmManager();

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                 Object value = intent.getExtras().get(key);
                    String actiono =  intent.getAction();

                if(key.equals("packagename")) {
                    keyint = 1;
                    pacakegename = value.toString();
                }else if(key.equals("Lockpermission")){
                    lockphonepermissoin = value.toString();
                    keyint = 2;
                    pacakegename = value.toString();
                }else if(key.equals("Installpermission")){
                    keyint = 3;
                    pacakegename = value.toString();
                } else if(key.equals("ischeck")){
                     statuslock = value.toString();
                     boleankey = value.toString();
                }else if(key.equals("data")){
                    ToastUtil.showToast("Notification Location");

                }else if(key.equals("bodymsg")){
                    bodytxt = value.toString();
                }else if(key.equals("updateapp")){
                    keyint = 4;
                }else if(key.equals("smartschedule")){
                    smartjson = value.toString();
                    keyint = 5;
                }else if(key.equals("ChildLogout")){
                    keyint = 6;
                }else if(key.equals("task")){
                    keyint = 7;
                }else if(key.equals("updatejson")){
                    keyint = 8;
                }else if(key.equals("taskstatus")){
                    keyint = 9;
                }else if(key.equals("Allappscheck")){
                    keyint = 10;
                }



//                dataBody.put(appnname, json)
//                dataBody.put("ischeck", ischeck)
//                dataBody.put("bodymsg", texts)
//                notificationBody.put("title", "Haipp")
//                notificationBody.put("body", "Haipp")
//                notificationBody.put("click_action", "Haipp")
//                notificationBody.put("notification_type", "Haipp")
//                notification.put("to", tokenlis)
//                notification.put("priority", "high")
//                notification.put("data", dataBody)
//                notification.put("notification", notificationBody)
//                mainActivity?.sendNotification(notification)

                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }

            if(keyint == 1)
            {
                 changeItemLockStatus(boleankey, pacakegename);
                 generateExpireNotification("Haipp", bodytxt,intent,context);

            }else if(keyint == 2){

                preferenceManager.setscreenlock(Boolean.parseBoolean(boleankey));
                MyBroadCastReciever mReceiver = new MyBroadCastReciever();
                IntentFilter screenStateFilter = new IntentFilter();
                screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
                screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
                getApplicationContext().registerReceiver(mReceiver, screenStateFilter);

                DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName  mDeviceAdminRcvr = new ComponentName(context, DevAdminReceiver.class);
                boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                if (active) {
                    mDPM.lockNow();
                }
                generateExpireNotification("Haipp", bodytxt,intent,context);

            }else if(keyint == 3){

                if(boleankey.equals("true")){
                    DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName  mDeviceAdminRcvr = new ComponentName(context, DevAdminReceiver.class);
                    boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                    generateExpireNotification("Haipp", bodytxt,intent,context);
                }else {
                    DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    ComponentName  mDeviceAdminRcvr = new ComponentName(context, DevAdminReceiver.class);
                    boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
                    if (active) {
                        mDPM.removeActiveAdmin(mDeviceAdminRcvr);
                    }
                    generateExpireNotification("Haipp", bodytxt,intent,context);
                }



            }else if(keyint == 4){
                 preferenceManager.setAppupdate(true);
                 generateExpireNotification("Haipp", bodytxt,intent,context);
                   calfirebase(context);
            }else if(keyint == 5){

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                editor.putString("smartmodule", smartjson);
                editor.apply();
                editor.commit();
                //preferenceManager.setAppupdate(Boolean.parseBoolean(boleankey));
                 if(smartjson.equals("")){
                     ArrayList<SmartschedulModelJava> taskArrayList =  new ArrayList<SmartschedulModelJava>();
                     taskArrayList = getArrayList("smartmodule", context);
                    // preferenceManager.setstarttime(false);
                     preferenceManager.setsmartschlock(true);
                     preferenceManager.setstarttime(true);
                 }else {
                     checkvaluew(context);
                 }

                generateExpireNotification("Haipp", bodytxt,intent,context);
            }else if(keyint == 6){
                preferenceManager.setchildlogout(Boolean.parseBoolean(boleankey));
                generateExpireNotification("Haipp", bodytxt,intent,context);
            }else if(keyint == 7) {
                generateExpireNotification("Haipp", bodytxt, intent, context);
                String lastWord = bodytxt.substring(bodytxt.lastIndexOf(" ") + 1);
                Log.i(TAG, "onReceive: " + lastWord);
              //  if(HomeFragment.not_det == null) {
                if (lastWord.equals("task")) {
                    preferenceManager.settasksheernav(true);
                    preferenceManager.setworksheernav(false);
                    //   }
                } else if (lastWord.equals("worksheet")) {
                    preferenceManager.settasksheernav(true);
                    preferenceManager.setworksheernav(false);
                }else if (lastWord.equals("Sheet")) {
                    preferenceManager.settasksheernav(true);
                    preferenceManager.setworksheernav(false);
                }

          //  }
            }else if(keyint == 8){
                    preferenceManager.setJsonupdate(true);
                 // generateExpireNotification("Haipp", "Update Smart Schedule",intent,context);
            }else if(keyint == 9){
                // generateExpireNotification("Haipp", bodytxt,intent,context);
            }else if(keyint == 10){
                  preferenceManager.setAppupdate(true);
                  generateExpireNotification("Haipp", "Update All apps",intent,context);
                // generateExpireNotification("Haipp", bodytxt,intent,context);
            }else {
                generateExpireNotification("Haipp", bodytxt,intent,context);
                String lastWord = bodytxt.substring(bodytxt.lastIndexOf(" ")+1);
                Log.i(TAG, "onReceive: "+lastWord);

                //if(HomeFragment.not_det == null) {
                    if (lastWord.equals("Sheet")) {
                        preferenceManager.settasksheernav(true);
                        preferenceManager.setworksheernav(false);
                    } else if (lastWord.equals("task")) {
                        preferenceManager.settasksheernav(true);
                        preferenceManager.setworksheernav(false);
                    }else if (lastWord.equals("worksheet")) {
                        preferenceManager.settasksheernav(false);
                        preferenceManager.setworksheernav(true);
                    }
             //  }
            }

        }


    }


    public void changeItemLockStatus(String boleankey, String pacakegename) {
        try{

          if (boleankey.equals("true")) {
              mLockInfoManager.lockCommApplication(pacakegename);
        } else {
              mLockInfoManager.unlockCommApplication(pacakegename);
        }


         }catch (Exception e){
            e.getCause();
        }
        //notifyItemChanged(position);
    }

    public void checkvaluew(Context context) {
        ArrayList<SmartschedulModelJava> taskArrayList =  new ArrayList<SmartschedulModelJava>();
        taskArrayList = getArrayList("smartmodule", context);
        if(taskArrayList != null) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
            int minute = mcurrentTime.get(Calendar.MINUTE);
            int dayOfWeek = mcurrentTime.get(Calendar.DAY_OF_WEEK);

            String daycheck = callday(dayOfWeek);

            for (int i = 0; i < taskArrayList.size(); i++) {
            //for (item in taskArrayList!!) {
                if (taskArrayList.get(i).getSs_day().equals(daycheck)) {
                    String[] separated = taskArrayList.get(i).getSs_start_time().split(":");
                    String h = separated[0]; // this will contain "Fruit"
                    String m = separated[1];
                    String[] eseparatedd = taskArrayList.get(i).getSs_end_time().split(":");
                    String eh = eseparatedd[0]; // this will contain "Fruit"
                    String em = eseparatedd[1];
                    //int value = Integer.parseInt(string);
//                    if(m == "23"){
//                        h = "24";
//                    }
                    if(em.equals("59")){
                        eh = "24";
                    }


                    if (hour >= Integer.parseInt(h)) {
                        if (Integer.parseInt(eh) > hour) {
                              lockkeystate = true;
                              //preferenceManager.setscreenlock(false);
                               preferenceManager.setsmartschlock(false);

                              Toast.makeText(context,"no lock",Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }

            }
                if(!lockkeystate) {
                    lockmobile(context);
                }

              //  break;

        }else{
           // preferenceManager.setscreenlock(false);
            preferenceManager.setsmartschlock(true);
        }
    }

    private void calfirebase(Context context) {
        Toast.makeText(context,"Apps Permission",Toast.LENGTH_LONG).show();
        listdata = getsavearrray("app",context);
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
            applistner(myRefoneapp,context);
        }


    }


    private void applistner(DatabaseReference myRef, Context context) {
        listdata = getsavearrray("app", context);
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


    public static String callday(int dayOfWeek) {
       String dayof;
        switch (dayOfWeek) {
            case 1:
                dayof ="Sun";
                break;
            case 2:
                dayof ="Monday";
                break;
            case 3:
                dayof ="Tuesday";
                break;
            case 4:
                dayof ="Wed";
                break;
            case 5:
                dayof ="Thursday";
                break;
            case 6:
                dayof ="Fri";
                break;
            case 7:
                dayof ="Sat";
                break;
            default:
                dayof ="";
        }
       // Log.d("**********", range[0] + " ~ " + range[1]);
        return dayof;
    }
//    private void callday(int dayOfWeek) {
//          if(dayOfWeek){
//            1 ->{ dayof ="Sun";
//                return  dayof
//            }
//            2 -> {dayof = "Monday";
//                return dayof
//            }
//            3 -> {dayof = "Tuesday";
//                return dayof
//            };
//            4 ->  {dayof ="Wed";
//                return dayof
//            };
//            5 -> {dayof = "Thursday";
//                return dayof
//            };
//            6 -> {dayof = "Fri";
//                return dayof
//            };
//            7 -> {dayof = "Sat";
//                return dayof;
//            };
//
//        }
//        return dayof;
//
//    }




    private void lockmobile(Context context) {
        //preferenceManager.setscreenlock(true);
        preferenceManager.setsmartschlock(true);
        Toast.makeText(context, "LocK OFF", Toast.LENGTH_LONG).show();


//        MyBroadCastReciever mReceiver = new MyBroadCastReciever();
//        IntentFilter screenStateFilter = new IntentFilter();
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        getApplicationContext().registerReceiver(mReceiver, screenStateFilter);
//
//
//
//        Toast.makeText(context, "LocK ON", Toast.LENGTH_LONG).show();
//        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        ComponentName  mDeviceAdminRcvr = new ComponentName(context, DevAdminReceiver.class);
//        boolean active = mDPM.isAdminActive(mDeviceAdminRcvr);
//        if (active) {
//            mDPM.lockNow();
//        }










    }


    public ArrayList<CommLockInfo> getsavearrray(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<CommLockInfo>>() {}.getType();
        return gson.fromJson(json, type);
    }




    public ArrayList<SmartschedulModelJava> getArrayList(String key,Context context){
        try {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<SmartschedulModelJava>>() {}.getType();
        return gson.fromJson(json, type);

         }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }




    private void generateExpireNotification(String title, String body, Intent intent,Context context) {
           /* Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" +
              R.raw.sound);*/

       // Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.sound);
        long[] VIBRATE_PATTERN = {0, 500};
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // Timber.d("NotificationType" + notificationType);
        switch (notificationType) {
            //first step for service provider when driver request for service then get driver request
            case NOTIFICATION_TYPE_SEND_REQUEST:
            case NOTIFICATION_TYPE_ACCEPT_REQUEST:
            case NOTIFICATION_TYPE_ARRIVAL:
            case NOTIFICATION_TYPE_LOADING:
            case NOTIFICATION_TYPE_START:
            case NOTIFICATION_TYPE_UNLOADING:
            case NOTIFICATION_TYPE_CANCELLED_REQUEST:
            case NOTIFICATION_TYPE_DISCARD_REQUEST:
            case NOTIFICATION_TYPE_SCHEDULE_START:
            case NOTIFICATION_TYPE_END: {
//                IS_GET_DRIVER_REQUEST_ALREADY_HIT = false;
                intent.putExtra(KEY_MESSAGE, body);
//              intent.putExtra(KEY_FROM, FROM_PUSH_NOTIFICATION);

                break;
            }
        }

        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.default_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(VIBRATE_PATTERN)
                .setPriority(IMPORTANCE_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getResources().getString(R.string.default_channel_id),
                    "KarveChannelName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(VIBRATE_PATTERN);
            channel.enableVibration(true);
            //  channel.setSound(NOTIFICATION_SOUND_URI, att);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.cancelAll();
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }






    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void show_Notification() {

    }
}
