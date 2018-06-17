package com.latesttrendingapps.template;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import static com.latesttrendingapps.template.SplashActivity.splashObj;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private ViewPager viewPager;
    private SectionPageAdapter mSecitonsPagerAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Electoral Search");

        viewPager = (ViewPager) findViewById(R.id.main_tabsPager);
        mSecitonsPagerAdapter = new SectionPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSecitonsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(viewPager);

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
}
