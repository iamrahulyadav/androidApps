package com.highlightindianapps.learnenglishfromhindi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static com.highlightindianapps.learnenglishfromhindi.SplashActivity.splashObj;

public class OurAppsActivity extends AppCompatActivity {
    ImageView oneImg, twoImg, threeImg, fourImg, fiveImg, sixImg, sevenImg, eightImg, nineImg;
    Button rate_us,exit;
    TextView view_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_apps);

        rate_us = (Button) findViewById(R.id.rate_us);
        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getApplication().getPackageName()));
                startActivity(myIntent);

            }
        });
        view_all = (TextView) findViewById(R.id.view_all);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Black%20Hash&hl=en"));
                startActivity(myIntent);
            }
        });

        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(OurAppsActivity.this, OurAdActivity.class));
                    }


                }
            }
        }

        loadOurApps();
    }

    void loadOurApps(){

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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp1icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(oneImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp2icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(twoImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp3icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(threeImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp4icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fourImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp5icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fiveImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp6icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sixImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp7icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(sevenImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp8icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(eightImg);
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
            Glide.with(this).load(StaticDataHandler.getInstance().getOurApp9icon()).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(nineImg);
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
    }
    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){

            //we are connected to a network
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }
        return connected;
    }
}
