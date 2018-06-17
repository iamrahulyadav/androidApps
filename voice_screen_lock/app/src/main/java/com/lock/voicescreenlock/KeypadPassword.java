package com.lock.voicescreenlock;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import static com.lock.voicescreenlock.SplashActivity.splashObj;

public class KeypadPassword extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private CardView numberOneCard, numberTwoCard, numberThreeCard,
            numberFourCard, numberFiveCard, numberSixCard, numberSevenCard,
            numberEightCard, numberNineCard, numberZeroCard, doneCard, cutCard;

    private TextView tvTopOnPass,tvAltrPass, dilogTv;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean passrdConfirmFlag;
    private boolean passrdFlag;
    private String password;
    private String passString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad_password);
        initializeVars();
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(KeypadPassword.this, OurAdActivity.class));
                    }
                }
            }
        }
        sharedPreferences = getSharedPreferences("voice_recognition_preference", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        passrdFlag = false;
        passrdConfirmFlag = false;
        if (sharedPreferences.getBoolean("alternative_password_stored", true)) {
            passrdFlag = true;
            tvTopOnPass.setText("Enter New Password");
        }
    }

    private void initializeVars() {
        tvTopOnPass = (TextView) findViewById(R.id.tv_topon_home_password);
        dilogTv = (TextView) findViewById(R.id.tv_done_pass);
        tvAltrPass = (TextView) findViewById(R.id.tv_alt_pass);

        numberZeroCard = (CardView) findViewById(R.id.number_0);
        numberZeroCard.setOnClickListener(this);

        numberOneCard = (CardView) findViewById(R.id.number_1);
        numberOneCard.setOnClickListener(this);

        numberTwoCard = (CardView) findViewById(R.id.number_2);
        numberTwoCard.setOnClickListener(this);

        numberThreeCard = (CardView) findViewById(R.id.number_3);
        numberThreeCard.setOnClickListener(this);

        numberFourCard = (CardView) findViewById(R.id.number_4);
        numberFourCard.setOnClickListener(this);

        numberFiveCard = (CardView) findViewById(R.id.number_5);
        numberFiveCard.setOnClickListener(this);

        numberSixCard = (CardView) findViewById(R.id.number_6);
        numberSixCard.setOnClickListener(this);

        numberSevenCard = (CardView) findViewById(R.id.number_7);
        numberSevenCard.setOnClickListener(this);

        numberEightCard = (CardView) findViewById(R.id.number_8);
        numberEightCard.setOnClickListener(this);

        numberNineCard = (CardView) findViewById(R.id.number_9);
        numberNineCard.setOnClickListener(this);

        doneCard = (CardView) findViewById(R.id.done);
        doneCard.setOnClickListener(this);

        cutCard = (CardView) findViewById(R.id.cut);
        cutCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        password = tvAltrPass.getText().toString();
        switch (view.getId()) {
            case R.id.number_1 :
                password += "1";
                break;
            case R.id.number_2 :
                password += "2";
                break;
            case R.id.number_3:
                password += "3";
                break;
            case R.id.number_4:
                password += "4";
                break;
            case R.id.number_5:
                password += "5";
                break;
            case R.id.number_6:
                password += "6";
                break;
            case R.id.number_7:
                password += "7";
                break;
            case R.id.number_8:
                password += "8";
                break;
            case R.id.number_9:
                password += "9";
                break;
            case R.id.number_0:
                password += "0";
                break;
            case R.id.cut:
                if (password.length() > 0) {
                    password = password.substring(0, password.length() - 1);
                    break;
                }
                break;
            case R.id.done:
                if (!passrdFlag) {
                    if (tvAltrPass.getText().toString().length() > 0 && !sharedPreferences.getString("alternative_password", "1234").equalsIgnoreCase(tvAltrPass.getText().toString())) {
                        tvAltrPass.setText("");
                        tvTopOnPass.setText("Wrong password");
                        password = "";
                        break;
                    } else if (tvAltrPass.getText().toString().length() > 0){
                        tvAltrPass.setText("");
                        tvTopOnPass.setText("Enter New Password");
                        passrdFlag = true;
                        return;
                    }else if (tvAltrPass.getText().length() <= 3) {
                        Toast.makeText(this, "Password is too short (Minimum 4 digits are required)", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }else if (tvAltrPass.getText().length() <= 3) {
                    Toast.makeText(this, "Password is too short (Minimum 4 digits are required)", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!passrdConfirmFlag) {
                    passString = tvAltrPass.getText().toString();
                    tvAltrPass.setText("");
                    passrdConfirmFlag = true;
                    tvTopOnPass.setText("PLEASE CONFIRM PASSWORD");
                    return;
                } else if (tvAltrPass.getText().toString().equalsIgnoreCase(passString)) {
                    editor.putString("alternative_password", tvAltrPass.getText().toString());
                    editor.putBoolean("alternative_password_stored", false);
                    editor.commit();
                    passString = "";
                    Dialog dialog = new Dialog(this);
                    dialog.requestWindowFeature(1);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.dialog_activity);
                    TextView tv_pass_string = (TextView) dialog.findViewById(R.id.tv_done_pass);
                    tv_pass_string.setText(R.string.your_alternate_pass);
                    tv_pass_string.setText(tv_pass_string.getText() + "\n\"" + tvAltrPass.getText().toString() + "\"");
                    ((TextView) dialog.findViewById(R.id.btn_done_pass)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           finish();
                        }
                    });
                    dialog.show();
                    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    dialog.getWindow().setLayout((display.getWidth() * 6) / 7, display.getHeight() / 2);
                    tvAltrPass.setText("");
                    tvTopOnPass.setText("");
                    break;
                } else {
                    tvAltrPass.setText("");
                    tvTopOnPass.setText("Password does not Match");
                    return;
                }
        }
        tvAltrPass.setText(password);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!passrdFlag && sharedPreferences.getString("alternative_password", "1234").length() == tvAltrPass.getText().toString().length() && sharedPreferences.getString("alternative_password", "1234").equalsIgnoreCase(tvAltrPass.getText().toString())) {
            passrdFlag = true;
            tvAltrPass.setText("");
            tvTopOnPass.setText("NOW ENTER NEW PASSWORD");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onRestart() {
        super.onRestart();
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
