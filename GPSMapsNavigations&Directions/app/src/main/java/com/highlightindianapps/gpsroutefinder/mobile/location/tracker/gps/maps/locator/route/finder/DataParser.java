package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

class DataParser {
    private String jDistance = null;
    private String jTime = null;
    private String startAddress, endAddress, realTime;

    Date finalDate;
    String Degrees = null, date="null", prevouesDay="null";
    String WhetherType = null, city="null";
    Boolean todayFirst = false, day2First = false, day3First = false,day4First =false, day5First = false;
    @SuppressLint("SimpleDateFormat")

    public HashMap<String,String> parseWhetherReports(String s) {
        HashMap<String, String> getWhetherReport = new HashMap<>();
        JSONArray jsonArrayTemp = null;
        JSONArray jsonArrayWhether = null;
        JSONObject jsonObject;

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(today);
//
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day2 = calendar.getTime();
         SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String Date2 = df.format(day2);

//
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day3 = calendar.getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String Date3 = df.format(day3);
//
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day4 = calendar.getTime();
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        String Date4 = df.format(day4);
//
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date day5 = calendar.getTime();
        SimpleDateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");
        String Date5 = df.format(day5);

        try {
            jsonObject = new JSONObject((String) s);
            jsonArrayTemp = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArrayTemp.length(); i++) {
                realTime = ((JSONObject) jsonArrayTemp.get(i))
                        .getString("dt_txt").substring(11,16);
                date = ((JSONObject) jsonArrayTemp.get(i))
                        .getString("dt_txt").substring(0,10);
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Date dt1=dateFormat.parse(date);
                DateFormat dayFormat=new SimpleDateFormat("EEEE");
                String finalDay=dayFormat.format(dt1).substring(0,3);

                if (date.equals(todaysDate) && (!todayFirst)) {
                    todayFirst = true;
                    Degrees = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONObject("main")
                            .getString("temp");
                    WhetherType = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    city = jsonObject.getJSONObject("city").getString("name");
                    getWhetherReport.put("degree1",Degrees);
                    getWhetherReport.put("city1",city);
                    getWhetherReport.put("date1",date.substring(5));
                    getWhetherReport.put("day1",finalDay);
                    getWhetherReport.put("WhetherType1",WhetherType);

                } else if (date.equals(Date2) && (!day2First) && realTime.equals("09:00")) {
                    day2First = true;

                    Degrees = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONObject("main")
                            .getString("temp");
                    WhetherType = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    city = jsonObject.getJSONObject("city").getString("name");
                    getWhetherReport.put("degree2",Degrees);
                    getWhetherReport.put("date2",date.substring(5));
                    getWhetherReport.put("day2",finalDay);
                    getWhetherReport.put("WhetherType2",WhetherType);
                } else if (date.equals(Date3) && (!day3First) && realTime.equals("09:00")) {
                    day3First = true;

                    Degrees = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONObject("main")
                            .getString("temp");
                    WhetherType = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    city = jsonObject.getJSONObject("city").getString("name");
                    getWhetherReport.put("degree3",Degrees);
                    getWhetherReport.put("date3",date.substring(5));
                    getWhetherReport.put("day3",finalDay);
                    getWhetherReport.put("WhetherType3",WhetherType);
                } else if (date.equals(Date4) && (!day4First) && realTime.equals("09:00")) {
                    day4First = true;

                    Degrees = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONObject("main")
                            .getString("temp");
                    WhetherType = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    city = jsonObject.getJSONObject("city").getString("name");
                    getWhetherReport.put("degree4",Degrees);
                    getWhetherReport.put("date4",date.substring(5));
                    getWhetherReport.put("day4",finalDay);
                    getWhetherReport.put("WhetherType4",WhetherType);
                } else if (date.equals(Date5) && (!day5First) && realTime.equals("09:00")) {
                    day5First = true;
                    Degrees = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONObject("main")
                            .getString("temp");
                    WhetherType = ((JSONObject) jsonArrayTemp.get(i))
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    city = jsonObject.getJSONObject("city").getString("name");
                    getWhetherReport.put("degree5",Degrees);
                    getWhetherReport.put("date5",date.substring(5));
                    getWhetherReport.put("day5",finalDay);
                    getWhetherReport.put("WhetherType5",WhetherType);
                }
            }


        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getWhetherReport;
    }

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try {
            if (!googlePlaceJson.isNull("name")) {

                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");

            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");


            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("latitude", latitude);
            googlePlacesMap.put("longitude", longitude);
            googlePlacesMap.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;
    }


    public List<List<HashMap<String,String>>> parseDirections(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            jRoutes = jObject.getJSONArray("routes");

            if (jRoutes.length() > 0) {


            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance").getString("text");
                    jTime = ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text");
                    startAddress = ((JSONObject) jLegs.get(j)).getString("start_address");
                    endAddress = ((JSONObject) jLegs.get(j)).getString("end_address");
                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            hm.put("distance", jDistance);
                            hm.put("duration", jTime);
                            hm.put("start_address", startAddress);
                            hm.put("end_address", endAddress);
                            path.add(hm);
                        }
                    }
                    routes.add(path);
//                    routes.add(jDistance);
                }
            }
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }
    private List<LatLng> decodePoly(String polyline) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }



}
