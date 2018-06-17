package com.highlightindianapps.learnenglishfromhindi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class QuizSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static boolean tbflag;
    private Spinner staticSpinner;
    public static int st;
    private Button startBtn;
    private ToggleButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(QuizSettingsActivity.this, OurAdActivity.class));
                    }
                }
            }
        }
        loadSingleInstall();

        tb = (ToggleButton) findViewById(R.id.nagative_marker_toggle_btn);

        staticSpinner = (Spinner) findViewById(R.id.static_spinner);
        staticSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.list, android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(staticAdapter);


        startBtn = (Button) findViewById(R.id.start_timer);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (st == 0) {
                    Toast.makeText(QuizSettingsActivity.this, "Plaese Select Quiz", Toast.LENGTH_SHORT).show();
                    return;
                }
                tbflag = tb.isChecked();
                QuizActivity.iv = 0;
                QuizActivity.marks = 0;
                QuizActivity.wrong = 0;
                QuizActivity.correct = 0;
                QuizActivity.unans = 0;
                QuizActivity.count = 0;
                startActivity(new Intent(QuizSettingsActivity.this,QuizActivity.class));
            }
        });
    }

    private void loadSingleInstall() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtQuizSettings);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtQuizSettings);
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
                Toast.makeText(QuizSettingsActivity.this, "Failed to load native ad: "
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        String item = adapterView.getItemAtPosition(position).toString();

        st = adapterView.getSelectedItemPosition();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        st = 0;
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
