package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ABC on 4/17/2018.
 */

class GetDirections extends AsyncTask<Object, String, String> {
    private CameraUpdate cameraPosition;
    private RelativeLayout relativeLayout;
    private LatLng startPostion, destinationPosition;
    GoogleMap mMap;
    String googleDirectionsData;
    private String distance, duration;
    String url;
    Context context;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        context  = (Context)objects[2];
        relativeLayout = (RelativeLayout) objects[3];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(s);
        } else {
            MyDynamicToast.informationMessage(context, "Please Check Your Internet Connection..");
        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                // Starts parsing data
                routes = parser.parseDirections(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            if (result != null) {

                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        distance = point.get("distance");
                        duration = point.get("duration");
                        LatLng position = new LatLng(lat, lng);

                        if (j == 0) {
                            startPostion = position;
                            markerOptions.position(position);
                            markerOptions.title(point.get("start_address"));
                            markerOptions.getInfoWindowAnchorV();
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            mMap.addMarker(markerOptions);
                        }
                        if (j == path.size() - 1) {
                            destinationPosition = position;
                            markerOptions.position(position);
                            markerOptions.getInfoWindowAnchorV();
                            markerOptions.title(point.get("end_address"));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            mMap.addMarker(markerOptions);
                        }
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                }
                mMap.addPolyline(lineOptions);
                cameraPosition = CameraUpdateFactory.newLatLngZoom(startPostion,13);
                mMap.moveCamera(cameraPosition);

                Snackbar.make(relativeLayout, "Distance"+distance +"Time"+ duration, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(relativeLayout, "No Directions Available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            // Drawing polyline in the Google Map for the i-th route

        }
    }



}
