package com.lock.voicescreenlock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lock.voicescreenlock.service.VoiceLockService;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by ABC on 5/15/2018.
 */

public class VoiceLockBroadCastReciever extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    SharedPreferences sp;

    public void onReceive(Context context, Intent intent) {
        this.sp = context.getSharedPreferences("voice_recognition_preference", 0);
        if (!VoiceLockScreen.isInCall) {
            Intent i;
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                wasScreenOn = true;
                i = new Intent(context, VoiceLockScreen.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                wasScreenOn = false;
                i = new Intent(context, VoiceLockScreen.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && sp.getBoolean("service_started", false)) {
            context.startService(new Intent(context, VoiceLockService.class));
        }
    }
}
