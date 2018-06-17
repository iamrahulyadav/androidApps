package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

class GetWhetherReports extends AsyncTask<Object, String, String > {
    private String whetherReports;
    private String url;
    private Context context;

    private TextView mDegrees1,mWhetherType1,mArea1;
    private ImageView whetherTypeImage;
    private LinearLayout linearLayout;

    private ProgressDialog progressDialog;
    private TextView day1,day2, day3, day4, day5, temp1,temp2, temp3, temp4, temp5, date1,date2, date3, date4, date5;

    private ImageView day1WhetherTypeImage, day2WhetherTypeImage, day3WhetherTypeImage, day4WhetherTypeImage, day5WhetherTypeImage;
    @Override
    protected String doInBackground(Object... objects) {

        url = (String) objects[0];
        context = (Context) objects[1];

        mDegrees1 = (TextView) objects[2];
        mWhetherType1 = (TextView) objects[3];
        mArea1 = (TextView) objects[4];
        whetherTypeImage = (ImageView) objects[5];
        linearLayout = (LinearLayout) objects[6];


        day1 = (TextView) objects[24];
        day2 = (TextView) objects[7];
        day3 = (TextView) objects[8];
        day4 = (TextView) objects[9];
        day5 = (TextView) objects[10];

        temp1 = (TextView) objects[25];
        temp2 = (TextView) objects[11];
        temp3 = (TextView) objects[12];
        temp4 = (TextView) objects[13];
        temp5 = (TextView) objects[14];

        date1 = (TextView) objects[23];
        date2 = (TextView) objects[15];
        date3 = (TextView) objects[16];
        date4 = (TextView) objects[17];
        date5 = (TextView) objects[18];

        day1WhetherTypeImage = (ImageView) objects[26];
        day2WhetherTypeImage = (ImageView) objects[19];
        day3WhetherTypeImage = (ImageView) objects[20];
        day4WhetherTypeImage = (ImageView) objects[21];
        day5WhetherTypeImage = (ImageView) objects[22];

        progressDialog = (ProgressDialog) objects[27];


        DownloadUrl downloadUrl = new DownloadUrl();

        try {

            whetherReports = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return whetherReports;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            HashMap<String, String> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList =  dataParser.parseWhetherReports(s);
            ShowWhetherReports(nearbyPlacesList);
        } else {
            MyDynamicToast.informationMessage(context, "Please Check Your Internet Connection..");
            progressDialog.dismiss();
        }
    }

    private void ShowWhetherReports(HashMap<String, String> nearbyPlacesList) {
        String degree, date;
        double degrees;
        String WhetherType, day;
        degree = nearbyPlacesList.get("degree1");
        degrees = Double.valueOf(degree).doubleValue();
        degrees = convertToCelsius(degrees);
        WhetherType = nearbyPlacesList.get("WhetherType1");
        String city = nearbyPlacesList.get("city1");
        day = nearbyPlacesList.get("day1");
        date = nearbyPlacesList.get("date1");
        mDegrees1.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        mWhetherType1.setText(WhetherType);
        day1.setText(day);
        if(WhetherType.equals("light rain") ) {
            whetherTypeImage.setImageResource(R.drawable.light_rain);
            day1WhetherTypeImage.setImageResource(R.drawable.light_rain);
//            linearLayout.setBackgroundResource(R.drawable.lite_rain_background);
        } else if (WhetherType.equals("few clouds")) {
            whetherTypeImage.setImageResource(R.drawable.few_clouds);
            day1WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        } else if (WhetherType.equals("scattered clouds")) {
            whetherTypeImage.setImageResource(R.drawable.scattered_clouds);
            day1WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if (WhetherType.equals("clear sky")) {
            whetherTypeImage.setImageResource(R.drawable.clear_sky);
            day1WhetherTypeImage.setImageResource(R.drawable.clear_sky);
        }else if (WhetherType.equals("broken clouds")) {
            whetherTypeImage.setImageResource(R.drawable.scattered_clouds);
            day1WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if (WhetherType.equals("overcast clouds")) {
            whetherTypeImage.setImageResource(R.drawable.few_clouds);
            day1WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }
        mArea1.setText(city);
        temp1.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        date1.setText(date.replace("-","/"));
//
        degree = nearbyPlacesList.get("degree2");
        degrees = Double.valueOf(degree).doubleValue();
        degrees = convertToCelsius(degrees);
        WhetherType = nearbyPlacesList.get("WhetherType2");
        day = nearbyPlacesList.get("day2");
        date = nearbyPlacesList.get("date2");
        temp2.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        day2.setText(day);
        date2.setText(date.replace("-","/"));
        if(WhetherType.equals("light rain") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.light_rain);
        }else if(WhetherType.equals("few clouds") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }else if(WhetherType.equals("scattered clouds") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("clear sky") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.clear_sky);
        }else if(WhetherType.equals("broken clouds") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("overcast clouds") ) {
            day2WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }
//
        degree = nearbyPlacesList.get("degree3");
        degrees = Double.valueOf(degree).doubleValue();
        degrees = convertToCelsius(degrees);
        WhetherType = nearbyPlacesList.get("WhetherType3");
        day = nearbyPlacesList.get("day3");
        date = nearbyPlacesList.get("date3");
        temp3.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        day3.setText(day);
        date3.setText(date.replace("-","/"));
        if(WhetherType.equals("light rain") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.light_rain);
        }else if(WhetherType.equals("few clouds") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }else if(WhetherType.equals("scattered clouds") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("clear sky") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.clear_sky);
        }else if(WhetherType.equals("broken clouds") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("overcast clouds") ) {
            day3WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }
//
        degree = nearbyPlacesList.get("degree4");
        degrees = Double.valueOf(degree).doubleValue();
        degrees = convertToCelsius(degrees);
        WhetherType = nearbyPlacesList.get("WhetherType4");
        day = nearbyPlacesList.get("day4");
        date = nearbyPlacesList.get("date4");
        temp4.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        day4.setText(day);
        date4.setText(date.replace("-","/"));
        if(WhetherType.equals("light rain") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.light_rain);
        }else if(WhetherType.equals("few clouds") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }else if(WhetherType.equals("scattered clouds") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("clear sky") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.clear_sky);
        }else if(WhetherType.equals("broken clouds") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("overcast clouds") ) {
            day4WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }

        degree = nearbyPlacesList.get("degree5");
        degrees = Double.valueOf(degree).doubleValue();
        degrees = convertToCelsius(degrees);
        WhetherType = nearbyPlacesList.get("WhetherType5");
        day = nearbyPlacesList.get("day5");
        date = nearbyPlacesList.get("date5");
        temp5.setText(new DecimalFormat("##.##").format(degrees) + "\u2103");
        day5.setText(day);
        date5.setText(date.replace("-","/"));
        if(WhetherType.equals("light rain") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.light_rain);
        }else if(WhetherType.equals("few clouds") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }else if(WhetherType.equals("scattered clouds") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("clear sky") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.clear_sky);
        }else if(WhetherType.equals("broken clouds") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.scattered_clouds);
        }else if(WhetherType.equals("overcast clouds") ) {
            day5WhetherTypeImage.setImageResource(R.drawable.few_clouds);
        }

        progressDialog.dismiss();


    }

    private double convertToCelsius(double degrees){
        double celsius, fahrenhiet, kelvin;
        kelvin = degrees;
        celsius = kelvin - 273.15;
//        System.out.println ("\n" + kelvin + "K = "+ celsius + "C");
        fahrenhiet = (celsius * 9.0/5.0) + 32.0;
        System.out.println ("\n" + celsius + "C = ");
        return  celsius;
    }

}
