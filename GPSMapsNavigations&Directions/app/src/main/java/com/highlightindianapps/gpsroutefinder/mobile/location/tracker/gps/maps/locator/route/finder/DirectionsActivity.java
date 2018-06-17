package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class DirectionsActivity extends AppCompatActivity {

    private String startAddress, destinationAddress;
    private Button mGetDirections;
    AlertDialog alertDialog1;
    CharSequence[] values = {" Car "," Bike ", " Bus", " Walk "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        mGetDirections = (Button) findViewById(R.id.get_directions);

        PlaceAutocompleteFragment startAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_start_address_fragment);
        startAddressFragment.setHint("From Address");
        AutocompleteFilter mStartAddressAutoComplete = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        startAddressFragment.setFilter(mStartAddressAutoComplete);
        startAddressFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startAddress = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(DirectionsActivity.this, " " +status, Toast.LENGTH_SHORT).show();
            }
        });


        PlaceAutocompleteFragment destinationAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_end_address_fragment);
        destinationAddressFragment.setHint("To Addresss");
        AutocompleteFilter mDestinationAddressComplete = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        destinationAddressFragment.setFilter(mDestinationAddressComplete);
        destinationAddressFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
             destinationAddress = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(DirectionsActivity.this, " " + status, Toast.LENGTH_SHORT).show();
            }
        });

        mGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                    CreateAlertDialogWithRadioButtonGroup();
                } else {
                    Toast.makeText(DirectionsActivity.this, "Please Enter Start Address and Destination Address ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateAlertDialogWithRadioButtonGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);

        builder.setTitle("Select Your Type of Travel");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Intent mapsIntent = new Intent(DirectionsActivity.this, MapsActivity.class);

                if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                    if (startAddress.contains(", ")) {
                        startAddress = startAddress.replace(", ", ",");
                    }
                    startAddress = startAddress.replace(" ", ",");
                    if (destinationAddress.contains(", ")) {
                        destinationAddress = destinationAddress.replace(", ", ",");
                    }
                    destinationAddress = destinationAddress.replace(" ", ",");
                }


                switch(item)
                {
                    case 0:
                        if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                            String travelType = "driving";
                            mapsIntent.putExtra("Map", "directions");
                            mapsIntent.putExtra("startAddress", startAddress);
                            mapsIntent.putExtra("destinationAddress", destinationAddress);
                            mapsIntent.putExtra("travelType", "driving");
                            startActivity(mapsIntent);
                        } else {
                            Toast.makeText(DirectionsActivity.this, "Please Enter Start Address and Destination Address ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                            mapsIntent.putExtra("Map", "directions");
                            mapsIntent.putExtra("startAddress", startAddress);
                            mapsIntent.putExtra("destinationAddress", destinationAddress);
                            mapsIntent.putExtra("travelType", "driving");
                            startActivity(mapsIntent);
                        } else {
                            Toast.makeText(DirectionsActivity.this, "Please Enter Start Address and Destination Address ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                            mapsIntent.putExtra("Map", "directions");
                            mapsIntent.putExtra("startAddress", startAddress);
                            mapsIntent.putExtra("destinationAddress", destinationAddress);
                            mapsIntent.putExtra("travelType", "bus");
                            startActivity(mapsIntent);
                        } else {
                            Toast.makeText(DirectionsActivity.this, "Please Enter Start Address and Destination Address ", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        if (!TextUtils.isEmpty(startAddress) && !TextUtils.isEmpty(destinationAddress)) {
                            mapsIntent.putExtra("Map", "directions");
                            mapsIntent.putExtra("startAddress", startAddress);
                            mapsIntent.putExtra("destinationAddress", destinationAddress);
                            mapsIntent.putExtra("travelType", "walking");
                            startActivity(mapsIntent);
                        } else {
                            Toast.makeText(DirectionsActivity.this, "Please Enter Start Address and Destination Address ", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

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
                        startActivity(new Intent(DirectionsActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(DirectionsActivity.this, "Check your internet connection..");
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
