package com.highlightindianapps.learnenglishfromhindi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by windows on 6/9/2017.
 */

public class DataHandler {
    private static final DataHandler ourInstance = new DataHandler();
    public final static String RATING_PREFERENCES = "ratingpreferences";

    public static DataHandler getInstance() {
        return ourInstance;
    }

    private DataHandler() {
    }

    boolean isRatingDone(Context context){

        SharedPreferences sharedpreferences = context.getSharedPreferences(RATING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return sharedpreferences.getBoolean("rate", false);
    }
    void setRatingDone(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(RATING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("rate", true);
        editor.commit();
    }
}
