package com.mehndidesign.offline2018;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.mehndidesign.offline2018.adapters.BridalDesignsAdapter;
import com.mehndidesign.offline2018.adapters.ExploreMehndiDesignsAdapter;
import com.mehndidesign.offline2018.adapters.LatestDesignsAdapter;
import com.mehndidesign.offline2018.helper.Designs;
import com.mehndidesign.offline2018.helper.ExploreDesigns;

import java.util.ArrayList;
import java.util.List;

import static com.mehndidesign.offline2018.SplashActivity.splashObj;

public class MainActivity extends AppCompatActivity {
    private LinearLayoutManager mLinearLayout;
    private TextView moreForlatest, moreForBridal;
//    Explore Designs
    private RecyclerView exploreMehdiDesignsRecyclerView;
    private ExploreMehndiDesignsAdapter exploreMehndiDesignsAdapter;
    private List<ExploreDesigns> exploreDesignsList;

//Latest Designs
    private List<Designs> designsList;
    private LatestDesignsAdapter latestDesignsAdapter;
//    Bridal
    private RecyclerView bridalRecyclerView;
    private List<Designs> bridalDesignsList;
    private BridalDesignsAdapter bridalDesignsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd() != null)
            if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                    splashObj.mInterstitialAd.show();
                }else {
                    startActivity(new Intent(MainActivity.this, OurAdActivity.class));
                }
            }
        }else{
            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
        }
//        if(!DataHandler.getInstance().isRatingDone(MainActivity.this) ) {
//            Intent i = new Intent(MainActivity.this, RatingActivity.class);
//            startActivity(i);
//        }

        InitializeVars();
//        Explore Designs
        settingExploreDesignsView();
//        Latest Desings
        settingLatestDesignsView();
//        Bridal Mehndi Designs
        settingBridalDesigns();
    }

    private void InitializeVars() {
        moreForlatest = (TextView) findViewById(R.id.more_latest_designs);
        moreForlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, GridViewActivity_old.class);
//                i.putExtra("designType", "latest");
//                startActivity(i);
            }
        });

        moreForBridal = (TextView) findViewById(R.id.more_bridal_designs);
        moreForBridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, GridViewActivity_old.class);
//                i.putExtra("designType", "bridal");
//                startActivity(i);
            }
        });
    }

    private void settingBridalDesigns() {
        bridalDesignsList = new ArrayList<>();

        bridalDesignsList.add(new Designs(R.drawable.bridal_1));
        bridalDesignsList.add(new Designs(R.drawable.bridal_2));
        bridalDesignsList.add(new Designs(R.drawable.bridal_3));
        bridalDesignsList.add(new Designs(R.drawable.bridal_4));
        bridalDesignsList.add(new Designs(R.drawable.bridal_5));
        bridalDesignsList.add(new Designs(R.drawable.bridal_6));
        bridalDesignsList.add(new Designs(R.drawable.bridal_7));
        bridalDesignsList.add(new Designs(R.drawable.bridal_8));
        bridalDesignsList.add(new Designs(R.drawable.bridal_9));
        bridalDesignsList.add(new Designs(R.drawable.bridal_10));
        bridalDesignsList.add(new Designs(R.drawable.bridal_11));
        bridalDesignsList.add(new Designs(R.drawable.bridal_12));

        bridalDesignsAdapter = new BridalDesignsAdapter(this, bridalDesignsList);
        bridalRecyclerView = (RecyclerView) findViewById(R.id.bridal_designs_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        bridalRecyclerView.setHasFixedSize(true);
        bridalRecyclerView.setLayoutManager(mLinearLayout);
        bridalRecyclerView.setAdapter(bridalDesignsAdapter);


    }

    private void settingLatestDesignsView() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.latest_2));
        designsList.add(new Designs(R.drawable.latest_3));
        designsList.add(new Designs(R.drawable.latest_4));
        designsList.add(new Designs(R.drawable.latest_5));
        designsList.add(new Designs(R.drawable.latest_6));
        designsList.add(new Designs(R.drawable.latest_7));
        designsList.add(new Designs(R.drawable.latest_8));
        designsList.add(new Designs(R.drawable.latest_9));
        designsList.add(new Designs(R.drawable.latest_10));
        designsList.add(new Designs(R.drawable.latest_11));
        designsList.add(new Designs(R.drawable.latest_12));

        latestDesignsAdapter = new LatestDesignsAdapter(this, designsList);
        exploreMehdiDesignsRecyclerView = (RecyclerView) findViewById(R.id.latest_designs_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        exploreMehdiDesignsRecyclerView.setHasFixedSize(true);
        exploreMehdiDesignsRecyclerView.setLayoutManager(mLinearLayout);
        exploreMehdiDesignsRecyclerView.setAdapter(latestDesignsAdapter);



    }

    private void settingExploreDesignsView() {
        exploreDesignsList = new ArrayList<>();
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_12, "Indian"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_12, "Pakistani"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_12, "Arabic"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_12, "Indo Arabivc"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_12, "Moracann"));
        exploreMehndiDesignsAdapter = new ExploreMehndiDesignsAdapter(this, exploreDesignsList);

        exploreMehdiDesignsRecyclerView = (RecyclerView) findViewById(R.id.explore_mehndi_recycler_view);
        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        exploreMehdiDesignsRecyclerView.setHasFixedSize(true);
        exploreMehdiDesignsRecyclerView.setLayoutManager(mLinearLayout);
        exploreMehdiDesignsRecyclerView.setAdapter(exploreMehndiDesignsAdapter);

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

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
