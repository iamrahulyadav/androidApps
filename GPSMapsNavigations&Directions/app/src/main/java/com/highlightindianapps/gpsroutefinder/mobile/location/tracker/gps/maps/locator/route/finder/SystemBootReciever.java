package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.GPSTracker.context;

/**
 * Created by ABC on 5/1/2018.
 */

public class SystemBootReciever extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
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
