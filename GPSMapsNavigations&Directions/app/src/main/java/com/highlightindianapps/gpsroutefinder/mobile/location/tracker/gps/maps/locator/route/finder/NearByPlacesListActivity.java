package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class NearByPlacesListActivity extends AppCompatActivity {

    Intent intent;
    private String intentValue;
    private int PROXIMITY_RADIUS = 1000;

    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferencesLat, sharedPreferencesLang;
    private List<NearByPlacesList> nearByPlacesLists;
    private ProgressDialog progressDialog;

    private GPSTracker gps;
    double latitude, langitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places_list);

        progressDialog = new ProgressDialog(NearByPlacesListActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_for_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gps = new GPSTracker(NearByPlacesListActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            langitude = gps.getLongitude();
            intent = getIntent();
            intentValue = intent.getStringExtra("PlaceName");
            nearByPlaces(latitude, langitude, intentValue);
//
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Enable Your GPS", Toast.LENGTH_SHORT).show();
        }
    }


    private void nearByPlaces(double latitude, double longitude, String placeNames) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(NearByPlacesListActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            MyDynamicToast.informationMessage(NearByPlacesListActivity.this, "Check your internet connection..");
        }

        if (addresses != null && latitude != 0.0 && longitude != 0.0 ) {
            if (addresses.size() > 0) {
                final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                String url = getUrl(latitude, longitude, placeNames);
                Object[] DataTransfer = new Object[8];
                DataTransfer[0] = recyclerView;
                DataTransfer[1] = url;
                DataTransfer[3] = placeNames;
                DataTransfer[2] = NearByPlacesListActivity.this;
                DataTransfer[4] = address;
                DataTransfer[5] = progressDialog;
                DataTransfer[6] = latitude;
                DataTransfer[7] = longitude;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
            }
        } else {
            progressDialog.dismiss();
            finish();
            MyDynamicToast.informationMessage(NearByPlacesListActivity.this, "Unable to get your location Please try again..");
        }

    }

    private String getUrl(double latitude, double longitude, String placeNames) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        if (placeNames.equals("airport") || placeNames.equals("train_station")) {
            googlePlacesUrl.append("&radius=" + 10000);
        } else {
            googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        }
        googlePlacesUrl.append("&type=").append(placeNames);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAmD2mvXCwmXSfyQCGT4gmHR3XYWINCu1s");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
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
    public void onBackPressed() {
        super.onBackPressed();
        if(isConnectedToInternet()){
            if (StaticDataHandler.getInstance().getShowInterstitialAd() != null) {
                if (StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")) {
                    if (splashObj != null && splashObj.mInterstitialAd.isLoaded()) {
                        splashObj.mInterstitialAd.show();
                    } else {
                        startActivity(new Intent(NearByPlacesListActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(NearByPlacesListActivity.this, "Check your internet connection..");
        }
    }
}
