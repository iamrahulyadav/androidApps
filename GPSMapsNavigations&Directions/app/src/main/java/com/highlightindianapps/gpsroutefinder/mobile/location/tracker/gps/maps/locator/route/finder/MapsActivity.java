package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng latLng;
    private LocationRequest mLocationRequest;
    private Marker currLocationMarker;
    private CameraUpdate cameraPosition;
    private LocationManager mLocationManager;
    private RelativeLayout mRelativeLayout;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;

    private CardView directionsCard;
    private TextView directionsText;
    public static String placeNameSearch = null;

    private String intentValue;
    private RadioButton mNormalViewBtn, mSatelliteViewBtn, mHybridViewBtn, mTrafficViewBtn;
    private FloatingActionButton mPlacesSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        directionsCard = (CardView) findViewById(R.id.directions_card);
        directionsText = (TextView) findViewById(R.id.directions_text);
        mPlacesSearchBtn = (FloatingActionButton) findViewById(R.id.places_search);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mNormalViewBtn = (RadioButton) findViewById(R.id.normal_view_btn);
        mSatelliteViewBtn = (RadioButton) findViewById(R.id.satellite_view_btn);
        mHybridViewBtn = (RadioButton) findViewById(R.id.hybrid_view_btn);
        mTrafficViewBtn = (RadioButton) findViewById(R.id.traffic_radio_btn);


        mPlacesSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeNameSearch = null;
                Intent intent = new Intent(MapsActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        intentValue = intent.getStringExtra("Map");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(MapsActivity.this)
                .addOnConnectionFailedListener(MapsActivity.this)
                .addApi(LocationServices.API)
                .build();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mNormalViewBtn.setChecked(true);
        if (intentValue.equals("know_address")) {
            mPlacesSearchBtn.setVisibility(View.VISIBLE);
            Intent intent = new Intent(MapsActivity.this, DialogActivity.class);
            startActivity(intent);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(point.latitude, point.longitude)).title(address);
                        mMap.clear();
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        currLocationMarker = mMap.addMarker(marker);
                        currLocationMarker.showInfoWindow();

                    } else {
                        MyDynamicToast.informationMessage(MapsActivity.this, "No Address Found");
                    }

                }
            });
        }
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mMap.setMyLocationEnabled(true);

        } else {
            mMap.setMyLocationEnabled(false);
        }
        mMap.setBuildingsEnabled(true);


        buildGoogleApiClient();
        mGoogleApiClient.connect();

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAmD2mvXCwmXSfyQCGT4gmHR3XYWINCu1s");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();

        if (intentValue.equals("know_address")) {
//            latLng = new LatLng(28.00, 77.00);
//            Geocoder geocoder;
//            List<Address> addresses = null;
//            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
//            try {
//                addresses = geocoder.getFromLocation(28.00, 77.00, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (addresses.size() > 0) {
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//                markerOptions.title(address);
//                markerOptions.position(latLng);
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                currLocationMarker = mMap.addMarker(markerOptions);
//                currLocationMarker.showInfoWindow();
//                cameraPosition = CameraUpdateFactory.newLatLngZoom(latLng,0);
//                mMap.moveCamera(cameraPosition);
//                Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
//            }
        } else {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                markerOptions.position(latLng);
                markerOptions.title("Current Position.......");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                currLocationMarker = mMap.addMarker(markerOptions);
                currLocationMarker.showInfoWindow();
                cameraPosition = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                if (intentValue.equals("traffic_areas")) {
                    mMap.setTrafficEnabled(true);
                }
                mMap.moveCamera(cameraPosition);
                if (intentValue.equals("NearByPlaces")) {
                    Intent placename = getIntent();
                    String travelType = "bike";
                    String placeNames = placename.getStringExtra("placeType");
                    String destination = placename.getStringExtra("destination");
                    String startAddress = placename.getStringExtra("startAddress");
                    if (startAddress.contains(", ")) {
                        startAddress = startAddress.replace(", ", ",");
                    }
                    startAddress = startAddress.replace(" ", ",");
                    if (destination.contains(", ")) {
                        destination = destination.replace(", ", ",");
                    }
                    destination = destination.replace(" ", ",");
                    getDirection(startAddress, destination, travelType);
                } else if (intentValue.equals("directions")) {

                    Intent travelTypes = getIntent();
                    String travelType = travelTypes.getStringExtra("travelType");
                    String startAddress = travelTypes.getStringExtra("startAddress");
                    String destinationAddress = travelTypes.getStringExtra("destinationAddress");
                    getDirection(startAddress, destinationAddress, travelType);
                }
            }
    }

    private void getDirection(String startAddress, String destinationAddress, String travelType) {
        String directionsUrl = getDirectionsUrl(startAddress, destinationAddress, travelType);
        Object[] DataTransfer = new Object[4];
        DataTransfer[0] = mMap;
        DataTransfer[1] = directionsUrl;
        DataTransfer[2] = MapsActivity.this;
        DataTransfer[3] = mRelativeLayout;
        Log.d("onClick", directionsUrl);
        GetDirections getDirections = new GetDirections();
        getDirections.execute(DataTransfer);
    }

    private String getDirectionsUrl(String startAddress, String destinationAddress, String travelType) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+startAddress);
        googleDirectionsUrl.append("&destination="+destinationAddress);
        googleDirectionsUrl.append("&mode="+travelType);
        googleDirectionsUrl.append("&key=AIzaSyChvVz9DM0Rh-XEWPzoNAuPQ627NC2oefU");
        Log.d("getDirectoinsUrl", googleDirectionsUrl.toString());
        return (googleDirectionsUrl.toString());
    }

    private void nearByPlaces(double latitude, double longitude, String placeNames) {
            String url = getUrl(latitude, longitude, placeNames);
            Object[] DataTransfer = new Object[2];
            DataTransfer[0] = mMap;
            DataTransfer[1] = url;
            Log.d("onClick", url);
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.execute(DataTransfer);
}

    public void onRadioBtnClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.normal_view_btn:
                if (checked) {
                    if (mMap != null) {
                        mMap.setTrafficEnabled(false);
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        mSatelliteViewBtn.setChecked(false);
                        mHybridViewBtn.setChecked(false);
                        mTrafficViewBtn.setChecked(false);
                    }
                }
                    break;
            case R.id.satellite_view_btn:
                if (checked) {
                    if (mMap != null) {
                        mMap.setTrafficEnabled(false);
                        mNormalViewBtn.setChecked(false);
                        mHybridViewBtn.setChecked(false);
                        mTrafficViewBtn.setChecked(false);
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    }
                }
                    break;
                case R.id.hybrid_view_btn:
                if (checked) {
                    if (mMap != null) {
                        mMap.setTrafficEnabled(false);
                        mNormalViewBtn.setChecked(false);
                        mSatelliteViewBtn.setChecked(false);
                        mTrafficViewBtn.setChecked(false);
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
                    break;

                case R.id.traffic_radio_btn:
                if (checked) {
                    if (mMap != null) {
                        mMap.setTrafficEnabled(true);
                        mNormalViewBtn.setChecked(false);
                        mSatelliteViewBtn.setChecked(false);
                        mHybridViewBtn.setChecked(false);
                    }
                }
                    break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (placeNameSearch != null) {
            if (placeNameSearch.contains(", ")) {
                placeNameSearch = placeNameSearch.replace(", ", ",");
            }
            placeNameSearch = placeNameSearch.replace(" ", ",");
            getLatLang(placeNameSearch);
            placeNameSearch = null;
        }

    }

    private void getLatLang(String placeNameSearch) {
        MarkerOptions markerOptions = new MarkerOptions();

        Geocoder coder = new Geocoder(MapsActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(placeNameSearch, 5);
            if (address == null) {
                Toast.makeText(this, "" + address, Toast.LENGTH_SHORT).show();
            }

            if (address != null) {
                if (address.size() > 0) {
                    Address location = address.get(0);
                    p1 = new LatLng(location.getLatitude(), location.getLongitude() );

                }
            }


        } catch (IOException ex) {
            Toast.makeText(this, "There Is some Error While getting the Result", Toast.LENGTH_SHORT).show();

        }
        if (p1 != null) {
            if (mMap != null) {
                Marker marker;
                markerOptions.title(placeNameSearch);
                markerOptions.position(p1);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
                cameraPosition = CameraUpdateFactory.newLatLngZoom(p1,13);
                mMap.moveCamera(cameraPosition);
            }

        } else {
            Toast.makeText(this, "There Is some Error While getting the Result, Plese Try Again", Toast.LENGTH_SHORT).show();

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
                        startActivity(new Intent(MapsActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(MapsActivity.this, "Check your internet connection..");
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
