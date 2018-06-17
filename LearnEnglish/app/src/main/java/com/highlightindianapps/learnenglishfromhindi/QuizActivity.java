package com.highlightindianapps.learnenglishfromhindi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.concurrent.TimeUnit;

import static com.highlightindianapps.learnenglishfromhindi.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.learnenglishfromhindi.MyUtils.populateContentAdView;
import static com.highlightindianapps.learnenglishfromhindi.SplashActivity.splashObj;

public class QuizActivity extends AppCompatActivity {

    public static int pp;
    public static int stp;
    public static int ttl;
    private TextView question, timerView;
    private Button nextBtn;
    private CountDownTimer timer;
    public static int marks;
    private RadioGroup radioOptionsGroup;
    int selectedId;
    private String[] ans;
    private String[] ans1;
    private String[] ans2;
    private String[] ans3;

    public static int unans;
    public static int wrong;

    public static int iv;
    private String[] data;

    private RadioButton ans1Radio,ans2Radio,ans3Radio,ans4Radio;

    public static int ful;

    public static int correct;
    public static int count;
    private Toolbar mToolBar;


    public QuizActivity() {
        if (QuizSettingsActivity.st == 1) {
            iv = 0;
            stp = 10;
            pp = 0;
        }
        if (QuizSettingsActivity.st == 2) {
            iv = 10;
            stp = 20;
            pp = 10;
        }
        if (QuizSettingsActivity.st == 3) {
            iv = 20;
            stp = 30;
            pp = 20;
        }
        if (QuizSettingsActivity.st == 4) {
            iv = 30;
            stp = 40;
            pp = 30;
        }
        if (QuizSettingsActivity.st == 5) {
            iv = 5;
            stp = 15;
            pp = 5;
        }
        if (QuizSettingsActivity.st == 6) {
            iv = 15;
            stp = 25;
            pp = 15;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(QuizActivity.this, OurAdActivity.class));
                    }
                }
            }
        }


        loadSingleInstall();

        Resources res = getResources();
        data = res.getStringArray(R.array.qnsdata);
        ful = data.length - 1;


        ans = res.getStringArray(R.array.ansdata);
        ans1 = res.getStringArray(R.array.ansdata1);
        ans2 = res.getStringArray(R.array.ansdata2);
        ans3 = res.getStringArray(R.array.ansdata3);

        question = (TextView) findViewById(R.id.question);
        nextBtn = (Button) findViewById(R.id.next_btn);


        radioOptionsGroup = (RadioGroup) findViewById(R.id.options);
        ans1Radio = (RadioButton) findViewById(R.id.option_1);
        ans2Radio = (RadioButton) findViewById(R.id.option_2);
        ans3Radio = (RadioButton) findViewById(R.id.option_3);
        ans4Radio = (RadioButton) findViewById(R.id.leave_blank);

        question.setText(data[iv]);
        ans1Radio.setText(ans1[iv]);
        ans2Radio.setText(ans2[iv]);
        ans3Radio.setText(ans3[iv]);
        ans4Radio.setText("Leave Blank");

        timerView = (TextView) findViewById(R.id.time_display);
        timer = new CountDownTimer(180000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))});
                System.out.println(hms);
                timerView.setText("Remaining Time :" + hms);
            }

            @Override
            public void onFinish() {
                timerView.setText("Test Finished");
                if (QuizSettingsActivity.tbflag) {
                    marks = (correct * 3) - wrong;
                } else {
                    marks = correct * 3;
                }
                ttl = (correct + wrong) + unans;
                Intent in = new Intent(QuizActivity.this, ResultActivity.class);
                startActivity(in);
                finish();
            }
        }.start();


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioOptionsGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(QuizActivity.this, "Please Select atleast one", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ansText = ((RadioButton) findViewById(radioOptionsGroup.getCheckedRadioButtonId())).getText().toString();

                if (ansText.equalsIgnoreCase(ans[iv])) {
                    Toast.makeText(QuizActivity.this, "Correct ans", Toast.LENGTH_SHORT).show();
                    correct++;
                } else {
                    if (ansText.equalsIgnoreCase("Leave Blank")) {
                        unans++;
                    } else {
                        wrong++;
                    }
                    Toast.makeText(QuizActivity.this, "Wrong ans", Toast.LENGTH_SHORT).show();
                }
                QuizActivity.iv++;
                QuizActivity.count++;

                if (iv < stp) {
                    radioOptionsGroup.clearCheck();
                    question.setText(data[iv]);
                    ans1Radio.setText(ans1[iv]);
                    ans2Radio.setText(ans2[iv]);
                    ans3Radio.setText(ans3[iv]);
                    ans4Radio.setText("Leave Blank");
                    return;
                }
                if (QuizSettingsActivity.tbflag) {
                    marks = (correct * 3) - wrong;
                } else {
                    marks = correct * 3;
                }
                ttl = (correct + wrong) + unans;
                timerView.setText("Test Finished");
                Intent in = new Intent(QuizActivity.this, ResultActivity.class);
                timer.cancel();
                startActivity(in);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) getString(R.string.quit_quiz)).setCancelable(false)
                .setPositiveButton((CharSequence) "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                timer.cancel();
            }
        }).setNegativeButton((CharSequence) "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();

    }
    private void loadSingleInstall() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtQuiz);
                @SuppressLint("InflateParams")
                NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
                        .inflate(R.layout.ad_app_install, null);
                populateAppInstallAdView(ad, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtQuiz);
                @SuppressLint("InflateParams") NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
                        .inflate(R.layout.ad_content, null);
                populateContentAdView(ad, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(QuizActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

//        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
//                .setNativeAdChoicesIconExpandable(true)
//                .build();
//        AdRequest adRequest = new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .build();
//        adLoader.loadAd(adRequest);

        adLoader.loadAd(new AdRequest.Builder().build());
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
