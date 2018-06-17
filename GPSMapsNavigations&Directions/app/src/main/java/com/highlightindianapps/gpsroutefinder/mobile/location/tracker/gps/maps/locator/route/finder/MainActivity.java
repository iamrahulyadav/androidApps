package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateContentAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private CardView mMycurrentLocation, mGetDirections, mLocationHistory,
            mNearByPlaces,mTrafficAreas,mCompass,mWhetherReporst,mRoomTemprature, mKnowAddress;
    GPSTracker gps;
    private LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String settingsResult;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private InterstitialAd interstitialAd;

    private boolean servieStarted;

    private boolean accepted;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd() != null) {
                if (StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")) {
                    if (splashObj != null && splashObj.mInterstitialAd.isLoaded()) {
                        splashObj.mInterstitialAd.show();
                    } else {
                        startActivity(new Intent(MainActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
        }
//        Add
        loadSingleInstall();
        loadSingleInstall1();
//
        buildGoogleApiClient();
//
        mGoogleApiClient.connect();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMycurrentLocation = (CardView) findViewById(R.id.current_location);
        mKnowAddress = (CardView) findViewById(R.id.know_address);
        mCompass = (CardView) findViewById(R.id.compass);
        mNearByPlaces = (CardView) findViewById(R.id.near_by_places);
        mGetDirections = (CardView) findViewById(R.id.get_directions);
        mLocationHistory = (CardView) findViewById(R.id.location_history);
        mWhetherReporst = (CardView) findViewById(R.id.whether_reports);


        if (checkPermission()) {
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (!checkServiceRunning()) {
                    Intent i = new Intent(this, LocationUpdaterService.class);
                    startService(i);
                }
            }
        }


//        Whether Reports
        mWhetherReporst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "whether";
                if (checkPermission()) {
                    if(isConnectedToInternet()){
                        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent traficAreas = new Intent(MainActivity.this, WhetherReportsActivity.class);
//                                      traficAreas.putExtra("Map","traffic_areas");
                            startActivity(traficAreas);
                        } else {
                            settingsrequest();
                        }
                    }else{
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if(isConnectedToInternet()){
                            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent traficAreas = new Intent(MainActivity.this, WhetherReportsActivity.class);
//                                      traficAreas.putExtra("Map","traffic_areas");
                                startActivity(traficAreas);
                            } else {
                                settingsrequest();
                            }
                        }else{
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }

            }
        });
//        Location History
        mLocationHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "location_history";
                if (checkPermission()) {
                    if (isConnectedToInternet()) {
                        mSharedPreferences = getSharedPreferences("locationHistory", MainActivity.MODE_PRIVATE);
                        String locHis = mSharedPreferences.getString("locationHistory", "");
                        if (locHis != null) {
                            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent locaitonHistoryIntet = new Intent(MainActivity.this, LocationHistory.class);
//                                       directionsIntent.putExtra("Map","get_directions");
                                startActivity(locaitonHistoryIntet);
                            } else {
                                settingsrequest();
                            }
                        } else {
                            MyDynamicToast.informationMessage(MainActivity.this, "Location History is not Yet Registered..");
                        }

                    } else {
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }

                } else {
                    requestPermission();
                    if (accepted) {
                        if (isConnectedToInternet()) {
                            mSharedPreferences = getSharedPreferences("locationHistory", MainActivity.MODE_PRIVATE);
                            String locHis = mSharedPreferences.getString("locationHistory", "");
                            if (locHis != null) {
                                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    Intent locaitonHistoryIntet = new Intent(MainActivity.this, LocationHistory.class);
//                                       directionsIntent.putExtra("Map","get_directions");
                                    startActivity(locaitonHistoryIntet);
                                } else {
                                    settingsrequest();
                                }
                            } else {
                                MyDynamicToast.informationMessage(MainActivity.this, "Location History is not Yet Registered..");
                            }
                        } else {
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }
            }
        });
//        Get Directions
        mGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "get_directions";
                if (checkPermission()) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent directionsIntent = new Intent(MainActivity.this, DirectionsActivity.class);
                            directionsIntent.putExtra("Map", "get_directions");
                            startActivity(directionsIntent);
                        } else {
                            settingsrequest();
                        }
                    } else {
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if (isConnectedToInternet()) {
                            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent directionsIntent = new Intent(MainActivity.this, DirectionsActivity.class);
                                directionsIntent.putExtra("Map", "get_directions");
                                startActivity(directionsIntent);
                            } else {
                                settingsrequest();
                            }
                        } else {
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }
            }
        });
//        Current Location
        mMycurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "my_current_location";
                if (checkPermission()) {
                    if (isConnectedToInternet()) {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                            mapsIntent.putExtra("Map", "current_location");
                            startActivity(mapsIntent);
                        } else {
                            settingsrequest();
                        }
                    } else {
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if (isConnectedToInternet()) {
                            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                                mapsIntent.putExtra("Map", "current_location");
                                startActivity(mapsIntent);
                            } else {
                                settingsrequest();
                            }
                        } else {
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }

            }
        });
