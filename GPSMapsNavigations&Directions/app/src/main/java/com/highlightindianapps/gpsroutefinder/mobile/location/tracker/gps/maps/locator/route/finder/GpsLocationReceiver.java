package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by ABC on 4/18/2018.
 */

public class GpsLocationReceiver extends BroadcastReceiver {
    private static final String TAG = "MyLocationService";
    GPSTracker gps;
    double latitude, longitude;
    private SharedPreferences sharedPreferences;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
         this.context = context;
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            if (!checkServiceRunning())  {
                Intent i = new Intent(context, LocationUpdaterService.class);
                context.startService(i);
            }
        }
    }
    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
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
}
