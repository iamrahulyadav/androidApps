package com.highlightindianapps.learnenglishfromhindi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import static com.highlightindianapps.learnenglishfromhindi.SplashActivity.splashObj;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String intentValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(WebViewActivity.this, OurAdActivity.class));
                    }
                }
            }
        }

        webView = (WebView) findViewById(R.id.web_view);

        Intent intent = getIntent();
        intentValue = intent.getStringExtra("webView");

        switch (intentValue) {
            case "best_way_to_learn_english":
                webView.loadUrl("file:///android_asset/tips.html");
                webView.getSettings().setBuiltInZoomControls(true);
                break;
            case "abhivadhan":
                webView.loadUrl("file:///android_asset/abhi.html");
                break;

            case "isAmAre":
                webView.loadUrl("file:///android_asset/is.html");
                break;

            case "hasHaveHad":
                webView.loadUrl("file:///android_asset/has.html");
                break;

            case "pleaseSorry":
                webView.loadUrl("file:///android_asset/sorry.html");
                break;

            case "tense":
                webView.loadUrl("file:///android_asset/tense.html");
                break;
            case "presentTense":
                webView.loadUrl("file:///android_asset/present.html");
                break;

            case "pastTense":
                webView.loadUrl("file:///android_asset/past.html");
                break;

            case "futureTense":
                webView.loadUrl("file:///android_asset/future.html");
                break;

            case "samanyeVasthuye":
                webView.loadUrl("file:///android_asset/voc.html");
                break;

            case "bodyParts":
                webView.loadUrl("file:///android_asset/boyparts.html");
                break;

            case "daysNamesInEnglish":
                webView.loadUrl("file:///android_asset/time.html");
                break;

            case "thereUsage":
                webView.loadUrl("file:///android_asset/there.html");
                break;

            case "letUsage":
                webView.loadUrl("file:///android_asset/let.html");
                break;
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
