package com.lock.voicescreenlock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.telephony.PhoneStateListener.LISTEN_CALL_STATE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;


public class VoiceLockScreen extends AppCompatActivity implements RecognitionListener, View.OnClickListener, TextWatcher {
    public final static int REQUEST_CODE = 65635;
    public static boolean homeKeyDisabled = true;
    public static boolean isInCall = false;
    public static boolean locked = false;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private LinearLayout linearLayoutKeypad;
    private ImageView switchToKey;
    private String text = "";
    private SoundPool sp2;
    BroadcastReceiver _broadcastReceiver;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("hh : mm");
    private ImageButton btnRecord;
    private SharedPreferences sharedPreferences;
    public static WindowManager wm;
    public static ViewGroup mTopView;
    private String password;
    private TextView tv_on_pass_tv;
    private TextView tvAllToast;
    private TextView tvAltrPass, forgotPass, forgetPassHint;
    private TextView tvTime, tvDate, missedCalls, messages;
    private boolean askedForOverlayPermission;
    private Context context;
    private CardView numberOneCard, numberTwoCard, numberThreeCard,
            numberFourCard, numberFiveCard, numberSixCard, numberSevenCard,
            numberEightCard, numberNineCard, numberZeroCard, doneCard, cutCard;
    private SharedPreferences.Editor editor;
    public static final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("voice_recognition_preference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("enable_lock", true)) {
            context = getApplicationContext();
            wm = (WindowManager) getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            params.width = -1;
            params.height = -1;
            mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_voice_lock_screen, null);
            getWindow().setAttributes(params);
            initVars();
            wm.addView(mTopView, params);

        } else {
            mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_voice_lock_screen, null);
            setContentView(mTopView);
            registerReceiver(new Reciever(), new IntentFilter("finish_activity"));
        }
        locked = true;
        mTopView.bringToFront();
        mTopView.setFocusable(true);
        mTopView.setFocusableInTouchMode(true);
        mTopView.setOnKeyListener(new keyListner());
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        recognizerIntent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", "en");
        recognizerIntent.putExtra("calling_package", getPackageName());
        recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 1);
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final SharedPreferences spVoiceLockScreen = getSharedPreferences("voice_recognition_preference", MODE_PRIVATE);
        manager.listen(new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (spVoiceLockScreen.getBoolean("service_started", true) && mTopView.getVisibility() == View.INVISIBLE) {
                            mTopView.setVisibility(View.VISIBLE);
                            break;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (spVoiceLockScreen.getBoolean("service_started", true) && mTopView.getVisibility() == View.VISIBLE) {
                            mTopView.setVisibility(View.INVISIBLE);
                            break;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if (spVoiceLockScreen.getBoolean("service_started", true) && mTopView.getVisibility() == View.VISIBLE) {
                            mTopView.setVisibility(View.INVISIBLE);
                            break;
                        }
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }, LISTEN_CALL_STATE);
    }

    private void initVars() {
        forgetPassHint = (TextView) mTopView.findViewById(R.id.alt_pass_text_forget);
        btnRecord = (ImageButton) mTopView.findViewById(R.id.btn_record_and_match_voice);
        linearLayoutKeypad = (LinearLayout) mTopView.findViewById(R.id.ll_visibility);
        switchToKey = (ImageView) mTopView.findViewById(R.id.switch_to_key);
        tv_on_pass_tv = (TextView) mTopView.findViewById(R.id.tv_topon_home_password);
        tvAltrPass = (TextView) mTopView.findViewById(R.id.tv_alt_pass_);
        tvAllToast = (TextView) mTopView.findViewById(R.id.tv_all_toast);
        tvTime = (TextView) mTopView.findViewById(R.id.tv_time);
        tvDate = (TextView) mTopView.findViewById(R.id.tv_date);
        missedCalls = (TextView) mTopView.findViewById(R.id.missed_calls);
        messages = (TextView) mTopView.findViewById(R.id.messages);
        forgotPass = (TextView) mTopView.findViewById(R.id.forget_pass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("alternative_password", "1234");
                editor.commit();
                forgetPassHint.setVisibility(View.VISIBLE);
                forgetPassHint.setText("Now the Password is your Default Password");
                Toast.makeText(context, "The Password Will be Your Default Password.", Toast.LENGTH_SHORT).show();
            }
        });
        numberZeroCard = (CardView) mTopView.findViewById(R.id.number_0_0);
        numberZeroCard.setOnClickListener(this);

        numberOneCard = (CardView) mTopView.findViewById(R.id.number_1_1);
        numberOneCard.setOnClickListener(this);

        numberTwoCard = (CardView) mTopView.findViewById(R.id.number_2_2);
        numberTwoCard.setOnClickListener(this);

        numberThreeCard = (CardView) mTopView.findViewById(R.id.number_3_3);
        numberThreeCard.setOnClickListener(this);

        numberFourCard = (CardView) mTopView.findViewById(R.id.number_4_4);
        numberFourCard.setOnClickListener(this);

        numberFiveCard = (CardView) mTopView.findViewById(R.id.number_5_5);
        numberFiveCard.setOnClickListener(this);

        numberSixCard = (CardView) mTopView.findViewById(R.id.number_6_6);
        numberSixCard.setOnClickListener(this);

        numberSevenCard = (CardView) mTopView.findViewById(R.id.number_7_7);
        numberSevenCard.setOnClickListener(this);

        numberEightCard = (CardView) mTopView.findViewById(R.id.number_8_8);
        numberEightCard.setOnClickListener(this);

        numberNineCard = (CardView) mTopView.findViewById(R.id.number_9_9);
        numberNineCard.setOnClickListener(this);

        doneCard = (CardView) mTopView.findViewById(R.id.done_);
        doneCard.setOnClickListener(this);

        cutCard = (CardView) mTopView.findViewById(R.id.cut_);
        cutCard.setOnClickListener(this);
    }

        class keyListner implements OnKeyListener {
        keyListner() {
        }

        @SuppressLint({"NewApi"})
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode !=  KeyEvent.KEYCODE_BACK || event.getAction() != KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_HOME || linearLayoutKeypad.getVisibility() != View.VISIBLE) {
                return false;
            }
            linearLayoutKeypad.setVisibility(View.INVISIBLE);
            btnRecord.setVisibility(View.VISIBLE);
            tvAllToast.setVisibility(View.VISIBLE);
            forgetPassHint.setVisibility(View.INVISIBLE);
            password = "";
            tv_on_pass_tv.setText(R.string.enter_your_password);
            tvAllToast.setText(R.string.tap_on_mic_to_speak_voice_password_or_click_on_keypad_icon_to_unlock_with_pincode);
            tvAltrPass.setText("");
            return true;
        }
    }
    class Reciever extends BroadcastReceiver {
        Reciever() {
        }

        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            finish();
        }
    }

    public static String getErrorText(int errorCode) {
        switch (errorCode) {
            case 1:
                return "Network timeout";
            case 2:
                return "Network error";
            case 3:
                return "Audio recording error";
            case 4:
                return "Please connect to internet";
            case 5:
                return "Client side error";
            case 6:
                return "No speech input";
            case 7:
                return "No match";
            case 8:
                return "RecognitionService busy";
            case 9:
                return "Insufficient permissions";
            default:
                return "Didn't understand, please try again.";
        }
    }

    public void startMatchingVoive(View view) {
        try {
            if (getApplicationContext().getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0).size() != 0) {
                tvAllToast.setText("");
                speech.startListening(recognizerIntent);
                tvAllToast.setText("Speak password now");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMatchingVoiveee(View view) {
        btnRecord.setVisibility(View.INVISIBLE);
        linearLayoutKeypad.setVisibility(View.VISIBLE);
    }
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        btnRecord.setBackgroundResource(R.drawable.mike1);
    }

    @Override
    public void onError(int i) {
        btnRecord.setBackgroundResource(R.drawable.mike1);
        tvAllToast.setText(getErrorText(i));
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList("results_recognition");
        if (matches.size() > 0) {
           text = (String) matches.get(0);
        }
        if (sharedPreferences.getString("recorded_voice", "hello").equalsIgnoreCase(text)) {
            locked = false;
            try {
                ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).removeView(mTopView);
                wm.removeView(mTopView);
                mTopView.setVisibility(View.INVISIBLE);
            } catch (IllegalArgumentException e) {
            }
            moveTaskToBack(true);
            if (speech != null) {
                speech.destroy();
            }
            finish();
            finishAffinity();
            System.exit(0);
            return;
        }
        this.tvAllToast.setText("WRONG VOICE PASSWORD \n\"" + this.text + "\"");
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (sharedPreferences.getString("alternative_password", "1234").length() == tvAltrPass.getText().toString().length() && sharedPreferences.getString("alternative_password", "1234").equalsIgnoreCase(tvAltrPass.getText().toString())) {
            tvAltrPass.setText("");
            locked = false;
            try {
                mTopView.setVisibility(View.INVISIBLE);
                ((WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE)).removeView(mTopView);
                wm.removeView(mTopView);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            moveTaskToBack(true);
            if (speech != null) {
                speech.destroy();
            }
            finish();
            finishAffinity();
            System.exit(0);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        password = this.tvAltrPass.getText().toString();
        switch (view.getId()) {
            case R.id.number_1_1 :
                password += "1";
                break;
            case R.id.number_2_2 :
                password += "2";
                break;
            case R.id.number_3_3:
                password += "3";
                break;
            case R.id.number_4_4:
                password += "4";
                break;
            case R.id.number_5_5:
                password += "5";
                break;
            case R.id.number_6_6:
                password += "6";
                break;
            case R.id.number_7_7:
                password += "7";
                break;
            case R.id.number_8_8:
                password += "8";
                break;
            case R.id.number_9_9:
                password += "9";
                break;
            case R.id.number_0_0:
                password += "0";
                break;
            case R.id.cut_:
                if (password.length() > 0) {
                    password = password.substring(0, password.length() - 1);
                    break;
                }
                break;
            case R.id.done_:
                if (tvAltrPass.getText().toString().length() > 0 && !sharedPreferences.getString("alternative_password", "1234").equalsIgnoreCase(tvAltrPass.getText().toString())) {
                    tv_on_pass_tv.setText("Wrong Password");
                    password = "";
                    break;
                }else if (tvAltrPass.getText().toString().length() >0) {
                    locked = false;
                    try {
                        mTopView.setVisibility(View.INVISIBLE);
                        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).removeView(mTopView);
                        wm.removeView(mTopView);
                    } catch (IllegalArgumentException e) {
                    }
                    moveTaskToBack(true);
                    finish();
                    finishAffinity();
                    System.exit(0);
                    break;
                }
        }
        tvAltrPass.setText(this.password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerActionTimeTick();
        getMissedCalls();
        getMessages();
        tvTime.setText(_sdfWatchTime.format(new Date()));
        tvDate.setText(new SimpleDateFormat("d MMMM, ''yyyy\nEEEE").format(Calendar.getInstance().getTime()));
    }

    public void getMissedCalls() {
        String PATH = "content://call_log/calls";
        String sortOrder = CallLog.Calls.DATE + " DESC";
        StringBuffer sb = new StringBuffer();
        sb.append(CallLog.Calls.TYPE).append("=?").append(" and ").append(CallLog.Calls.IS_READ).append("=?");
        String[] projection = new String[]{CallLog.Calls.CACHED_NAME,CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE};
        Cursor c = getApplicationContext().getContentResolver().query(Uri.parse(PATH), projection, sb.toString(),
                new String[] { String.valueOf(CallLog.Calls.MISSED_TYPE), "0" }, sortOrder);
        c.moveToFirst();
        missedCalls.setText(new StringBuilder(String.valueOf(String.valueOf(c.getCount()))).append("  Missed Call ( s )").toString());
        c.close();
    }

    public void getMessages() {
        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"),
                new String[]{"count(_id)"}, "read = 0", null, null);
        c.moveToFirst();
        messages.setText(new StringBuilder(String.valueOf(String.valueOf(c.getInt(0)))).append("  Message ( s )").toString());
        c.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(_broadcastReceiver);

    }

    @Override
    public void onBackPressed() {
        if (linearLayoutKeypad.isShown()) {
            btnRecord.setVisibility(View.VISIBLE);
            tvAllToast.setVisibility(View.VISIBLE);
            linearLayoutKeypad.setVisibility(View.INVISIBLE);
        }
    }

    private void registerActionTimeTick() {
        _broadcastReceiver = new RecieverForTime();
        registerReceiver(this._broadcastReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
    }

    class RecieverForTime extends BroadcastReceiver {
        RecieverForTime() {
        }

        public void onReceive(Context ctx, Intent intent) {
            if (intent.getAction().compareTo("android.intent.action.TIME_TICK") == 0) {
                tvTime.setText(_sdfWatchTime.format(new Date()));
                getMissedCalls();
                getMessages();
            }
        }


    }

}
