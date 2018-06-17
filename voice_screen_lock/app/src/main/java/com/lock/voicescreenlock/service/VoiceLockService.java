package com.lock.voicescreenlock.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lock.voicescreenlock.VoiceLockBroadCastReciever;


/**
 * Created by ABC on 5/15/2018.
 */

public class VoiceLockService extends Service {

    private BroadcastReceiver broadcastReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        Log.d("VoiceLockService", "onCreate: ");
        ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();
        super.onCreate();
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("VoiceLockService", "onStart: ");
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        broadcastReceiver = new VoiceLockBroadCastReciever();
        registerReceiver(broadcastReceiver, filter);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("VoiceLockService", "onStartCommand: ");
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        broadcastReceiver = new VoiceLockBroadCastReciever();
        registerReceiver(broadcastReceiver, filter);
        return START_STICKY;
    }

    public void onDestroy() {
        Log.d("VoiceLockService", "onDestroy: ");
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}
