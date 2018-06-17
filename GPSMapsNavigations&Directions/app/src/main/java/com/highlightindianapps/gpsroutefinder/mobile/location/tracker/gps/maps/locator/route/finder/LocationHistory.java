package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class LocationHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<LocationHistoryStorage> locationHistoryList;
    private ProgressDialog progressDialog;
    private SharedPreferences mSharedPreferences;
    private Array mArray;
    private String locHis;

    private String address, time;

    boolean adLoaded, dataLoaded;
    public static final int NUMBER_OF_ADS = 5;
    private List<NativeAd> mNativeAds = new ArrayList<>();

    List<Object> mRecyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        progressDialog = new ProgressDialog(LocationHistory.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadNativeAd();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        mSharedPreferences = getSharedPreferences("locationHistory", this.MODE_PRIVATE);

        locHis = mSharedPreferences.getString("locationHistory", "");

//        locationHistoryList = new ArrayList<>();

        if (locHis.equals("")) {
            MyDynamicToast.informationMessage(LocationHistory.this, "Location History is not Yet Registered..");
            progressDialog.dismiss();
            finish();

        } else {
            String[] arr = locHis.split(";");
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; i++) {
                    try {
                        JSONObject jsonObj = new JSONObject(arr[i]);
                        address = jsonObj.getString("address");
                        time = jsonObj.getString("time");

                        LocationHistoryStorage student = new LocationHistoryStorage(
                                address, time
                        );

                        mRecyclerViewItems.add(student);
                        dataLoaded = true;
                        insertAdsInMenuItems();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


        }
    }

    private void loadNativeAd() {
        loadNativeAd(0);
    }
    private void loadNativeAd(final int adLoadCount) {

        if (adLoadCount >= NUMBER_OF_ADS) {
            adLoaded = true;
            insertAdsInMenuItems();
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(LocationHistory.this, "ca-app-pub-4445981284747506/6783416261");
        AdLoader adLoader = builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                // An app install ad loaded successfully, call this method again to
                // load the next ad.
                mNativeAds.add(ad);
                loadNativeAd(adLoadCount + 1);

            }
        }).forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                // A content ad loaded successfully, call this method again to
                // load the next ad.
                mNativeAds.add(ad);
                loadNativeAd(adLoadCount + 1);
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // A native ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                        + errorCode + " load another.");
                loadNativeAd(adLoadCount );
            }
        }).build();

        // Load the Native Express ad.
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void insertAdsInMenuItems() {

        if (mNativeAds.size() <= 0) {
            return;
        }

        if (dataLoaded && adLoaded) {

//            int offset = (mRecyclerViewItems.size() / mNativeAds.size()) + 1;
//            int index = 4;
//            for (NativeAd ad : mNativeAds) {
//                mRecyclerViewItems.add(index, ad);
//                index = index + offset;
//                    }
            if (mRecyclerViewItems.size() >= 27) {
                int offset = 5;
                int index = 2;
                for (NativeAd ad : mNativeAds) {
                    mRecyclerViewItems.add(index, ad);
                    index = index + offset;
                }
            } else if (mRecyclerViewItems.size() >= 5 && mRecyclerViewItems.size() < 27) {
                mRecyclerViewItems.add(4, mNativeAds.get(0));
                if (mRecyclerViewItems.size() >= 10) {
                    mRecyclerViewItems.add(8, mNativeAds.get(1));
                }
                if (mRecyclerViewItems.size() >= 15) {
                    mRecyclerViewItems.add(12, mNativeAds.get(2));
                }
                if (mRecyclerViewItems.size() >= 20) {
                    mRecyclerViewItems.add(16, mNativeAds.get(3));
                }
            } else if (mRecyclerViewItems.size() >= 1 && mRecyclerViewItems.size() < 5) {
                mRecyclerViewItems.add(1, mNativeAds.get(0));
            }
            adapter = new LocationHistoryAdapter(mRecyclerViewItems, this);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isConnectedToInternet()){
            if (StaticDataHandler.getInstance().getShowInterstitialAd() != null) {
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(LocationHistory.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(LocationHistory.this, "Check your internet connection..");
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
