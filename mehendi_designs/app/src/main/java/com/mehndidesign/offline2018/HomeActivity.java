package com.mehndidesign.offline2018;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.List;

import static com.mehndidesign.offline2018.MyUtils.populateAppInstallAdView;
import static com.mehndidesign.offline2018.MyUtils.populateContentAdView;
import static com.mehndidesign.offline2018.SplashActivity.splashObj;

public class HomeActivity extends AppCompatActivity {

    ProgressDialog prgDialog;
    ImageView oneImg, twoImg, threeImg, fourImg, fiveImg, sixImg, sevenImg, eightImg, nineImg;
    LinearLayout expressAdView;
    LinearLayout ourAppsView;
    TextView viewall;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(HomeActivity.this, OurAdActivity.class));
                    }
                }
            }
        }
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        loadSingleInstall();
        ourAppsView = (LinearLayout) findViewById(R.id.ourAppsLayout);
        viewall = (TextView)findViewById(R.id.viewall);
        if(viewall != null && isConnectedToInternet()) {
            viewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnectedToInternet()) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Most+Usefull+Apps"));
                        startActivity(i);
                    }else{
                        MyDynamicToast.informationMessage(HomeActivity.this, "Check your internet connection..");
                    }
                }
            });
        }
//        if( StaticDataHandler.getInstance().getOurApps()!= null){
//            if(StaticDataHandler.getInstance().getOurApps().equals("true")){
//                ourAppsView.setVisibility(View.VISIBLE);
//            }
//        }
//        prgDialog = new ProgressDialog(this);
//        prgDialog.setMessage("Please wait...");
//        prgDialog.setCancelable(false);
//        prgDialog.show();
        start = (Button) findViewById(R.id.start);
        if(start != null && isConnectedToInternet()){
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnectedToInternet()){
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                    }else{
                        MyDynamicToast.informationMessage(HomeActivity.this, "Check your internet connection..");
                    }
                }
            });
        }else{
            MyDynamicToast.informationMessage(HomeActivity.this, "Check your internet connection..");
        }
        if(isConnectedToInternet()){
//            if(StaticDataHandler.getInstance().getOurApps().equals("true")) {
//                loadOurApps();
//            }else{
//                if(prgDialog != null && prgDialog.isShowing()){
//                    prgDialog.dismiss();
//                }
//            }
        }else{
            MyDynamicToast.informationMessage(HomeActivity.this, "No internet connection, please connect your device to internet..");
//            Toast.makeText(getApplicationContext(),"No internet connection, please connect your device to internet",Toast.LENGTH_LONG).show();
        }
    }
    void loadOurApps(){
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(HomeActivity.this));

        if(StaticDataHandler.getInstance().getOurApp1Link() == null || StaticDataHandler.getInstance().getOurApp2Link() == null ||
                StaticDataHandler.getInstance().getOurApp3Link() == null || StaticDataHandler.getInstance().getOurApp4Link() == null ||
                StaticDataHandler.getInstance().getOurApp5Link() == null || StaticDataHandler.getInstance().getOurApp6Link() == null ||
                StaticDataHandler.getInstance().getOurApp7Link() == null || StaticDataHandler.getInstance().getOurApp8Link() == null ||
                StaticDataHandler.getInstance().getOurApp9Link() == null
                )
        {
            return;
        }

        if(StaticDataHandler.getInstance().getOurApp1icon() == null || StaticDataHandler.getInstance().getOurApp2icon() == null ||
                StaticDataHandler.getInstance().getOurApp3icon() == null || StaticDataHandler.getInstance().getOurApp4icon() == null ||
                StaticDataHandler.getInstance().getOurApp5icon() == null || StaticDataHandler.getInstance().getOurApp6icon() == null ||
                StaticDataHandler.getInstance().getOurApp7icon() == null || StaticDataHandler.getInstance().getOurApp8icon() == null ||
                StaticDataHandler.getInstance().getOurApp9icon() == null
                )
        {
            return;
        }

        if(!StaticDataHandler.getInstance().getOurApp1Link().equals("")){
            oneImg = (ImageView)findViewById(R.id.one);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp1icon(), oneImg);
            if(oneImg != null){
                oneImg.setVisibility(View.VISIBLE);
                oneImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp1Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp2Link().equals("")){
            twoImg = (ImageView)findViewById(R.id.two);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp2icon(), twoImg);
            if(twoImg != null){
                twoImg.setVisibility(View.VISIBLE);
                twoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp2Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }


        if(!StaticDataHandler.getInstance().getOurApp3Link().equals("")){
            threeImg = (ImageView)findViewById(R.id.three);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp3icon(), threeImg);
            if(threeImg != null){
                threeImg.setVisibility(View.VISIBLE);
                threeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp3Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp4Link().equals("")){
            fourImg = (ImageView)findViewById(R.id.four);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp4icon(), fourImg);
            if(fourImg != null){
                fourImg.setVisibility(View.VISIBLE);
                fourImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp4Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp5Link().equals("")){
            fiveImg = (ImageView)findViewById(R.id.five);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp5icon(), fiveImg);
            if(fiveImg != null){
                fiveImg.setVisibility(View.VISIBLE);
                fiveImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp5Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp6Link().equals("")){
            sixImg = (ImageView)findViewById(R.id.six);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp6icon(), sixImg);
            if(sixImg != null){
                sixImg.setVisibility(View.VISIBLE);
                sixImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp6Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp7Link().equals("")){
            sevenImg = (ImageView)findViewById(R.id.seven);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp7icon(), sevenImg);
            if(sevenImg != null){
                sevenImg.setVisibility(View.VISIBLE);
                sevenImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp7Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp8Link().equals("")){
            eightImg = (ImageView)findViewById(R.id.eight);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp8icon(), eightImg);
            if(eightImg != null){
                eightImg.setVisibility(View.VISIBLE);
                eightImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp8Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }

        if(!StaticDataHandler.getInstance().getOurApp9Link().equals("")){
            nineImg = (ImageView)findViewById(R.id.nine);
            imageLoader.displayImage(StaticDataHandler.getInstance().getOurApp9icon(), nineImg);
            if(nineImg != null){
                nineImg.setVisibility(View.VISIBLE);
                nineImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp9Link()));
                        startActivity(myIntent);
                    }
                });
            }
        }
        if(prgDialog != null && prgDialog.isShowing()){
            prgDialog.dismiss();
        }
    }
    public boolean isConnectedToInternet(){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
    private void loadSingleInstall() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtHome);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtHome);
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
                Toast.makeText(HomeActivity.this, "Failed to load native ad: "
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
    public void onBackPressed() {
        startActivity(new Intent(HomeActivity.this, OurAppsActivity.class));
        finish();
    }
}
