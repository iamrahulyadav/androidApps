package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

/**
 * Created by ABC on 4/18/2018.
 */

public class LocationHistoryStorage {
    public String address;
    public String mTime;


    public LocationHistoryStorage(String address, String mTime) {
        this.address = address;
        this.mTime = mTime;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
