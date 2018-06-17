package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

/**
 * Created by ABC on 4/30/2018.
 */

public class LocationUpdaterService extends Service {

    public static final int TWO_MINUTES = 3600000;
    private static final String TAG = "MyLocationService";
    public static Boolean isRunning = false;
    public double latitude, longitude;
    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    private SharedPreferences sharedPreferences;
    public Location previousBestLocation = null;
    private Timer mTimer = null;
    private GPSTracker gps;
    private Handler mHandler = new Handler();
    private boolean isFirst;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("SErvice", "onCreate: ");
//        mLocationListener.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        sharedPreferences = getSharedPreferences("locationHistory", Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (checkPermission()) {
            if (startId == 1) {
                isFirst = true;
                if (mTimer != null)
                    mTimer.cancel();
                else
                    mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, TWO_MINUTES);
            } else {
                isFirst = false;
                if (mTimer != null)
                    mTimer.cancel();
                else
                    mTimer = new Timer();   //recreate new
                mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, TWO_MINUTES);
            }
        }

        return START_STICKY;
    }
    class TimeDisplay extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable(){

                @Override
                public void run() {

                    if (isFirst) {
                        startListening();
                    }

                    isFirst = true;
                }
            });

        }
    }

//    Runnable mHandlerTask = new Runnable(){
//        @Override
//        public void run() {
//            if (!isRunning) {
//                Log.d("SErvice", "run: Handler");
//                sharedPreferences = getSharedPreferences("locationHistory", Context.MODE_PRIVATE);
//                startListening();
//            }
//            mHandler.postDelayed(mHandlerTask, TWO_MINUTES);
//        }
//    };

//

    @Override
    public void onDestroy() {
        stopListening();
        mTimer.cancel();
        Log.d("SErvice", "onDestroy: ");
        super.onDestroy();
    }

    private void startListening() {
        Log.d("SErvice", "startListening: ");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) mLocationListener);

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mLocationListener);
        }
        isRunning = true;
    }


    private void stopListening() {
        Log.d("SErvice", "stopListening: ");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates((LocationListener) mLocationListener);
        }
        isRunning = false;
    }


    public class LocationUpdaterListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
                previousBestLocation = location;
                try {// Script to post location data to server..
                    Log.d("SErvice", "onLocationChanged: ");
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    latitude = location.getLatitude();
                    longitude =location.getLongitude();

                    SharedPreferences latSharedPRefs;
                    SharedPreferences langSharedPRefs;
                    latSharedPRefs = getSharedPreferences("lat", Context.MODE_PRIVATE);
                    langSharedPRefs = getSharedPreferences("lang", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editorLat;
                    editorLat = latSharedPRefs.edit();
                    editorLat.putString("lat", Double.toString(latitude));
                    editorLat.commit();

                    SharedPreferences.Editor editorLang;
                    editorLang = langSharedPRefs.edit();
                    editorLang.putString("lang", Double.toString(longitude));
                    editorLang.commit();

                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        long time = location.getTime();
                        Date dt = new Date(time);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                        String time1 = sdf.format(dt);

                        SharedPreferences.Editor editor;
                        editor = sharedPreferences.edit();

                        String previousLocation = sharedPreferences.getString("locationHistory", "");
                        Log.d(TAG, "Location History" + previousLocation);


                        JSONObject historyObjf = new JSONObject();
                        try {
                            historyObjf.put("address",address);
                            historyObjf.put("time",time1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String newLocation = historyObjf.toString();
                        String appendedValue = append(previousLocation, newLocation);
                        editor.putString("locationHistory", appendedValue);
                        editor.commit();
                        String Latest = sharedPreferences.getString("locationHistory", "");

                        Log.d(TAG, "Location History" + Latest);

                    } else {
                        Log.e(TAG, "Address: Error");
                    }
//                    SharedPreferences sharedPreferences = getSharedPreferences("location_history", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                    editor.putString("Lat", Double.toString(previousBestLocation.getLatitude()));
//                    editor.putString("Longitude",Double.toString(previousBestLocation.getLongitude()));
//                    editor.putString("time", Long.toString(previousBestLocation.getTime()));
//                    editor.commit();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Log.d("SErvice", "onLocationChanged:  Finally");
                    stopListening();
                }
        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
//            startListening();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

    }
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    protected String append(String existing_location, String new_location) {
        String latestLocation;
        if (existing_location.isEmpty()) {
            latestLocation = new_location;
        } else {
            latestLocation = new_location+";"+existing_location ;
        }
        return latestLocation;
    }

    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.LocationUpdaterService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
//
private boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

    return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
}
}
