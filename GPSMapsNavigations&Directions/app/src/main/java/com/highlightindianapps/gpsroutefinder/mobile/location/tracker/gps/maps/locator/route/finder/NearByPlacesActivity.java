package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import java.util.List;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateContentAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class NearByPlacesActivity extends AppCompatActivity {

    private CardView mBank, mAtm, mHospital, mDoctor, mPetrolBunk, mPolice
            ,mUniversity, mRestaurants, mRailStations, mTemples, mParking, mPharmacy,
    mBusStations, mPark, mMosque, mAirports, mArtGalleries, mBar;

    private GPSTracker gps;
    double latitude, langitude;

    private BottomSheetBehavior mBottomSheetBehavior;
    private NestedScrollView mNestedScrollView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NearByPlacesList> nearByPlacesLists;
    private LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.activity_recyclerview_nsv);

        loadSingleInstall();


        mBank = (CardView) findViewById(R.id.near_by_banks);
        mAtm = (CardView) findViewById(R.id.near_by_atms);
        mHospital = (CardView) findViewById(R.id.near_by_hospitals);
        mDoctor = (CardView) findViewById(R.id.near_by_doctors);
        mPetrolBunk = (CardView) findViewById(R.id.near_by_petrolpumps);
        mPolice = (CardView) findViewById(R.id.near_by_police);
        mUniversity = (CardView) findViewById(R.id.near_by_universitys);
        mRestaurants = (CardView) findViewById(R.id.near_by_restaurants);
        mRailStations = (CardView) findViewById(R.id.near_by_railway_station);
        mTemples = (CardView) findViewById(R.id.near_by_temples);
        mParking = (CardView) findViewById(R.id.near_by_parkings);
        mPharmacy = (CardView) findViewById(R.id.near_by_medical_stores);
        mBusStations = (CardView) findViewById(R.id.near_by_bus_stations);
        mPark = (CardView) findViewById(R.id.near_by_parks);
        mMosque = (CardView) findViewById(R.id.near_by_mosque);
        mAirports = (CardView) findViewById(R.id.near_by_airports);
        mArtGalleries = (CardView) findViewById(R.id.near_by_art_gallery);
        mBar = (CardView) findViewById(R.id.near_by_bars);

//        gps = new GPSTracker(this);
            mBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "bar");
                            startActivity(banksIntent);

                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mArtGalleries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "art_gallery");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mAirports.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "airport");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mMosque.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "mosque");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mPark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "park");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mBusStations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "bus_station");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mPharmacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "pharmacy");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mParking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "parking");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mTemples.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "hindu_temple");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mRailStations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "train_station");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mRestaurants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "restaurant");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mUniversity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "school");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });

            mBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheet3DialogFragment();
//                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "bank");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mAtm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "atm");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mHospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "hospital");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "doctor");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mPetrolBunk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "gas_station");
                            startActivity(banksIntent);
                        }else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });
            mPolice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent banksIntent = new Intent(NearByPlacesActivity.this, NearByPlacesListActivity.class);
                            banksIntent.putExtra("Map", "NearByPlaces");
                            banksIntent.putExtra("PlaceName", "police");
                            startActivity(banksIntent);
                        } else {
                            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Please Enable your GPS");
                        }
                    } else {
                        MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
                    }

                }
            });

        loadSingleInstall1();
    }

    private void loadSingleInstall1() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtNearByPlaces2);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtNearByPlaces2);
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
//                Toast.makeText(NearByPlacesActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
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
                frameLayout = (FrameLayout) findViewById(R.id.adAtNearByPlaces1);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtNearByPlaces1);
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
//                Toast.makeText(NearByPlacesActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
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
    protected void onRestart() {
        super.onRestart();
//        if(isConnectedToInternet()){
//            if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
//                if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
//                    splashObj.mInterstitialAd.show();
//                }else {
//                    startActivity(new Intent(NearByPlacesActivity.this, OurAdActivity.class));
//                }
//            }
//        }else{
//            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
//        }
    }
    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        //we are connected to a network
        try {
            connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connected;
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
                        startActivity(new Intent(NearByPlacesActivity.this, OurAdActivity.class));
                    }
                }
            }

        }else{
            MyDynamicToast.informationMessage(NearByPlacesActivity.this, "Check your internet connection..");
        }
    }


}
