package com.mehndidesign.offline2018;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.mehndidesign.offline2018.adapters.BridalDesignsAdapter;
import com.mehndidesign.offline2018.adapters.ExploreMehndiDesignsAdapter;
import com.mehndidesign.offline2018.adapters.FingersDesignsAdapter;
import com.mehndidesign.offline2018.adapters.GlitterDesignsAdapter;
import com.mehndidesign.offline2018.adapters.GolTikkaDesignsAdapter;
import com.mehndidesign.offline2018.adapters.LatestDesignsAdapter;
import com.mehndidesign.offline2018.adapters.NailDesignsAdapter;
import com.mehndidesign.offline2018.adapters.TattoosDesignsAdapter;
import com.mehndidesign.offline2018.helper.Designs;
import com.mehndidesign.offline2018.helper.ExploreDesigns;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mehndidesign.offline2018.SplashActivity.splashObj;

public class NavigationMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Designs> favDesignsList;
    public static List<String> favStringList;
    private LinearLayoutManager mLinearLayout;
    private TextView moreForlatest, moreForBridal,
            moreForGlitter, moreForgolTikka, moreNailDesigns, moreForFingerDesigns, moreForTattoos;
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

//    Glitter
    private RecyclerView glitterRecyclerView;
    private List<Designs> glitterDesignsList;
    private GlitterDesignsAdapter glitterDesignsAdapter;


//
    private RecyclerView goltikkaRecyclerView;
    private List<Designs> goltikkaDesignsList;
    private GolTikkaDesignsAdapter goltikkaDesignsAdapter;

//
    private RecyclerView nailDesignsRecyclerView;
    private List<Designs> nailDesignsList;
    private NailDesignsAdapter nailDesignsAdapter;

//

    private RecyclerView fingersDesignsRecyclerView;
    private List<Designs> fingersDesignsList;
    private FingersDesignsAdapter fingersDesignsAdapter;


//
    private RecyclerView tattosDesignsRecyclerView;
    private List<Designs> tattosDesignsList;
    private TattoosDesignsAdapter tattosDesignsAdapter;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd() != null)
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(NavigationMain.this, OurAdActivity.class));
                    }
                }
        }else{
            MyDynamicToast.informationMessage(NavigationMain.this, "Check your internet connection..");
        }
        InitializeVars();
//        Explore Designs

        if(!checkPermission()) {
            requestPermission();
        }

        settingExploreDesignsView();
//        Latest Desings
        settingLatestDesignsView();
