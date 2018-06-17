package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

/**
 * Created by ABC on 4/23/2018.
 */

class NearByPlacesList {

    private String name;
    private String address, placeType,vicinity, distance;


    public NearByPlacesList(String name, String address, String placeType, String vicinity, String distance) {
        this.name = name;
        this.address = address;
        this.placeType = placeType;
        this.vicinity = vicinity;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    //    private Image image;





}
