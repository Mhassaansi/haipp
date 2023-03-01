package com.appsnado.haippNew.Firebase;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


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




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


      //  Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();


    }
    private void generateExpireNotification(String title, String body, Intent intent, Context context) {

           /* Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" +
              R.raw.sound);*/

        // Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.sound);

    }





    //This is for sending data to local broadcast receivers that are ( registered in fragments )
    private void sendBroadcastMessage(String body, String notificationType, String requestId, String rideType) {

        if (body != null && notificationType != null) {

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
                    Intent intent = new Intent(ACTION_FIRE_BASE_BROADCAST);
                    intent.putExtra(NOTIFICATION_TYPE, notificationType);
                    intent.putExtra(TRIP_ID, requestId);
                    intent.putExtra(RIDE_TYPE, rideType);
                    sendBroadcast(intent);
                    break;


                }

            }
        }
        //This is for sending data to local broadcast receivers that are ( Registered in fragments )
    }


}