package com.lock.voicescreenlock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.lock.voicescreenlock.service.VoiceLockService;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.RECORD_AUDIO;
import static com.lock.voicescreenlock.SplashActivity.splashObj;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch aSwitch;
    private Boolean switchState;
    private CardView voicePassword,keypadPassword,appSettings, rateUs;
    private SharedPreferences sharedPreferences;
    public static Context homeContext;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean accepted;
    private boolean askedForOverlayPermission, permissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeContext = this;
        aSwitch = (Switch) findViewById(R.id.activate_phone_lock);
        sharedPreferences = getSharedPreferences("voice_recognition_preference", MODE_PRIVATE);

        rateUs = (CardView) findViewById(R.id.rate_us);
        rateUs.setOnClickListener(this);
        voicePassword = (CardView) findViewById(R.id.voice_password);
        voicePassword.setOnClickListener(this);

        keypadPassword = (CardView) findViewById(R.id.keypad_password);
        keypadPassword.setOnClickListener(this);

        appSettings = (CardView) findViewById(R.id.more_apps);
        appSettings.setOnClickListener(this);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null){
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(MainActivity.this, OurAdActivity.class));
                    }
                }
            }

        }else{
            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
        }

        addOverlay();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, READ_PHONE_STATE, READ_CALL_LOG, READ_SMS}, PERMISSION_REQUEST_CODE);
    }

    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voice_password:
                if (checkPermission()) {
                    startActivity(new Intent(MainActivity.this,FirstVoiceActivity.class));
                } else {
                    requestPermission();
                }
                break;
            case  R.id.keypad_password:
                if (checkPermission()) {
                    startActivity(new Intent(MainActivity.this, KeypadPassword.class));

                } else
                    requestPermission();
                break;
            case R.id.more_apps :
                if (checkPermission()) {
                    startActivity(new Intent(MainActivity.this, OurAppsActivity.class));
                } else
                    requestPermission();
                break;
            case R.id.rate_us:
                startActivity(new Intent(MainActivity.this, RatingActivity.class));
                break;
            default:
                return;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    if (checkPermission()) {
                        stopService(new Intent(MainActivity.this, VoiceLockService.class));
                        sharedPreferences.edit().putBoolean("service_started", false).commit();
                    }

                } else {
                    boolean s = sharedPreferences.getBoolean("permission_granted",false);
                    if (checkPermission() && s) {
                        startService(new Intent(MainActivity.this, VoiceLockService.class));
                        sharedPreferences.edit().putBoolean("service_started", true).commit();
                    } else {
                        aSwitch.setChecked(false);
                        addOverlay();
                        requestPermission();
                        Toast.makeText(MainActivity.this, "Need To accept Permissions", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
        checkService();
    }

    private void checkService() {
        if (sharedPreferences.getBoolean("service_started", false)) {
            aSwitch.setChecked(true);
            return;
        }
        aSwitch.setChecked(false);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED
                && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED
                && result3 == PackageManager.PERMISSION_GRANTED;
    }

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } else {
                sharedPreferences.edit().putBoolean("permission_granted", true).commit();
            }
        } else {
            sharedPreferences.edit().putBoolean("permission_granted", true).commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            askedForOverlayPermission = false;
            if (Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
//                permissionGranted = true;
                sharedPreferences.edit().putBoolean("permission_granted", true).commit();
                Toast.makeText(MainActivity.this, R.string.drawing_over_other_app_permission_accepted, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, R.string.drawing_over_other_apps_permission_denined, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean recordingAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean phoneStateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean callAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean smsAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (recordingAccepted && phoneStateAccepted && callAccepted && smsAccepted) {
                        accepted = true;
                        Toast.makeText(MainActivity.this, R.string.permission_granted, Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(MainActivity.this, R.string.permission_denined, Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{RECORD_AUDIO, READ_PHONE_STATE, READ_CALL_LOG,READ_SMS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

}
