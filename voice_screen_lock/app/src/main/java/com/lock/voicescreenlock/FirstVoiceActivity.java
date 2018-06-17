package com.lock.voicescreenlock;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.lock.voicescreenlock.SplashActivity.splashObj;

public class FirstVoiceActivity extends AppCompatActivity implements RecognitionListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private boolean voiceConfrmPassFlag;
    private boolean voicePassFlag;

    private TextView tvVoicepassInstruction, voiceText;
    private Button saveBtn;
    private String text = "";
    private String voicePassString = "";
    private ImageView microPhoneImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_voice);
        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initializeVariables();
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(FirstVoiceActivity.this, OurAdActivity.class));
                    }
                }
            }
        }

        sharedPreferences = getSharedPreferences("voice_recognition_preference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        recognizerIntent.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", "en");
        recognizerIntent.putExtra("calling_package", getPackageName());
        recognizerIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        recognizerIntent.putExtra("android.speech.extra.MAX_RESULTS", 1);

        voicePassFlag = false;
        voiceConfrmPassFlag = false;

        if (sharedPreferences.getBoolean("recorded_voice_stored", true)) {
            voiceConfrmPassFlag = true;
            tvVoicepassInstruction.setText("Tap on MIC\nto record Voice Password");
        }
        checkIfFirst();
        microPhoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordFirstVoice(view);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voiceText.getText().toString().length() <= 0) {
                    Toast.makeText(FirstVoiceActivity.this, "Please speak something First to Record", Toast.LENGTH_SHORT).show();
                }else if (voiceConfrmPassFlag) {
                    if (!voicePassFlag) {
                        voicePassString = voiceText.getText().toString();
                        voiceText.setText("");
                        voicePassFlag = true;
                        tvVoicepassInstruction.setText("Now speak again to confirm your Voice Password.");
                    } else if (voicePassString.equalsIgnoreCase(voiceText.getText().toString())) {
                        editor.putString("recorded_voice", voiceText.getText().toString());
                        editor.putBoolean("recorded_voice_stored", false);
                        editor.commit();
                        voiceText.setText("");
                        final Dialog dialog = new Dialog(MainActivity.homeContext);
                        dialog.requestWindowFeature(1);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.dialog_activity);
                        TextView tv_pass_string = (TextView) dialog.findViewById(R.id.tv_done_pass);
                        tv_pass_string.setText(tv_pass_string.getText() + "\"" + text + "\"");
                        ((TextView) dialog.findViewById(R.id.btn_done_pass)).setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                        dialog.getWindow().setLayout((display.getWidth() * 6) / 7, display.getHeight() / 2);
                        finish();
                    } else {
                        tvVoicepassInstruction.setText("Voice-password does not match.");
                    }
                } else if (sharedPreferences.getString("recorded_voice", "hello").equalsIgnoreCase(voiceText.getText().toString())) {
                    voiceConfrmPassFlag = true;
                    voiceText.setText("");
                    tvVoicepassInstruction.setText("Create new Voice Password now.");

                } else {
                    Toast.makeText(FirstVoiceActivity.this, "Voice Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initializeVariables() {
        tvVoicepassInstruction = (TextView) findViewById(R.id.instructor_text);
        voiceText = (TextView) findViewById(R.id.voice_text);
        saveBtn = (Button) findViewById(R.id.btnSave);
        microPhoneImage = (ImageView) findViewById(R.id.microphone_image_btn);
    }

    private void checkIfFirst() {
        if (!this.sharedPreferences.getBoolean("password_not_set", true)) {
            saveBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void recordFirstVoice(View view) {
        try {
            if (getApplicationContext().getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0).size() != 0) {
                speech.startListening(this.recognizerIntent);
                Toast.makeText(this, "Please speak something to Record..", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @SuppressLint("ResourceType")
    @Override
    public void onEndOfSpeech() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onError(int i) {
        Toast.makeText(this, VoiceLockScreen.getErrorText(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList("results_recognition");
        if (matches.size() > 0) {
            this.text = (String) matches.get(0);
        }
        voiceText.setVisibility(View.VISIBLE);
        voiceText.setText(this.text);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (speech != null) {
            speech.destroy();
        }
    }

    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
}
