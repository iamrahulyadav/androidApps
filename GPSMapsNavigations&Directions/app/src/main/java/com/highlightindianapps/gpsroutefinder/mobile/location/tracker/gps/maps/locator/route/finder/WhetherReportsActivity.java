package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.SplashActivity.splashObj;

public class WhetherReportsActivity extends AppCompatActivity implements android.location.LocationListener{

    private ImageView mWhetherTypeImage;
    private TextView mDegrees,mArea, mWhetherType;

    private TextView day1,day2, day3, day4, day5, temp1, temp2, temp3, temp4, temp5, date1,date2, date3, date4, date5;
    private ImageView day1WhetherTypeImage,day2WhetherTypeImage, day3WhetherTypeImage, day4WhetherTypeImage, day5WhetherTypeImage;

    private LinearLayout linearLayout;
    private SharedPreferences sharedPreferencesLat, sharedPreferencesLang;
    double latitude, langitude;
    private String APIKEY = "750949f4313407b68fddb13717bf30e5";
    private String CITYID = "1269844";
    private ProgressDialog progressDialog;

    private GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whether_reports);

        progressDialog = new ProgressDialog(WhetherReportsActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        gps = new GPSTracker(WhetherReportsActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            langitude = gps.getLongitude();
        } else {
            Toast.makeText(WhetherReportsActivity.this, "Please Enable your GPS", Toast.LENGTH_SHORT).show();
        }

        linearLayout = (LinearLayout) findViewById(R.id.whether_layout);
        mDegrees = (TextView) findViewById(R.id.degrees);
        mWhetherType = (TextView) findViewById(R.id.whether_type);
        mArea = (TextView) findViewById(R.id.area);
        mWhetherTypeImage = (ImageView) findViewById(R.id.whether_type_image);

        day1 = (TextView) findViewById(R.id.today_day);
        day2 = (TextView) findViewById(R.id.day_2);
        day3 = (TextView) findViewById(R.id.day_3);
        day4 = (TextView) findViewById(R.id.day_4);
        day5 = (TextView) findViewById(R.id.day_5);
        temp1 = (TextView) findViewById(R.id.today_temp);
        temp2 = (TextView) findViewById(R.id.day_2_temp);
        temp3 = (TextView) findViewById(R.id.day_3_temp);
        temp4 = (TextView) findViewById(R.id.day_4_temp);
        temp5 = (TextView) findViewById(R.id.day_5_temp);

        date1 = (TextView) findViewById(R.id.today_date);
        date2 = (TextView) findViewById(R.id.day_2_date);
        date3 = (TextView) findViewById(R.id.day_3_date);
        date4 = (TextView) findViewById(R.id.day_4_date);
        date5 = (TextView) findViewById(R.id.day_5_date);

        day1WhetherTypeImage = (ImageView) findViewById(R.id.today_whether_image);
        day2WhetherTypeImage = (ImageView) findViewById(R.id.day_2_whether_image);
        day3WhetherTypeImage = (ImageView) findViewById(R.id.day_3_whether_image);
        day4WhetherTypeImage = (ImageView) findViewById(R.id.day_4_whether_image);
        day5WhetherTypeImage = (ImageView) findViewById(R.id.day_5_whether_image);


        String url = getUrl(APIKEY,CITYID);
        Object[] DataTransfer = new Object[28];
        DataTransfer[1] = WhetherReportsActivity.this;
        DataTransfer[0] = url;
        DataTransfer[2] = mDegrees;
        DataTransfer[3] = mWhetherType;
        DataTransfer[4] = mArea;
        DataTransfer[5] = mWhetherTypeImage;
        DataTransfer[6] = linearLayout;
        DataTransfer[7] = day2;
        DataTransfer[8] = day3;
        DataTransfer[9] = day4;
        DataTransfer[10] = day5;
        DataTransfer[11] = temp2;
        DataTransfer[12] = temp3;
        DataTransfer[13] = temp4;
        DataTransfer[14] = temp5;

        DataTransfer[15] = date2;
        DataTransfer[16] = date3;
        DataTransfer[17] = date4;
        DataTransfer[18] = date5;

        DataTransfer[19] = day2WhetherTypeImage;
        DataTransfer[20] = day3WhetherTypeImage;
        DataTransfer[21] = day4WhetherTypeImage;
        DataTransfer[22] = day5WhetherTypeImage;

        DataTransfer[23] = date1;
        DataTransfer[24] = day1;
        DataTransfer[25] = temp1;
        DataTransfer[26] = day1WhetherTypeImage;
        DataTransfer[27] = progressDialog;

        GetWhetherReports getWhetherReports = new GetWhetherReports();
        getWhetherReports.execute(DataTransfer);
    }
    private String getUrl(String apikey, String cityid) {
        StringBuilder googlePlacesUrl = new StringBuilder("http://api.openweathermap.org/data/2.5/forecast?");
        googlePlacesUrl.append("lat="+ latitude);
        googlePlacesUrl.append("&lon="+ langitude);
        googlePlacesUrl.append("&APPID=" + apikey);
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
                        startActivity(new Intent(WhetherReportsActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(WhetherReportsActivity.this, "Check your internet connection..");
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