//        Bridal Mehndi Designs
        settingBridalDesigns();

        settingGlitterDesigns();

        settingGoltikkaDesigns();

        settingNailDesigns();

        settingFingersDesings();

        settingTatoosDesings();


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (readAccepted && writeAccepted) {

                    } else {
                        Toast.makeText(NavigationMain.this, "Permission Denied, You cannot download the pictures", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }

                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(NavigationMain.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    private void settingTatoosDesings() {
        tattosDesignsList = new ArrayList<>();

        tattosDesignsList.add(new Designs(R.drawable.tattoos_1));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_2));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_3));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_4));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_5));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_6));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_7));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_8));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_9));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_10));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_11));
        tattosDesignsList.add(new Designs(R.drawable.tattoos_12));

        tattosDesignsAdapter = new TattoosDesignsAdapter(this, tattosDesignsList);
        tattosDesignsRecyclerView = (RecyclerView) findViewById(R.id.tattos_designs_recycler_view);
        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        tattosDesignsRecyclerView.setHasFixedSize(true);
        tattosDesignsRecyclerView.setLayoutManager(mLinearLayout);
        tattosDesignsRecyclerView.setAdapter(tattosDesignsAdapter);
    }

    private void settingFingersDesings() {

        fingersDesignsList = new ArrayList<>();

        fingersDesignsList.add(new Designs(R.drawable.finger_1));
        fingersDesignsList.add(new Designs(R.drawable.finger_2));
        fingersDesignsList.add(new Designs(R.drawable.finger_3));
        fingersDesignsList.add(new Designs(R.drawable.finger_4));
        fingersDesignsList.add(new Designs(R.drawable.finger_5));
        fingersDesignsList.add(new Designs(R.drawable.finger_6));
        fingersDesignsList.add(new Designs(R.drawable.finger_7));
        fingersDesignsList.add(new Designs(R.drawable.finger_8));
        fingersDesignsList.add(new Designs(R.drawable.finger_9));
        fingersDesignsList.add(new Designs(R.drawable.finger_10));
        fingersDesignsList.add(new Designs(R.drawable.finger_11));
        fingersDesignsList.add(new Designs(R.drawable.finger_12));


        fingersDesignsAdapter = new FingersDesignsAdapter(this, fingersDesignsList);

        fingersDesignsRecyclerView = (RecyclerView) findViewById(R.id.fingers_designs_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        fingersDesignsRecyclerView.setHasFixedSize(true);
        fingersDesignsRecyclerView.setLayoutManager(mLinearLayout);
        fingersDesignsRecyclerView.setAdapter(fingersDesignsAdapter);

    }

    private void settingNailDesigns() {
        nailDesignsList = new ArrayList<>();

        nailDesignsList.add(new Designs(R.drawable.nails_1));
        nailDesignsList.add(new Designs(R.drawable.nails_2));
        nailDesignsList.add(new Designs(R.drawable.nails_3));
        nailDesignsList.add(new Designs(R.drawable.nails_4));
        nailDesignsList.add(new Designs(R.drawable.nails_5));
        nailDesignsList.add(new Designs(R.drawable.nails_6));
        nailDesignsList.add(new Designs(R.drawable.nails_7));
        nailDesignsList.add(new Designs(R.drawable.nails_8));
        nailDesignsList.add(new Designs(R.drawable.nails_9));
        nailDesignsList.add(new Designs(R.drawable.nails_10));
        nailDesignsList.add(new Designs(R.drawable.nails_11));
        nailDesignsList.add(new Designs(R.drawable.nails_12));

        nailDesignsAdapter = new NailDesignsAdapter(this, nailDesignsList);
        nailDesignsRecyclerView = (RecyclerView) findViewById(R.id.nail_designs_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        nailDesignsRecyclerView.setHasFixedSize(true);
        nailDesignsRecyclerView.setLayoutManager(mLinearLayout);
        nailDesignsRecyclerView.setAdapter(nailDesignsAdapter);

    }

    private void settingGoltikkaDesigns() {

        goltikkaDesignsList = new ArrayList<>();

        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_1));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_2));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_3));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_4));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_5));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_6));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_7));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_8));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_9));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_10));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_11));
        goltikkaDesignsList.add(new Designs(R.drawable.goltikka_12));


        goltikkaDesignsAdapter = new GolTikkaDesignsAdapter(this, goltikkaDesignsList);
        goltikkaRecyclerView = (RecyclerView) findViewById(R.id.goltikka_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        goltikkaRecyclerView.setHasFixedSize(true);
        goltikkaRecyclerView.setLayoutManager(mLinearLayout);
        goltikkaRecyclerView.setAdapter(goltikkaDesignsAdapter);
    }

    private void settingGlitterDesigns() {
        glitterDesignsList = new ArrayList<>();
        glitterDesignsList.add(new Designs(R.drawable.glitter_1_1));
        glitterDesignsList.add(new Designs(R.drawable.glitter_2));
        glitterDesignsList.add(new Designs(R.drawable.glitter_3));
        glitterDesignsList.add(new Designs(R.drawable.glitter_4));
        glitterDesignsList.add(new Designs(R.drawable.glitter_5));
        glitterDesignsList.add(new Designs(R.drawable.glitter_6));
        glitterDesignsList.add(new Designs(R.drawable.glitter_7));
        glitterDesignsList.add(new Designs(R.drawable.glitter_8));
        glitterDesignsList.add(new Designs(R.drawable.glitter_9));
        glitterDesignsList.add(new Designs(R.drawable.glitter_10));
        glitterDesignsList.add(new Designs(R.drawable.glitter_11));
        glitterDesignsList.add(new Designs(R.drawable.glitter_12));

        glitterDesignsAdapter = new GlitterDesignsAdapter(this, glitterDesignsList);
        glitterRecyclerView = (RecyclerView) findViewById(R.id.glitter_designs_recycler_view);

        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        glitterRecyclerView.setHasFixedSize(true);
        glitterRecyclerView.setLayoutManager(mLinearLayout);
        glitterRecyclerView.setAdapter(glitterDesignsAdapter);
    }

    private void InitializeVars() {

        moreForlatest = (TextView) findViewById(R.id.more_latest_designs);

        moreForlatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "latest");
                startActivity(i);
            }
        });

        moreForBridal = (TextView) findViewById(R.id.more_bridal_designs);
        moreForBridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "bridal");
                startActivity(i);
            }
        });

        moreForGlitter = (TextView) findViewById(R.id.more_glitter_designs);
        moreForGlitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "glitter");
                startActivity(i);
            }
        });
        moreForgolTikka = (TextView) findViewById(R.id.more_for_goltikka);
        moreForgolTikka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "goltikka");
                startActivity(i);
            }
        });

        moreNailDesigns = (TextView) findViewById(R.id.more_for_nail_designs);
        moreNailDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "nail_designs");
                startActivity(i);
            }
        });

        moreForFingerDesigns = (TextView) findViewById(R.id.more_for_only_on_fingers_designs);
        moreForFingerDesigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "finger_designs");
                startActivity(i);
            }
        });

        moreForTattoos = (TextView) findViewById(R.id.more_tattos_designs);
        moreForTattoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigationMain.this, GridViewActivity.class);
                i.putExtra("designType", "tattoos");
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void settingBridalDesigns() {
        bridalDesignsList = new ArrayList<>();

        bridalDesignsList.add(new Designs(R.drawable.bridal_1_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_2_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_3_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_4_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_5_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_6_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_7_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_8_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_9_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_11_small));
        bridalDesignsList.add(new Designs(R.drawable.bridal_12_small));

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

        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani_2, "Indian"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.pakistani, "Pakistani"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.arabic, "Arabic"));
        exploreDesignsList.add(new ExploreDesigns(R.drawable.moracann, "Moracann"));

        exploreMehndiDesignsAdapter = new ExploreMehndiDesignsAdapter(this, exploreDesignsList);

        exploreMehdiDesignsRecyclerView = (RecyclerView) findViewById(R.id.explore_mehndi_recycler_view);
        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        exploreMehdiDesignsRecyclerView.setHasFixedSize(true);
        exploreMehdiDesignsRecyclerView.setLayoutManager(mLinearLayout);
        exploreMehdiDesignsRecyclerView.setAdapter(exploreMehndiDesignsAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home_main) {
            // Handle the camera action
            intent = new Intent(NavigationMain.this, NavigationMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_bridal_main) {
            navigationView.setCheckedItem(R.id.nav_bridal_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "bridal");
            startActivity(intent);
        } else if (id == R.id.nav_glitter_main) {
            navigationView.setCheckedItem(R.id.nav_glitter_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "glitter");
            startActivity(intent);

        }
//        else if (id == R.id.nav_men_designs_main) {
//            navigationView.setCheckedItem(R.id.nav_men_designs_main);
//            intent = new Intent(NavigationMain.this, GridViewActivity.class);
//            intent.putExtra("designType", "men_designs");
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_gol_tikka_main) {
            navigationView.setCheckedItem(R.id.nav_gol_tikka_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "goltikka");
            startActivity(intent);

        }  else if (id == R.id.nav_on_nails_main) {
            navigationView.setCheckedItem(R.id.nav_on_nails_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "nail_designs");
            startActivity(intent);

        } else if (id == R.id.nav_on_fingers_main) {
            navigationView.setCheckedItem(R.id.nav_on_fingers_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "finger_designs");
            startActivity(intent);

        } else if (id == R.id.nav_tatoos_main) {
            navigationView.setCheckedItem(R.id.nav_tatoos_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "tattoos");
            startActivity(intent);

        } else if (id == R.id.nav_fav_list_main) {

            navigationView.setCheckedItem(R.id.nav_fav_list_main);
            SharedPreferences sharedPref = getApplicationContext().
                    getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);
            String jsonSaved = sharedPref.getString("Fav_List", "");
            if (jsonSaved == "[]" || jsonSaved == "") {
                Toast.makeText(NavigationMain.this, "There are No items in your favorite list", Toast.LENGTH_SHORT).show();
            }
            else {
                intent = new Intent(NavigationMain.this, GridViewActivity.class);
                intent.putExtra("designType", "fav_list");
                startActivity(intent);
            }
        } else if (id == R.id.nav_indian_main) {
            navigationView.setCheckedItem(R.id.nav_indian_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "indian");
            startActivity(intent);

        } else if (id == R.id.nav_pakistani_main) {
            navigationView.setCheckedItem(R.id.nav_pakistani_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "pakistani");
            startActivity(intent);
        } else if (id == R.id.nav_arabic_main) {
            navigationView.setCheckedItem(R.id.nav_arabic_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "arabic");
            startActivity(intent);

        }
//        else if (id == R.id.nav_indo_arabic_main) {
//            navigationView.setCheckedItem(R.id.nav_indo_arabic_main);
//            intent = new Intent(NavigationMain.this, GridViewActivity.class);
//            intent.putExtra("designType", "indo_arabic");
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_moracann_main) {
            navigationView.setCheckedItem(R.id.nav_moracann_main);
            intent = new Intent(NavigationMain.this, GridViewActivity.class);
            intent.putExtra("designType", "moracann");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
