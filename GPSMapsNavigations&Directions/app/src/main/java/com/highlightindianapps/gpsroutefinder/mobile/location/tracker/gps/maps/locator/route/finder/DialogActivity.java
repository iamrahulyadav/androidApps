package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class DialogActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        this.setFinishOnTouchOutside(false);
        mToolbar = (Toolbar) findViewById(R.id.dialogpage_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search For Address");
        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MapsActivity.placeNameSearch != null) {
                    finish();
                } else {
                    Toast.makeText(DialogActivity.this, "Plese Enter Required Address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        PlaceAutocompleteFragment startAddressFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.places_search);
        startAddressFragment.setHint("Enter Place Name");
        AutocompleteFilter mStartAddressAutoComplete = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        startAddressFragment.setFilter(mStartAddressAutoComplete);
        startAddressFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                MapsActivity.placeNameSearch = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(DialogActivity.this, " " +status, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
