package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ABC on 4/16/2018.
 */

class GetNearbyPlacesData extends AsyncTask<Object, String, String > implements GoogleMap.OnMarkerClickListener{
    private String googlePlacesData, placeType, address;
    private ProgressDialog progressDialog;
    private String url;
    private GoogleMap mMap;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<NearByPlacesList> nearByPlacesLists;
    double latitude, longitude;

    boolean adLoaded, dataLoaded;

    public static final int NUMBER_OF_ADS = 5;
    private List<NativeAd> mNativeAds = new ArrayList<>();

    List<Object> mRecyclerViewItems = new ArrayList<>();



    private void loadNativeAd() {
        loadNativeAd(0);
    }
    private void loadNativeAd(final int adLoadCount) {

        if (adLoadCount >= NUMBER_OF_ADS) {
            adLoaded = true;
            insertAdsInMenuItems();
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(context, "ca-app-pub-4445981284747506/6783416261");
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
            adapter = new MyAdapter(mRecyclerViewItems, context);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
    @Override
    protected String doInBackground(Object... objects) {

        recyclerView = (RecyclerView) objects[0];
        url = (String) objects[1];
        context = (Context) objects[2];
        placeType = (String) objects[3];
        address = (String) objects[4];
        progressDialog = (ProgressDialog) objects[5];
        latitude = (double) objects[6];
        longitude = (double) objects[7];
        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            return null;
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            loadNativeAd();
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList =  dataParser.parse(s);
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        } else {
            MyDynamicToast.informationMessage(context, "Poor internet connectivity..");
            progressDialog.dismiss();
        }


    }


    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        nearByPlacesLists = new ArrayList<>();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {

            Log.d("onPostExecute","Entered into showing locations");
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            double lat = Double.parseDouble(googlePlace.get("latitude"));
            double lng = Double.parseDouble(googlePlace.get("longitude"));

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);


            float distance = distance((float)latitude, (float)longitude, (float)lat, (float)lng);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            float twoDigitsF = Float.valueOf(decimalFormat.format(distance));
            NearByPlacesList student = new NearByPlacesList(
                    placeName , address,placeType, vicinity, Float.toString(twoDigitsF) + "m");


            mRecyclerViewItems.add(student);
        }

        dataLoaded = true;
        insertAdsInMenuItems();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();

        return false;
    }

    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
