package com.appsnado.haippNew;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

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

        String title = "";
        String body = "";
        String notificationType = "";
        String requestId = "";
        String sound = "";
        String rideType = "";

    }


    private void generateExpireNotification(String title, String body, String tripId, String notificationType, String sound, String rideType) {

           /* Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" +
              R.raw.sound);*/

        Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" );
        long[] VIBRATE_PATTERN = {0, 500};

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                intent.putExtra(TRIP_ID, tripId);
                intent.putExtra(NOTIFICATION_TYPE, notificationType);
                intent.putExtra(RIDE_TYPE, rideType);
                break;
            }
        }


        // running app
 /*       PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.notification_channel_name),
                    "My Notifications", NotificationManager.IMPORTANCE_MAX);// Configure the notification channel.

            notificationChannel.setDescription("Channel description");

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            notificationChannel.setSound(NOTIFICATION_SOUND_URI, att);

            notificationChannel.enableLights(true);

            notificationChannel.setLightColor(Color.RED);

            notificationChannel.setVibrationPattern(new long[0]);

            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);

        }


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, getResources().getString(R.string.notification_channel_name));

        notificationBuilder.setSmallIcon(R.drawable.notification_icon_push)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setPriority(IMPORTANCE_HIGH)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(NOTIFICATION_SOUND_URI)
                .setSmallIcon(R.drawable.notification_icon_push)
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);


//        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());

        if (notificationManager != null) {
            notificationManager.cancelAll();
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        }

*/


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle(title)
                .setContentText(body)
                .setSound(NOTIFICATION_SOUND_URI)
                .setVibrate(VIBRATE_PATTERN)
                .setPriority(IMPORTANCE_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_channel_id),
                    "Haipp", NotificationManager.IMPORTANCE_DEFAULT);

            //channel.setSound(NOTIFICATION_SOUND_URI, att);
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