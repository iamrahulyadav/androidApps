package com.english.hindi.offline.dictionary.translate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import java.util.Random;

public class RatingActivity extends AppCompatActivity {

    Button ok ,cancel;
    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    TextView ratingText;
    ImageView cancelIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingText = (TextView) findViewById(R.id.rating_text);
        cancelIcon = (ImageView) findViewById(R.id.cancel_action);
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] array = getApplicationContext().getResources().getStringArray(R.array.ratings_array);
        String randomStr = array[new Random().nextInt(array.length)];
        ratingText.setText(randomStr);
        ratingText.setText(randomStr);
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);
        setFinishOnTouchOutside(false);

        if(ok != null){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(isConnectedToInternet()) {
                    DataHandler.getInstance().setRatingDone(RatingActivity.this);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(myIntent);
                    finish();
                }else{
                    MyDynamicToast.informationMessage(RatingActivity.this, "Check your internet connection..");
                }
                }
            });
        }

        if(cancel != null){
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
    public boolean isConnectedToInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
