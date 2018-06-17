package com.latesttrendingapps.template;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashActivity extends AppCompatActivity {

    com.wang.avi.AVLoadingIndicatorView aviLoader;
    private FirebaseDatabase mFirebaseInstance;
    private int localAppV = 1;
    ProgressDialog prgDialog;
    private DatabaseReference mFirebaseMyStaticData;
    private DatabaseReference mFirebaseMyStaticData1;
    OurApps fBase_OurAppresult1;
    OurApps fBase_OurAppresult11;
    String homePage;
    String showourApps;
    String showAdinOurApps;
    Handler handler;
    int firebaseAppVersion;
    public  static SplashActivity splashObj;
    String showAd,AdonMainScreen;
    InterstitialAd mInterstitialAd;
    String ourApp1LINK = "";
    String ourApp1ICON = "";
    String ourApp2LINK = "";
    String ourApp2ICON = "";
    String ourApp3LINK = "";
    String ourApp3ICON = "";
    String ourApp4LINK = "";
    String ourApp4ICON = "";
    String ourApp5LINK = "";
    String ourApp5ICON = "";
    String ourApp6LINK = "";
    String ourApp6ICON = "";
    String ourApp7LINK = "";
    String ourApp7ICON = "";
    String ourApp8LINK = "";
    String ourApp8ICON = "";
    String ourApp9LINK = "";
    String ourApp9ICON = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashObj = this;
        aviLoader = (com.wang.avi.AVLoadingIndicatorView)findViewById(R.id.avi);
        aviLoader.setVisibility(View.VISIBLE);
        mInterstitialAd = new InterstitialAd(this);
        if(mInterstitialAd != null){
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
            requestNewInterstitial();

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);

                }

            });
        }
        if(isConnectedToInternet()) {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            if(mFirebaseInstance != null){
                mFirebaseMyStaticData = mFirebaseInstance.getReference("OurApps");
                mFirebaseMyStaticData1 = mFirebaseInstance.getReference("OurAppsInterstitial");
                getStaticData();
                getStaticData1();
            }
            mFirebaseInstance.getReference("AppVersionToUpdate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    firebaseAppVersion = Integer.parseInt(dataSnapshot.getValue(String.class));
                    StaticDataHandler.getInstance().setAppVersionToUpdate(firebaseAppVersion);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            mFirebaseInstance.getReference("ShowInterstitialAd").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showAd = dataSnapshot.getValue(String.class);
                    StaticDataHandler.getInstance().setShowInterstitialAd(showAd);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            mFirebaseInstance.getReference("AdonMainScreen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AdonMainScreen = dataSnapshot.getValue(String.class);
                    StaticDataHandler.getInstance().setAdonMainScreen(AdonMainScreen);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
//            AdonMainScreen
//            mFirebaseInstance.getReference("ShowOurApps").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    showourApps = dataSnapshot.getValue(String.class);
//                    StaticDataHandler.getInstance().setOurApps(showourApps);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                }
//            });
//            mFirebaseInstance.getReference("ShowAdInOurApps").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    showAdinOurApps = dataSnapshot.getValue(String.class);
//                    StaticDataHandler.getInstance().setShowAdInOurApps(showAdinOurApps);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                }
//            });
            mFirebaseInstance.getReference("HideHomeActivity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    homePage = dataSnapshot.getValue(String.class);
                    StaticDataHandler.getInstance().setHideHomeActivity(homePage);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkData();
                }
            }, 1000);

        }else{
            MyDynamicToast.informationMessage(SplashActivity.this, "No internet connection..");
//            Toast.makeText(getApplicationContext(),"No internet connection, please connect your device to internet",Toast.LENGTH_LONG).show();
        }
    }
    public void getStaticData(){
        mFirebaseMyStaticData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fBase_OurAppresult1 = dataSnapshot.getValue(OurApps.class);
                    assignStaticValues();
                }
                else{

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(prgDialog != null && prgDialog.isShowing()){
                    prgDialog.dismiss();
                }
            }
        });

    }
    public void getStaticData1(){
        mFirebaseMyStaticData1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fBase_OurAppresult11 = dataSnapshot.getValue(OurApps.class);
                    assignStaticValues1();
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void assignStaticValues1(){
        StaticDataHandler.getInstance().setOurApp1LinkIn(fBase_OurAppresult11.getOurApp1Link());
        StaticDataHandler.getInstance().setOurApp1iconIn(fBase_OurAppresult11.getOurApp1icon());
        StaticDataHandler.getInstance().setOurApp2LinkIn(fBase_OurAppresult11.getOurApp2Link());
        StaticDataHandler.getInstance().setOurApp2iconIn(fBase_OurAppresult11.getOurApp2icon());
        StaticDataHandler.getInstance().setOurApp3LinkIn(fBase_OurAppresult11.getOurApp3Link());
        StaticDataHandler.getInstance().setOurApp3iconIn(fBase_OurAppresult11.getOurApp3icon());
        StaticDataHandler.getInstance().setOurApp4LinkIn(fBase_OurAppresult11.getOurApp4Link());
        StaticDataHandler.getInstance().setOurApp4iconIn(fBase_OurAppresult11.getOurApp4icon());
        StaticDataHandler.getInstance().setOurApp5LinkIn(fBase_OurAppresult11.getOurApp5Link());
        StaticDataHandler.getInstance().setOurApp5iconIn(fBase_OurAppresult11.getOurApp5icon());
        StaticDataHandler.getInstance().setOurApp6LinkIn(fBase_OurAppresult11.getOurApp6Link());
        StaticDataHandler.getInstance().setOurApp6iconIn(fBase_OurAppresult11.getOurApp6icon());
        StaticDataHandler.getInstance().setOurApp7LinkIn(fBase_OurAppresult11.getOurApp7Link());
        StaticDataHandler.getInstance().setOurApp7iconIn(fBase_OurAppresult11.getOurApp7icon());
        StaticDataHandler.getInstance().setOurApp8LinkIn(fBase_OurAppresult11.getOurApp8Link());
        StaticDataHandler.getInstance().setOurApp8iconIn(fBase_OurAppresult11.getOurApp8icon());
        StaticDataHandler.getInstance().setOurApp9LinkIn(fBase_OurAppresult11.getOurApp9Link());
        StaticDataHandler.getInstance().setOurApp9iconIn(fBase_OurAppresult11.getOurApp9icon());
    }
    public void assignStaticValues(){
        StaticDataHandler.getInstance().setOurApp1Link(fBase_OurAppresult1.getOurApp1Link());
        StaticDataHandler.getInstance().setOurApp1icon(fBase_OurAppresult1.getOurApp1icon());
        StaticDataHandler.getInstance().setOurApp2Link(fBase_OurAppresult1.getOurApp2Link());
        StaticDataHandler.getInstance().setOurApp2icon(fBase_OurAppresult1.getOurApp2icon());
        StaticDataHandler.getInstance().setOurApp3Link(fBase_OurAppresult1.getOurApp3Link());
        StaticDataHandler.getInstance().setOurApp3icon(fBase_OurAppresult1.getOurApp3icon());
        StaticDataHandler.getInstance().setOurApp4Link(fBase_OurAppresult1.getOurApp4Link());
        StaticDataHandler.getInstance().setOurApp4icon(fBase_OurAppresult1.getOurApp4icon());
        StaticDataHandler.getInstance().setOurApp5Link(fBase_OurAppresult1.getOurApp5Link());
        StaticDataHandler.getInstance().setOurApp5icon(fBase_OurAppresult1.getOurApp5icon());
        StaticDataHandler.getInstance().setOurApp6Link(fBase_OurAppresult1.getOurApp6Link());
        StaticDataHandler.getInstance().setOurApp6icon(fBase_OurAppresult1.getOurApp6icon());
        StaticDataHandler.getInstance().setOurApp7Link(fBase_OurAppresult1.getOurApp7Link());
        StaticDataHandler.getInstance().setOurApp7icon(fBase_OurAppresult1.getOurApp7icon());
        StaticDataHandler.getInstance().setOurApp8Link(fBase_OurAppresult1.getOurApp8Link());
        StaticDataHandler.getInstance().setOurApp8icon(fBase_OurAppresult1.getOurApp8icon());
        StaticDataHandler.getInstance().setOurApp9Link(fBase_OurAppresult1.getOurApp9Link());
        StaticDataHandler.getInstance().setOurApp9icon(fBase_OurAppresult1.getOurApp9icon());
        loadOurApps();
    }

    private void loadOurApps() {

    }

    public void checkData(){
        if(homePage != null && firebaseAppVersion != 0 && AdonMainScreen != null  && fBase_OurAppresult1 != null && fBase_OurAppresult11 != null){
            checkVersion();
        }else{
            if(isConnectedToInternet()){
                delayCallToGetFBData();
            }else{
                MyDynamicToast.informationMessage(SplashActivity.this, "Check your internet connection..");
                delayCallToGetFBData();
            }
        }
    }
    public void delayCallToGetFBData(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkData();
            }
        }, 3000);
    }
    public void requestNewInterstitial() {
        AdRequest adRequest1 = new AdRequest.Builder().build();
        if(adRequest1 != null){
            mInterstitialAd.loadAd(adRequest1);
        }

    }
    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }
    public void checkVersion(){
        if( localAppV  >= firebaseAppVersion){
            if(homePage.equals("true")){
                startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                aviLoader.setVisibility(View.GONE);
                finish();
            }else{
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                aviLoader.setVisibility(View.GONE);
                finish();
            }
        }
        else
        {
            Intent i = new Intent(SplashActivity.this, UpdateApp.class);
            startActivity(i);
            aviLoader.setVisibility(View.GONE);
            finish();
        }

    }
}
