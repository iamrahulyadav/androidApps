package com.lock.voicescreenlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by ABC on 5/15/2018.
 */

public class IncomingCallBroadCastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!context.getSharedPreferences("voice_recognition_preference", Context.MODE_PRIVATE).getBoolean("enable_lock", false)) {
            VoiceLockScreen.isInCall = true;
            if (!intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getCallState()) {
                    case 0:
                        VoiceLockScreen.isInCall = false;
                        try {
                            if (VoiceLockScreen.locked) {
                                ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).addView(VoiceLockScreen.mTopView, VoiceLockScreen.params);
                                VoiceLockScreen.wm.addView(VoiceLockScreen.mTopView, VoiceLockScreen.params);
                            }
                        } catch (IllegalStateException e2) {
                        }
                        if (VoiceLockScreen.mTopView != null)
                            VoiceLockScreen.mTopView.setVisibility(View.VISIBLE);
                        return;
                    case 1:
                        VoiceLockScreen.isInCall = true;
                        if (VoiceLockScreen.mTopView != null)
                            VoiceLockScreen.mTopView.setVisibility(View.INVISIBLE);
                        try {
                            if (VoiceLockScreen.locked) {
                                ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(VoiceLockScreen.mTopView);
                                VoiceLockScreen.wm.removeView(VoiceLockScreen.mTopView);
                                return;
                            }
                            return;
                        } catch (IllegalArgumentException e3) {
                            return;
                        }
                    case 2:
                        VoiceLockScreen.isInCall = true;
                        if (VoiceLockScreen.mTopView != null)
                        VoiceLockScreen.mTopView.setVisibility(View.INVISIBLE);
                        try {
                            if (VoiceLockScreen.locked) {
                                ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(VoiceLockScreen.mTopView);
                                VoiceLockScreen.wm.removeView(VoiceLockScreen.mTopView);
                                return;
                            }
                            return;
                        } catch (IllegalArgumentException e3) {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
        else if (VoiceLockScreen.mTopView != null && VoiceLockScreen.wm != null && VoiceLockScreen.homeKeyDisabled) {
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                VoiceLockScreen.isInCall = true;
                VoiceLockScreen.mTopView.setVisibility(View.INVISIBLE);
                try {
                    if (VoiceLockScreen.locked) {
                        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(VoiceLockScreen.mTopView);
                        VoiceLockScreen.wm.removeView(VoiceLockScreen.mTopView);
                        return;
                    }
                    return;
                } catch (IllegalArgumentException e) {
                    return;
                }
            }
            switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getCallState()) {
                case 0:
                    VoiceLockScreen.isInCall = false;
                    try {
                        if (VoiceLockScreen.locked) {
                            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).addView(VoiceLockScreen.mTopView, VoiceLockScreen.params);
                            VoiceLockScreen.wm.addView(VoiceLockScreen.mTopView, VoiceLockScreen.params);
                        }
                    } catch (IllegalStateException e2) {
                    }
                    VoiceLockScreen.mTopView.setVisibility(View.VISIBLE);
                    return;
                case 1:
                case 2:
                    VoiceLockScreen.isInCall = true;
                    VoiceLockScreen.mTopView.setVisibility(View.INVISIBLE);
                    try {
                        if (VoiceLockScreen.locked) {
                            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(VoiceLockScreen.mTopView);
                            VoiceLockScreen.wm.removeView(VoiceLockScreen.mTopView);
                            return;
                        }
                        return;
                    } catch (IllegalArgumentException e3) {
                        return;
                    }
                default:
                    return;
            }
        }
    }
}
