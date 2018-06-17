package com.highlightindianapps.learnenglishfromhindi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import static com.highlightindianapps.learnenglishfromhindi.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.learnenglishfromhindi.MyUtils.populateContentAdView;
import static com.highlightindianapps.learnenglishfromhindi.SplashActivity.splashObj;

public class AnswersActivity extends AppCompatActivity {

    private TextView questionText, ansText;
    private String[] data;
    private String[] qata;
    private Button prevBtn,nextBtn;
    public static int ruk;
    public static int prest;
    public static int initialvalue;
    public AnswersActivity() {
        initialvalue = QuizActivity.pp;
        ruk = QuizActivity.stp;
        prest = QuizActivity.pp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(AnswersActivity.this, OurAdActivity.class));
                    }
                }
            }
        }
        loadSingleInstall();
//        initialvalue = QuizActivity.pp;

        questionText = (TextView) findViewById(R.id.ans_question);
        ansText = (TextView) findViewById(R.id.ans_ans);

        Resources res = getResources();
        data = res.getStringArray(R.array.ansdata);
        qata = res.getStringArray(R.array.qnsdata);
        questionText.setText("Qns : " + (qata[initialvalue]));
        ansText.setText("Ans : " + (data[initialvalue]));

        prevBtn = (Button) findViewById(R.id.prev_btn);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnswersActivity mainactivity = AnswersActivity.this;
                initialvalue--;
                if (initialvalue == prest - 1) {
                    initialvalue = QuizActivity.pp;
                    Toast.makeText(AnswersActivity.this, "This is First Qns Shown", Toast.LENGTH_SHORT).show();
                    return;
                }
                ansText.setText("Ans : " + (data[initialvalue]));
                questionText.setText("Qns : " + (qata[initialvalue]));
//                AnsActivity.this.text_nextvalue.setText((initialvalue + 1) + "/" + data.length);
            }
        });

        nextBtn = (Button) findViewById(R.id.next_btn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersActivity mainactivity = AnswersActivity.this;
                initialvalue++;
                if (new StringBuilder(String.valueOf(initialvalue)).toString().equals(new StringBuilder(String.valueOf(ruk)).toString())) {
                    initialvalue = ruk - 1;
                    Toast.makeText(AnswersActivity.this, "This is Last Qns Shown", Toast.LENGTH_SHORT).show();
                    return;
                }
                ansText.setText("Ans : " + (data[initialvalue]));
                questionText.setText("Qns : " + (qata[initialvalue]));
//                text_nextvalue.setText((AnsActivity.initialvalue + 1) + "/" + AnsActivity.this.data.length);
            }
        });
    }

    private void loadSingleInstall() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtAnswers);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtAnswers);
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
                Toast.makeText(AnswersActivity.this, "Failed to load native ad: "
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