//        Know Address
        mKnowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "knowAddress";
                if (checkPermission()) {
                    if(isConnectedToInternet()){
                        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                            mapsIntent.putExtra("Map","know_address");
                            startActivity(mapsIntent);
                        } else {
                            settingsrequest();
                        }
                    }else{
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if(isConnectedToInternet()){
                            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                                mapsIntent.putExtra("Map","know_address");
                                startActivity(mapsIntent);
                            } else {
                                settingsrequest();
                            }
                        }else{
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }


            }
        });
//        Compass
        mCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "compass";
                if (checkPermission()) {
                    if(isConnectedToInternet()){
                        PackageManager manager = getPackageManager();
                        boolean hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
                        if (hasAccelerometer) {
                            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                                startActivity(compassIntent);
                            } else {
                                settingsrequest();
                            }
                        } else {
                            MyDynamicToast.informationMessage(MainActivity.this, "Your Device does not support this feature..");

                        }

                    }else{
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if(isConnectedToInternet()){
                            PackageManager manager = getPackageManager();
                            boolean hasAccelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
                            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasAccelerometer) {
                                Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                                startActivity(compassIntent);
                            } else {
                                settingsrequest();
                            }
                        }else{
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }


            }
        });
//        Near By Places
        mNearByPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsResult = "nearByPlaces";
                if (checkPermission()) {
                    if(isConnectedToInternet()){
                        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent nearByplacesIntent = new Intent(MainActivity.this, NearByPlacesActivity.class);
                            startActivity(nearByplacesIntent);
                        } else {
                            settingsrequest();
                        }
                    }else{
                        MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                    }
                } else {
                    requestPermission();
                    if (accepted) {
                        if(isConnectedToInternet()){
                            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent nearByplacesIntent = new Intent(MainActivity.this, NearByPlacesActivity.class);
                                startActivity(nearByplacesIntent);
                            } else {
                                settingsrequest();
                            }
                        }else{
                            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
                        }
                    }
                }


            }
        });
    }
    private void loadSingleInstall1() {
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
//                Toast.makeText(MainActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();


        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadSingleInstall() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) findViewById(R.id.adAtMain);
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
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.adAtMain);
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
//                Toast.makeText(MainActivity.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();


        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
    }
    private void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();

                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            mProgressDialog = new ProgressDialog(MainActivity.this);
                            mProgressDialog.setMessage("Please Wait");
                            mProgressDialog.show();
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (settingsResult.equals("my_current_location")) {
                            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                            mapsIntent.putExtra("Map","current_location");
                            mProgressDialog.dismiss();
                            startActivity(mapsIntent);

                        } else if (settingsResult.equals("knowAddress")) {
                            Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                            mapsIntent.putExtra("Map","know_address");
                            mProgressDialog.dismiss();
                            startActivity(mapsIntent);

                        } else if (settingsResult.equals("compass")) {
                            Intent compassIntent = new Intent(MainActivity.this, CompassActivity.class);
                            mProgressDialog.dismiss();
                            startActivity(compassIntent);

                        } else if (settingsResult.equals("nearByPlaces")) {
                            Intent nearByPlacesIntent = new Intent(MainActivity.this, NearByPlacesActivity.class);
                            mProgressDialog.dismiss();
                            startActivity(nearByPlacesIntent);

                        } else if (settingsResult.equals("get_directions")) {
                            Intent nearByPlacesIntent = new Intent(MainActivity.this, DirectionsActivity.class);
                            mProgressDialog.dismiss();
                            startActivity(nearByPlacesIntent);

                        }else if (settingsResult.equals("location_history")) {
                            Intent locaitonHistoryIntet = new Intent(MainActivity.this, LocationHistory.class);
                            mProgressDialog.dismiss();
                            startActivity(locaitonHistoryIntet);

                        }else if (settingsResult.equals("traffic_areas")) {
                            Intent trafficAreas = new Intent(MainActivity.this, MapsActivity.class);
                            trafficAreas.putExtra("Map","traffic_areas");
                            mProgressDialog.dismiss();
                            startActivity(trafficAreas);

                        }else if (settingsResult.equals("whether")) {
                            Intent trafficAreas = new Intent(MainActivity.this, WhetherReportsActivity.class);
                            mProgressDialog.dismiss();
                            startActivity(trafficAreas);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        mProgressDialog.dismiss();
                        break;
                }
                break;
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

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted) {
                        accepted = true;
                        if (!checkServiceRunning())  {
                            Intent i = new Intent(this, LocationUpdaterService.class);
                            startService(i);
                        }
                        Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();

                    }
                    else {

                        Toast.makeText(MainActivity.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.LocationUpdaterService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

}

