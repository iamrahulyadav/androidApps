package com.highlightindianapps.learnenglishfromhindi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button myFirstButton,mySecondButton,isAmAre,hasHaveHad,pleaseSorry,tense,presentTense,
            pastTense,futureTense,samanyeVasthuye,daysNamesInEnglish,thereUsage,letUsage, bodyParts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSingleInstall();
        loadSingleInstall1();
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                    splashObj.mInterstitialAd.show();
                }else {
                    startActivity(new Intent(MainActivity.this, OurAdActivity.class));
                }
            }
        }else{
            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
        }
//        if(!DataHandler.getInstance().isRatingDone(MainActivity.this) ) {
//            Intent i = new Intent(MainActivity.this, RatingActivity.class);
//            startActivity(i);
//        }

        myFirstButton = (Button) findViewById(R.id.best_way_to_learn_english);
        myFirstButton.setOnClickListener(this);

        mySecondButton = (Button) findViewById(R.id.abhivadhan);
        mySecondButton.setOnClickListener(this);

        isAmAre = (Button) findViewById(R.id.is_am_are);
        isAmAre.setOnClickListener(this);

        hasHaveHad = (Button) findViewById(R.id.has_have_had);
        hasHaveHad.setOnClickListener(this);

        pleaseSorry = (Button) findViewById(R.id.please_sorry);
        pleaseSorry.setOnClickListener(this);

        tense = (Button) findViewById(R.id.tense);
        tense.setOnClickListener(this);

        presentTense = (Button) findViewById(R.id.present_tense);
        presentTense.setOnClickListener(this);

        pastTense = (Button) findViewById(R.id.past_tense);
        pastTense.setOnClickListener(this);

        futureTense = (Button) findViewById(R.id.future_tense);
        futureTense.setOnClickListener(this);

        samanyeVasthuye = (Button) findViewById(R.id.samanye_vasthuye);
        samanyeVasthuye.setOnClickListener(this);

        daysNamesInEnglish = (Button) findViewById(R.id.days_names_in_english);
        daysNamesInEnglish.setOnClickListener(this);

        thereUsage = (Button) findViewById(R.id.there_usage);
        thereUsage.setOnClickListener(this);

        letUsage = (Button) findViewById(R.id.let_usage);
        letUsage.setOnClickListener(this);

        bodyParts = (Button) findViewById(R.id.body_parts);
        bodyParts.setOnClickListener(this);
    }

    private void loadSingleInstall1() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtMain1);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtMain1);
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
                Toast.makeText(MainActivity.this, "Failed to load native ad: "
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

    private void loadSingleInstall() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtMain2);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtMain2);
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
                Toast.makeText(MainActivity.this, "Failed to load native ad: "
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.best_way_to_learn_english:
                Intent bestWayToLernIntent = new Intent(this, WebViewActivity.class);
                bestWayToLernIntent.putExtra("webView", "best_way_to_learn_english");
                startActivity(bestWayToLernIntent);
                break;
            case R.id.abhivadhan:
                Intent abhivadhanIntent = new Intent(this, WebViewActivity.class);
                abhivadhanIntent.putExtra("webView", "abhivadhan");
                startActivity(abhivadhanIntent);
                break;
            case R.id.is_am_are:
                Intent isAmAreIntent = new Intent(this, WebViewActivity.class);
                isAmAreIntent.putExtra("webView", "isAmAre");
                startActivity(isAmAreIntent);
                break;

            case R.id.has_have_had:
                Intent hasHaveHadIntent = new Intent(this, WebViewActivity.class);
                hasHaveHadIntent.putExtra("webView", "hasHaveHad");
                startActivity(hasHaveHadIntent);
                break;

            case R.id.please_sorry:
                Intent pleaseSorryIntent = new Intent(this, WebViewActivity.class);
                pleaseSorryIntent.putExtra("webView", "pleaseSorry");
                startActivity(pleaseSorryIntent);
                break;

            case R.id.tense:
                Intent tenseIntent = new Intent(this, WebViewActivity.class);
                tenseIntent.putExtra("webView", "tense");
                startActivity(tenseIntent);
                break;
            case R.id.present_tense:
                Intent presentTenseIntent = new Intent(this, WebViewActivity.class);
                presentTenseIntent.putExtra("webView", "presentTense");
                startActivity(presentTenseIntent);
                break;

            case R.id.past_tense:
                Intent pastTenseIntent = new Intent(this, WebViewActivity.class);
                pastTenseIntent.putExtra("webView", "pastTense");
                startActivity(pastTenseIntent);
                break;

            case R.id.future_tense:
                Intent futureTenseIntent = new Intent(this, WebViewActivity.class);
                futureTenseIntent.putExtra("webView", "futureTense");
                startActivity(futureTenseIntent);
                break;

            case R.id.samanye_vasthuye:
                Intent samanyeVasthuyeInent = new Intent(this, WebViewActivity.class);
                samanyeVasthuyeInent.putExtra("webView", "samanyeVasthuye");
                startActivity(samanyeVasthuyeInent);
                break;

            case R.id.days_names_in_english:
                Intent daysNamesInEnglishInent = new Intent(this, WebViewActivity.class);
                daysNamesInEnglishInent.putExtra("webView", "daysNamesInEnglish");
                startActivity(daysNamesInEnglishInent);
                break;

            case R.id.there_usage:
                Intent thereUsageInent = new Intent(this, WebViewActivity.class);
                thereUsageInent.putExtra("webView", "thereUsage");
                startActivity(thereUsageInent);
                break;
            case R.id.let_usage:
                Intent letUsageInent = new Intent(this, WebViewActivity.class);
                letUsageInent.putExtra("webView", "letUsage");
                startActivity(letUsageInent);
                break;

            case R.id.body_parts:
                Intent bodyPartsInent = new Intent(this, WebViewActivity.class);
                bodyPartsInent.putExtra("webView", "bodyParts");
                startActivity(bodyPartsInent);
                break;
        }
    }
}
