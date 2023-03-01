package com.appsnado.haippNew.Applocakpacakges.services;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appsnado.haippNew.Applocakpacakges.Model;
import com.appsnado.haippNew.Applocakpacakges.db.CommLockInfoManager;
import com.appsnado.haippNew.Applocakpacakges.model.CommLockInfo;
import com.appsnado.haippNew.DevAdminReceiver;
import com.appsnado.haippNew.data.SharedPreferenceManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.appsnado.haippNew.Applocakpacakges.services.LockService.settinglockpermission;
import static com.appsnado.haippNew.Firebase.FirebaseDataReceiver.firebasestatus;



public class  TimerCounter implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    int counter;
    private GoogleApiClient googleApiClient;
    protected LocationRequest mlocationRequest;
    private Location lastKnownLocation;
    double latitude = 0.0, longitude = 0.0;
    double previouslatitude = 0.0, previouslongitude = 0.0;
    Location LastLocation;
    private LatLng latLng;
    private Context context;
    private lanListener listener;
    public static Location mLastKnowLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback locationCallback;
    LocationRequest mLocationRequest;
    private ValueEventListener mListener;
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminRcvr;
    private CommLockInfoManager mLockInfoManager;




    public void Timer() {


    }

    private Timer timer;
    private TimerTask timerTask;
    private SharedPreferenceManager preferenceManager;

    public void startTimer(int counter, lanListener lan, LockService act) {
        this.counter = counter;
        this.context = act;
        this.listener = lan;
        // set a new timer
        timer = new Timer();
        preferenceManager =new  SharedPreferenceManager(act);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        // initialize the timer task's job
        initializeTimerTask();

        // schedule the timer, to wake up every 1 second
         timer.schedule(timerTask, 1000, 1000*30);











    }


    private void datacall() {

        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminRcvr = new ComponentName(context, DevAdminReceiver.class);
        mDPM.removeActiveAdmin(mDeviceAdminRcvr);

    }
    // it sets the timer to print the counter every x seconds
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

                     getDeviceLocation();
                     getGoogleLocation();
                     //calllocation();

                    //new LongOperation().execute();

                  //  Log.i("in timer", "in timer ++++ " + (counter++));

            }
        };
    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                sendAndRequestResponse();

            } catch (Exception e) {
                Log.e("LongOperation", "Interrupted", e);
                return "Interrupted";
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
    private void sendAndRequestResponse() {
         RequestQueue mRequestQueue;
         StringRequest mStringRequest;
       //  String url = "http://server.appsstaging.com:3001/api/device_coordinate/child/coordinate_child";
        String url = "http://server.appsstaging.com:3001/api/device_coordinate/child/coordinate_child?cc_device_id="+preferenceManager.getdevicesid()+"&cc_latitude="+latitude+"&cc_longitude="+longitude;
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", "onResponse: "+response);
              //  Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
           }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
               }
           })

        {
//            @Override
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("child_device_id", preferenceManager.getdevicesid());
//                params.put("child_latitude", "12.151613");
//                params.put("child_longitude", "45.55465");
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+ preferenceManager.getToken());
                return headers;
            }



        };

        mRequestQueue.add(mStringRequest);
    }


    private void calllocation() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
               // listener.onMovieClicked(counter++);
            }
        });

    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (preferenceManager.getstarttime()){
                       firebaseset();
                    }
                if (task.isSuccessful()) {
                    mLastKnowLocation = task.getResult();
                    if (mLastKnowLocation != null) {
                        // Constants.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), 17));
                    } else {


//                        LocationRequest locationRequest = LocationRequest.create();
//                        locationRequest.setInterval(60 * 1000);
//                        locationRequest.setFastestInterval(10 * 1000);
//                        locationRequest.setSmallestDisplacement(1000);


                        mlocationRequest = new LocationRequest();
                        mlocationRequest.setInterval(10 * 1000);
                        mlocationRequest.setFastestInterval(1 * 1000);
                        mlocationRequest.setSmallestDisplacement(1000);
                        mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult == null) {

                                    return;
                                } else {
                                       mLastKnowLocation = locationResult.getLastLocation();
                                       // //  // Constants.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), 17));
                                       mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                }
                            }

                        };
                        mFusedLocationProviderClient.requestLocationUpdates(mlocationRequest, locationCallback, null);

                    }
                }
            }
        });


    }

    private void firebaseset() {
        String name = preferenceManager.gettype();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(name != "parent") {
            try {


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

//            var currenttime = System.currentTimeMillis()
//            var totaltime = mainActivity!!.preferenceManager!!.gettimecount()
//            var totaltimeview = totaltime?.toLong()!!
//            if (totaltimeview!! > currenttime){
//                binding !!.childtime.visibility = View.VISIBLE
//                binding !!.timeswitch.isChecked = true
//                var time = totaltime !!.toLong() - currenttime
//            }
//
//
//
//
//
//
//
//            myRefoneapp.setValue(time);

            }catch (Exception e){

            }
        }
    }

    public void changeItemLockStatus(@NonNull String checkBox, @NonNull CommLockInfo info, int position) {
        if (checkBox.equals("true")) {
            info.setLocked(true);
            mLockInfoManager.lockCommApplication(info.getPackageName());
        } else {
            info.setLocked(false);
            mLockInfoManager.unlockCommApplication(info.getPackageName());
        }
        //notifyItemChanged(position);
    }
    private void getGoogleLocation() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        if (!googleApiClient.isConnected())
            googleApiClient.connect();
    }

    public void stopTimerTask() {
        // stop the timer, if it's not already null
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(60 * 1000);
        mlocationRequest.setFastestInterval(1000);
        mlocationRequest.setSmallestDisplacement(1000);

        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mlocationRequest);






        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        builder.build()
                );
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if(settinglockpermission) {
                            Toast.makeText(context,"Permissions Required",Toast.LENGTH_LONG).show();
                        }
                        updateLastLocation(true);
                        Log.e("Location Allowed", "SUCCESS");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        /*Log.e("Location Allowed", "RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(App.getContext(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }*/
                        //updateLastLocation(true);
                        Log.e("Location Allowed", "SUCCESS");
                        break;/*
                        break;*/
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("Location Allowed", "SETTINGS_CHANGE_UNAVAILABLE");
                        break;
                }
            }
        });
    }

    private void updateLastLocation(boolean move) {
        if (mLastKnowLocation != null) {
            if (move) {
                try {

                latitude = mLastKnowLocation.getLatitude();
                longitude = mLastKnowLocation.getLongitude();


                String lat = String.valueOf(latitude);
                String lon = String.valueOf(longitude);
                latitude = Double.parseDouble(lat.substring(0,lat.indexOf(".")+6));
                longitude = Double.parseDouble(lon.substring(0,lon.indexOf(".")+6));


                if(previouslatitude > 0.0){

                    Location selected_location = new Location("locationA");
                    selected_location.setLatitude(previouslatitude);
                    selected_location.setLongitude(previouslongitude);
                    Location near_locations = new Location("locationB");
                    near_locations.setLatitude(latitude);
                    near_locations.setLongitude(longitude);
                    double distance = selected_location.distanceTo(near_locations);
                    Log.i("TAG", "updateLastLocation: "+distance);
                    if(distance > 100.0){
                        new LongOperation().execute();
                    }
                    previouslatitude = latitude;
                    previouslongitude = longitude;

                 }else {

                    previouslatitude = latitude;
                    previouslongitude = longitude;

                }






                latLng = new LatLng(latitude, longitude);

               /// Toast.makeText(context, String.valueOf(latitude),Toast.LENGTH_SHORT).show();
                ///   sendDataToBGService(latitude,longitude);
                Geocoder gCoder = new Geocoder(context);
                List<Address> addresses = null;
                try {
                    //methodcall(latitude,longitude);
                    addresses = gCoder.getFromLocation(latitude, longitude, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    Log.i("myAddresss", addresses.get(0).getFeatureName()
                            + addresses.get(0).getFeatureName()
                            + addresses.get(0).getCountryCode()
                            + addresses.get(0).getAdminArea()
                            + addresses.get(0).getLocality()
                            + addresses.get(0).getPremises()
                            + addresses.get(0).getCountryName()
                            + addresses.get(0).getPhone()
                            + addresses.get(0).getPostalCode()
                            + addresses.get(0).getAddressLine(0));
                }





                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else {
           // getLocation();
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        LastLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
       /// Toast.makeText(context, String.valueOf(latitude),Toast.LENGTH_SHORT).show();
    }
}