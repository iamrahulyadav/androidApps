package com.mehndidesign.offline2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.mehndidesign.offline2018.adapters.GridViewImageAdapter;
import com.mehndidesign.offline2018.adapters.GridViewImageForFavDesigns;
import com.mehndidesign.offline2018.adapters.ImageAdapter;
import com.mehndidesign.offline2018.helper.Designs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.mehndidesign.offline2018.SplashActivity.splashObj;

public class GridViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp
    private List<Designs> designsList;
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private Intent intent;
    private String intentValue;
    public static Uri[] mUrls;
    public static List<String> favList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd() != null)
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(GridViewActivity.this, OurAdActivity.class));
                    }
                }
        }else{
            MyDynamicToast.informationMessage(GridViewActivity.this, "Check your internet connection..");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        intent = getIntent();
        intentValue = intent.getStringExtra("designType");

        gridView = (GridView) findViewById(R.id.gridView);
        InitilizeGridLayout();

        if (intentValue.equals("latest")) {
            designsList = latestDesignList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "latest");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("bridal")) {

            navigationView.setCheckedItem(R.id.nav_bridal_grid);
            designsList = bridalDesignList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "bridal");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });

        } else if (intentValue.equals("glitter")) {
            navigationView.setCheckedItem(R.id.nav_glitter_grid);
            designsList = glitterDesingsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "glitter");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("indian")){

            navigationView.setCheckedItem(R.id.nav_indian_grid);
            designsList = indanDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "indian");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("pakistani")) {
            navigationView.setCheckedItem(R.id.nav_pakistani_grid);
            designsList = pakistaniDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "pakistani");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("arabic")) {
            navigationView.setCheckedItem(R.id.nav_arabic_grid);
            designsList = arabicDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "arabic");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        }
//        else if (intentValue.equals("indo_arabic")) {
//            navigationView.setCheckedItem(R.id.nav_indo_arabic_grid);
//            designsList = indoArabicDesignsList();
//            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
//                    columnWidth,intentValue);
//            gridView.setAdapter(adapter);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
//                    i.putExtra("designType", "indo_arabic");
//                    i.putExtra("position", position);
//                    startActivity(i);
//                }
//            });
//        }
        else if (intentValue.equals("moracann")) {
            navigationView.setCheckedItem(R.id.nav_moracann_grid);
            designsList = moracannDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "moracann");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("fav_list")) {
            navigationView.setCheckedItem(R.id.nav_fav_list_grid);
            favList = favDesignsList();
            GridViewImageForFavDesigns gridViewImageForFavDesigns = new GridViewImageForFavDesigns(GridViewActivity.this, favList,
                    columnWidth,intentValue);
            gridView.setAdapter(gridViewImageForFavDesigns);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "fav_list");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("goltikka")) {
            navigationView.setCheckedItem(R.id.nav_gol_tikka_grid);
            designsList = golTikkaDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "goltikka");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("nail_designs")) {
            navigationView.setCheckedItem(R.id.nav_on_nails_grid);
            designsList = nailDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "nail_designs");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("finger_designs")) {
            navigationView.setCheckedItem(R.id.nav_on_fingers_grid);
            designsList = fingersDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "finger_designs");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        } else if (intentValue.equals("tattoos")) {
            navigationView.setCheckedItem(R.id.nav_tatoos_grid);
            designsList = tattoosDesignsList();
            adapter = new GridViewImageAdapter(GridViewActivity.this, designsList,
                    columnWidth,intentValue);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(GridViewActivity.this, FullViewActivity.class);
                    i.putExtra("designType", "tattoos");
                    i.putExtra("position", position);
                    startActivity(i);
                }
            });
        }
    }

    public List<Designs> tattoosDesignsList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.tattoos_1));
        designsList.add(new Designs(R.drawable.tattoos_2));
        designsList.add(new Designs(R.drawable.tattoos_3));
        designsList.add(new Designs(R.drawable.tattoos_4));
        designsList.add(new Designs(R.drawable.tattoos_5));
        designsList.add(new Designs(R.drawable.tattoos_6));
        designsList.add(new Designs(R.drawable.tattoos_7));
        designsList.add(new Designs(R.drawable.tattoos_8));
        designsList.add(new Designs(R.drawable.tattoos_9));
        designsList.add(new Designs(R.drawable.tattoos_10));
        designsList.add(new Designs(R.drawable.tattoos_11));
        designsList.add(new Designs(R.drawable.tattoos_12));
        designsList.add(new Designs(R.drawable.tattoos_13));
        designsList.add(new Designs(R.drawable.tattoos_14));
        designsList.add(new Designs(R.drawable.tattoos_15));
        designsList.add(new Designs(R.drawable.tattoos_16));
        designsList.add(new Designs(R.drawable.tattoos_17));
        designsList.add(new Designs(R.drawable.tattoos_18));
        designsList.add(new Designs(R.drawable.tattoos_19));
        designsList.add(new Designs(R.drawable.tattoos_20));
        designsList.add(new Designs(R.drawable.tattoos_21));
        designsList.add(new Designs(R.drawable.tattoos_22));
        designsList.add(new Designs(R.drawable.tattoos_23));
        designsList.add(new Designs(R.drawable.tattoos_24));
        designsList.add(new Designs(R.drawable.tattoos_25));
        designsList.add(new Designs(R.drawable.tattoos_26));
        designsList.add(new Designs(R.drawable.tattoos_27));
        designsList.add(new Designs(R.drawable.tattoos_28));
        designsList.add(new Designs(R.drawable.tattoos_29));
        designsList.add(new Designs(R.drawable.tattoos_30));
        designsList.add(new Designs(R.drawable.tattoos_31));
        designsList.add(new Designs(R.drawable.tattoos_32));
        designsList.add(new Designs(R.drawable.tattoos_33));
        designsList.add(new Designs(R.drawable.tattoos_34));
        return designsList;
    }

    public List<Designs> fingersDesignsList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.finger_1));
        designsList.add(new Designs(R.drawable.finger_2));
        designsList.add(new Designs(R.drawable.finger_3));
        designsList.add(new Designs(R.drawable.finger_4));
        designsList.add(new Designs(R.drawable.finger_5));
        designsList.add(new Designs(R.drawable.finger_6));
        designsList.add(new Designs(R.drawable.finger_7));
        designsList.add(new Designs(R.drawable.finger_8));
        designsList.add(new Designs(R.drawable.finger_9));
        designsList.add(new Designs(R.drawable.finger_10));
        designsList.add(new Designs(R.drawable.finger_11));
        designsList.add(new Designs(R.drawable.finger_12));
        designsList.add(new Designs(R.drawable.finger_13));
        designsList.add(new Designs(R.drawable.finger_14));
        designsList.add(new Designs(R.drawable.finger_15));
        designsList.add(new Designs(R.drawable.finger_16));
        designsList.add(new Designs(R.drawable.finger_17));
        designsList.add(new Designs(R.drawable.finger_18));
        designsList.add(new Designs(R.drawable.finger_19));
        designsList.add(new Designs(R.drawable.finger_20));
        designsList.add(new Designs(R.drawable.finger_21));
        designsList.add(new Designs(R.drawable.finger_22));
        designsList.add(new Designs(R.drawable.finger_23));
        designsList.add(new Designs(R.drawable.finger_24));
        designsList.add(new Designs(R.drawable.finger_25));
        designsList.add(new Designs(R.drawable.finger_26));
        designsList.add(new Designs(R.drawable.finger_27));
        designsList.add(new Designs(R.drawable.finger_28));
        designsList.add(new Designs(R.drawable.finger_29));
        designsList.add(new Designs(R.drawable.finger_30));
        designsList.add(new Designs(R.drawable.finger_31));
        designsList.add(new Designs(R.drawable.finger_32));
        designsList.add(new Designs(R.drawable.finger_33));
        designsList.add(new Designs(R.drawable.finger_34));
        designsList.add(new Designs(R.drawable.finger_35));
        designsList.add(new Designs(R.drawable.finger_36));
        designsList.add(new Designs(R.drawable.finger_37));
        designsList.add(new Designs(R.drawable.finger_38));
        designsList.add(new Designs(R.drawable.finger_39));
        designsList.add(new Designs(R.drawable.finger_40));
        designsList.add(new Designs(R.drawable.finger_41));
        return designsList;
    }

    public List<Designs> nailDesignsList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.nails_1));
        designsList.add(new Designs(R.drawable.nails_2));
        designsList.add(new Designs(R.drawable.nails_3));
        designsList.add(new Designs(R.drawable.nails_4));
        designsList.add(new Designs(R.drawable.nails_5));
        designsList.add(new Designs(R.drawable.nails_6));
        designsList.add(new Designs(R.drawable.nails_7));
        designsList.add(new Designs(R.drawable.nails_8));
        designsList.add(new Designs(R.drawable.nails_9));
        designsList.add(new Designs(R.drawable.nails_10));
        designsList.add(new Designs(R.drawable.nails_11));
        designsList.add(new Designs(R.drawable.nails_12));
        designsList.add(new Designs(R.drawable.nails_13));
        designsList.add(new Designs(R.drawable.nails_14));
        designsList.add(new Designs(R.drawable.nails_15));
        designsList.add(new Designs(R.drawable.nails_16));
        designsList.add(new Designs(R.drawable.nails_17));
        designsList.add(new Designs(R.drawable.nails_18));
        designsList.add(new Designs(R.drawable.nails_19));
        return designsList;
    }

    public List<Designs> golTikkaDesignsList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.goltikka_1));
        designsList.add(new Designs(R.drawable.goltikka_2));
        designsList.add(new Designs(R.drawable.goltikka_3));
        designsList.add(new Designs(R.drawable.goltikka_4));
        designsList.add(new Designs(R.drawable.goltikka_5));
        designsList.add(new Designs(R.drawable.goltikka_6));
        designsList.add(new Designs(R.drawable.goltikka_7));
        designsList.add(new Designs(R.drawable.goltikka_8));
        designsList.add(new Designs(R.drawable.goltikka_9));
        designsList.add(new Designs(R.drawable.goltikka_10));
        designsList.add(new Designs(R.drawable.goltikka_11));
        designsList.add(new Designs(R.drawable.goltikka_12));
        designsList.add(new Designs(R.drawable.goltikka_13));
        designsList.add(new Designs(R.drawable.goltikka_14));
        designsList.add(new Designs(R.drawable.goltikka_15));
        designsList.add(new Designs(R.drawable.goltikka_16));
        designsList.add(new Designs(R.drawable.goltikka_17));
        designsList.add(new Designs(R.drawable.goltikka_18));
        designsList.add(new Designs(R.drawable.goltikka_19));
        designsList.add(new Designs(R.drawable.goltikka_20));
        designsList.add(new Designs(R.drawable.goltikka_21));
        designsList.add(new Designs(R.drawable.goltikka_22));
        designsList.add(new Designs(R.drawable.goltikka_23));
        designsList.add(new Designs(R.drawable.goltikka_24));
        designsList.add(new Designs(R.drawable.goltikka_25));
        designsList.add(new Designs(R.drawable.goltikka_26));
        designsList.add(new Designs(R.drawable.goltikka_27));
        designsList.add(new Designs(R.drawable.goltikka_28));
        designsList.add(new Designs(R.drawable.goltikka_29));
        designsList.add(new Designs(R.drawable.goltikka_30));
        designsList.add(new Designs(R.drawable.goltikka_31));
        designsList.add(new Designs(R.drawable.goltikka_32));
        designsList.add(new Designs(R.drawable.goltikka_33));
        designsList.add(new Designs(R.drawable.goltikka_34));
        designsList.add(new Designs(R.drawable.goltikka_35));
        return designsList;
    }

    public List<String> favDesignsList() {

        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString("Fav_List", "");
        String replace = jsonSaved.replace("[","");
        System.out.println(replace);
        String replace1 = replace.replace("]","");
        System.out.println(replace1);
        List<String> myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));
        return myList;
    }


    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((getScreenWidth() - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);
        gridView.setNumColumns(NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_home_grid) {
            // Handle the camera action
            intent = new Intent(GridViewActivity.this, NavigationMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_bridal_grid) {
            if (!intentValue.equals("bridal")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "bridal");
                startActivity(intent);
            }

        } else if (id == R.id.nav_glitter_grid) {
            if (!intentValue.equals("glitter")) {
                item.setChecked(true);
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "glitter");
                startActivity(intent);
            }
        }
//        else if (id == R.id.nav_men_designs_grid) {
//            if (!intentValue.equals("men_designs")) {
//
//                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
//                intent.putExtra("designType", "men_designs");
//                startActivity(intent);
//            }
//        }
        else if (id == R.id.nav_gol_tikka_grid) {
            if (!intentValue.equals("goltikka")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "goltikka");
                startActivity(intent);
            }

        } else if (id == R.id.nav_on_nails_grid) {
            if (!intentValue.equals("pakistani")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "nail_designs");
                startActivity(intent);
            }
        } else if (id == R.id.nav_on_fingers_grid) {
            if (!intentValue.equals("finger_designs")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "finger_designs");
                startActivity(intent);
            }
        } else if (id == R.id.nav_tatoos_grid) {
            if (!intentValue.equals("tattoos")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "tattoos");
                startActivity(intent);
            }

        } else if (id == R.id.nav_indian_grid) {
            if (!intentValue.equals("indian")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "indian");
                startActivity(intent);
            }
        } else if (id == R.id.nav_pakistani_grid) {
            if (!intentValue.equals("pakistani")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "pakistani");
                startActivity(intent);
            }

        } else if (id == R.id.nav_arabic_grid) {
            if (!intentValue.equals("arabic")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "arabic");
                startActivity(intent);
            }
        }
//        else if (id == R.id.nav_indo_arabic_grid) {
//            if (!intentValue.equals("indo_arabic")) {
//                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
//                intent.putExtra("designType", "indo_arabic");
//                startActivity(intent);
//            }
//
//        }
        else if (id == R.id.nav_moracann_grid) {
            if (!intentValue.equals("moracann")) {
                intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "moracann");
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_fav_list_grid) {
            if (!intentValue.equals("fav_list")) {
                SharedPreferences sharedPref = getApplicationContext().
                        getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);
                String jsonSaved = sharedPref.getString("Fav_List", "");
                if (jsonSaved.equals("[]") || jsonSaved.equals("")) {
                    Toast.makeText(GridViewActivity.this, "There are No items in your favorite list", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent = new Intent(GridViewActivity.this, GridViewActivity.class);
                    intent.putExtra("designType", "fav_list");
                    startActivity(intent);
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Designs> moracannDesignsList() {
        designsList = new ArrayList<>();

        designsList.add(new Designs(R.drawable.moraccan_1));
        designsList.add(new Designs(R.drawable.moraccan_2));
        designsList.add(new Designs(R.drawable.moraccan_3));
        designsList.add(new Designs(R.drawable.moraccan_4));
        designsList.add(new Designs(R.drawable.moraccan_5));
        designsList.add(new Designs(R.drawable.moraccan_6));
        designsList.add(new Designs(R.drawable.moraccan_7));
        designsList.add(new Designs(R.drawable.moraccan_8));
        designsList.add(new Designs(R.drawable.moraccan_9));
        designsList.add(new Designs(R.drawable.moraccan_10));
        designsList.add(new Designs(R.drawable.moraccan_11));
        designsList.add(new Designs(R.drawable.moraccan_12));
        designsList.add(new Designs(R.drawable.moraccan_13));
        designsList.add(new Designs(R.drawable.moraccan_14));
        designsList.add(new Designs(R.drawable.moraccan_15));
        designsList.add(new Designs(R.drawable.moraccan_16));
        designsList.add(new Designs(R.drawable.moraccan_17));

        return designsList;
    }
    public List<Designs> indoArabicDesignsList() {
        designsList = new ArrayList<>();

        designsList.add(new Designs(R.drawable.bridal_1));
        designsList.add(new Designs(R.drawable.bridal_2));
        designsList.add(new Designs(R.drawable.bridal_3));
        designsList.add(new Designs(R.drawable.bridal_4));
        designsList.add(new Designs(R.drawable.bridal_5));
        designsList.add(new Designs(R.drawable.bridal_6));
        designsList.add(new Designs(R.drawable.bridal_7));
        designsList.add(new Designs(R.drawable.bridal_8));
        designsList.add(new Designs(R.drawable.bridal_9));
        designsList.add(new Designs(R.drawable.bridal_10));
        designsList.add(new Designs(R.drawable.bridal_11));
        designsList.add(new Designs(R.drawable.bridal_12));
        designsList.add(new Designs(R.drawable.bridal_1));
        designsList.add(new Designs(R.drawable.bridal_2));
        designsList.add(new Designs(R.drawable.bridal_3));
        designsList.add(new Designs(R.drawable.bridal_4));
        designsList.add(new Designs(R.drawable.bridal_5));
        designsList.add(new Designs(R.drawable.bridal_6));
        designsList.add(new Designs(R.drawable.bridal_7));
        designsList.add(new Designs(R.drawable.bridal_8));
        designsList.add(new Designs(R.drawable.bridal_9));
        designsList.add(new Designs(R.drawable.bridal_10));
        designsList.add(new Designs(R.drawable.bridal_11));
        designsList.add(new Designs(R.drawable.bridal_12));
        designsList.add(new Designs(R.drawable.bridal_1));
        designsList.add(new Designs(R.drawable.bridal_2));
        designsList.add(new Designs(R.drawable.bridal_3));
        designsList.add(new Designs(R.drawable.bridal_4));
        designsList.add(new Designs(R.drawable.bridal_5));
        designsList.add(new Designs(R.drawable.bridal_6));
        designsList.add(new Designs(R.drawable.bridal_7));
        designsList.add(new Designs(R.drawable.bridal_8));
        designsList.add(new Designs(R.drawable.bridal_9));
        designsList.add(new Designs(R.drawable.bridal_10));
        designsList.add(new Designs(R.drawable.bridal_11));
        designsList.add(new Designs(R.drawable.bridal_12));

        return designsList;
    }
    public List<Designs> arabicDesignsList() {
        designsList = new ArrayList<>();

        designsList.add(new Designs(R.drawable.arabic_1));
        designsList.add(new Designs(R.drawable.arabic_2));
        designsList.add(new Designs(R.drawable.arabic_3));
        designsList.add(new Designs(R.drawable.arabic_4));
        designsList.add(new Designs(R.drawable.arabic_5));
        designsList.add(new Designs(R.drawable.arabic_6));
        designsList.add(new Designs(R.drawable.arabic_7));
        designsList.add(new Designs(R.drawable.arabic_8));
        designsList.add(new Designs(R.drawable.arabic_9));
        designsList.add(new Designs(R.drawable.arabic_10));
        designsList.add(new Designs(R.drawable.arabic_11));
        designsList.add(new Designs(R.drawable.arabic_12));
        designsList.add(new Designs(R.drawable.arabic_13));
        designsList.add(new Designs(R.drawable.arabic_14));
        designsList.add(new Designs(R.drawable.arabic_15));
        designsList.add(new Designs(R.drawable.arabic_16));
        designsList.add(new Designs(R.drawable.arabic_17));
        designsList.add(new Designs(R.drawable.arabic_18));
        designsList.add(new Designs(R.drawable.arabic_19));

        return designsList;
    }
    public List<Designs> pakistaniDesignsList() {

        designsList = new ArrayList<>();

        designsList.add(new Designs(R.drawable.pakistani_12));
        designsList.add(new Designs(R.drawable.pakistani_4));
        designsList.add(new Designs(R.drawable.pakistani_5));
        designsList.add(new Designs(R.drawable.pakistani_6));
        designsList.add(new Designs(R.drawable.pakistani_7));
        designsList.add(new Designs(R.drawable.pakistani_8));
        designsList.add(new Designs(R.drawable.pakistani_9));
        designsList.add(new Designs(R.drawable.pakistani_10));
        designsList.add(new Designs(R.drawable.latest_20));
        designsList.add(new Designs(R.drawable.latest_10));
        designsList.add(new Designs(R.drawable.latest_30));
        designsList.add(new Designs(R.drawable.latest_40));
        designsList.add(new Designs(R.drawable.latest_21));
        designsList.add(new Designs(R.drawable.latest_25));
        designsList.add(new Designs(R.drawable.latest_29));
        designsList.add(new Designs(R.drawable.latest_32));
        designsList.add(new Designs(R.drawable.latest_34));
        designsList.add(new Designs(R.drawable.latest_50));
        designsList.add(new Designs(R.drawable.bridal_15));
        designsList.add(new Designs(R.drawable.bridal_16));
        designsList.add(new Designs(R.drawable.bridal_12));
        designsList.add(new Designs(R.drawable.bridal_17));
        designsList.add(new Designs(R.drawable.bridal_27));
        designsList.add(new Designs(R.drawable.bridal_32));

        return designsList;
    }
    public List<Designs> indanDesignsList() {
        designsList = new ArrayList<>();

        designsList.add(new Designs(R.drawable.bridal_12));
        designsList.add(new Designs(R.drawable.goltikka_1));
        designsList.add(new Designs(R.drawable.bridal_4));
        designsList.add(new Designs(R.drawable.goltikka_11));
        designsList.add(new Designs(R.drawable.goltikka_8));
        designsList.add(new Designs(R.drawable.goltikka_9));
        designsList.add(new Designs(R.drawable.goltikka_2));
        designsList.add(new Designs(R.drawable.goltikka_15));
        designsList.add(new Designs(R.drawable.bridal_2));
        designsList.add(new Designs(R.drawable.goltikka_20));
        designsList.add(new Designs(R.drawable.goltikka_25));
        designsList.add(new Designs(R.drawable.bridal_1));
        designsList.add(new Designs(R.drawable.bridal_5));
        designsList.add(new Designs(R.drawable.goltikka_30));
        designsList.add(new Designs(R.drawable.glitter_40));
        designsList.add(new Designs(R.drawable.glitter_23));
        designsList.add(new Designs(R.drawable.bridal_3));
        designsList.add(new Designs(R.drawable.glitter_25));
        designsList.add(new Designs(R.drawable.glitter_30));
        designsList.add(new Designs(R.drawable.bridal_10));
        designsList.add(new Designs(R.drawable.glitter_33));
        designsList.add(new Designs(R.drawable.glitter_10));
        designsList.add(new Designs(R.drawable.bridal_11));
        designsList.add(new Designs(R.drawable.bridal_8));
        designsList.add(new Designs(R.drawable.glitter_24));
        designsList.add(new Designs(R.drawable.glitter_14));
        designsList.add(new Designs(R.drawable.bridal_9));

        return designsList;
    }

    public List<Designs> glitterDesingsList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.glitter_1_1));
        designsList.add(new Designs(R.drawable.glitter_2));
        designsList.add(new Designs(R.drawable.glitter_3));
        designsList.add(new Designs(R.drawable.glitter_4));
        designsList.add(new Designs(R.drawable.glitter_5));
        designsList.add(new Designs(R.drawable.glitter_6));
        designsList.add(new Designs(R.drawable.glitter_7));
        designsList.add(new Designs(R.drawable.glitter_8));
        designsList.add(new Designs(R.drawable.glitter_9));
        designsList.add(new Designs(R.drawable.glitter_10));
        designsList.add(new Designs(R.drawable.glitter_11));
        designsList.add(new Designs(R.drawable.glitter_13));
        designsList.add(new Designs(R.drawable.glitter_14));
        designsList.add(new Designs(R.drawable.glitter_15));
        designsList.add(new Designs(R.drawable.glitter_16));
        designsList.add(new Designs(R.drawable.glitter_17));
        designsList.add(new Designs(R.drawable.glitter_18));
        designsList.add(new Designs(R.drawable.glitter_19));
        designsList.add(new Designs(R.drawable.glitter_20));
        designsList.add(new Designs(R.drawable.glitter_21));
        designsList.add(new Designs(R.drawable.glitter_22));
        designsList.add(new Designs(R.drawable.glitter_23));
        designsList.add(new Designs(R.drawable.glitter_24));
        designsList.add(new Designs(R.drawable.glitter_25));
        designsList.add(new Designs(R.drawable.glitter_26));
        designsList.add(new Designs(R.drawable.glitter_28));
        designsList.add(new Designs(R.drawable.glitter_29));
        designsList.add(new Designs(R.drawable.glitter_30));
        designsList.add(new Designs(R.drawable.glitter_31));
        designsList.add(new Designs(R.drawable.glitter_32));
        designsList.add(new Designs(R.drawable.glitter_33));
        designsList.add(new Designs(R.drawable.glitter_34));
        designsList.add(new Designs(R.drawable.glitter_35));
        designsList.add(new Designs(R.drawable.glitter_36));
        designsList.add(new Designs(R.drawable.glitter_37));
        designsList.add(new Designs(R.drawable.glitter_38));
        designsList.add(new Designs(R.drawable.glitter_39));
        designsList.add(new Designs(R.drawable.glitter_40));
        return designsList;
    }

    public List<Designs> bridalDesignList() {
        designsList = new ArrayList<>();
        designsList.add(new Designs(R.drawable.bridal_1));
        designsList.add(new Designs(R.drawable.bridal_2));
        designsList.add(new Designs(R.drawable.bridal_3));
        designsList.add(new Designs(R.drawable.bridal_4));
        designsList.add(new Designs(R.drawable.bridal_5));
        designsList.add(new Designs(R.drawable.bridal_6));
        designsList.add(new Designs(R.drawable.bridal_7));
        designsList.add(new Designs(R.drawable.bridal_8));
        designsList.add(new Designs(R.drawable.bridal_9));
        designsList.add(new Designs(R.drawable.bridal_11));
        designsList.add(new Designs(R.drawable.bridal_12));
        designsList.add(new Designs(R.drawable.bridal_14));
        designsList.add(new Designs(R.drawable.bridal_15));
        designsList.add(new Designs(R.drawable.bridal_16));
        designsList.add(new Designs(R.drawable.bridal_17));
        designsList.add(new Designs(R.drawable.bridal_18));
        designsList.add(new Designs(R.drawable.bridal_19));
        designsList.add(new Designs(R.drawable.bridal_20));
        designsList.add(new Designs(R.drawable.bridal_22));
        designsList.add(new Designs(R.drawable.bridal_23));
        designsList.add(new Designs(R.drawable.bridal_24));
        designsList.add(new Designs(R.drawable.bridal_25));
        designsList.add(new Designs(R.drawable.bridal_27));
        designsList.add(new Designs(R.drawable.bridal_28));
        designsList.add(new Designs(R.drawable.bridal_29));
        designsList.add(new Designs(R.drawable.bridal_30));
        designsList.add(new Designs(R.drawable.bridal_31));
        designsList.add(new Designs(R.drawable.bridal_32));
        designsList.add(new Designs(R.drawable.bridal_33));
        designsList.add(new Designs(R.drawable.bridal_34));
        designsList.add(new Designs(R.drawable.bridal_35));
        designsList.add(new Designs(R.drawable.bridal_37));
        designsList.add(new Designs(R.drawable.bridal_38));
        designsList.add(new Designs(R.drawable.bridal_39));
        designsList.add(new Designs(R.drawable.bridal_40));
        designsList.add(new Designs(R.drawable.bridal_41));
        designsList.add(new Designs(R.drawable.bridal_42));
        return designsList;
    }

    public List<Designs> latestDesignList() {
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
        designsList.add(new Designs(R.drawable.latest_13));
        designsList.add(new Designs(R.drawable.latest_14));
        designsList.add(new Designs(R.drawable.latest_15));
        designsList.add(new Designs(R.drawable.latest_16));
        designsList.add(new Designs(R.drawable.latest_17));
        designsList.add(new Designs(R.drawable.latest_18));
        designsList.add(new Designs(R.drawable.latest_19));
        designsList.add(new Designs(R.drawable.latest_20));
        designsList.add(new Designs(R.drawable.latest_21));
        designsList.add(new Designs(R.drawable.latest_22));
        designsList.add(new Designs(R.drawable.latest_25));
        designsList.add(new Designs(R.drawable.latest_26));
        designsList.add(new Designs(R.drawable.latest_27));
        designsList.add(new Designs(R.drawable.latest_28));
        designsList.add(new Designs(R.drawable.latest_30));
        designsList.add(new Designs(R.drawable.latest_31));
        designsList.add(new Designs(R.drawable.latest_32));
        designsList.add(new Designs(R.drawable.latest_33));
        designsList.add(new Designs(R.drawable.latest_34));
        designsList.add(new Designs(R.drawable.latest_35));
        designsList.add(new Designs(R.drawable.latest_36));
        designsList.add(new Designs(R.drawable.latest_37));
        designsList.add(new Designs(R.drawable.latest_38));
        designsList.add(new Designs(R.drawable.latest_39));
        designsList.add(new Designs(R.drawable.latest_40));
        designsList.add(new Designs(R.drawable.latest_41));
        designsList.add(new Designs(R.drawable.latest_42));
        designsList.add(new Designs(R.drawable.latest_43));
        designsList.add(new Designs(R.drawable.latest_44));
        designsList.add(new Designs(R.drawable.latest_45));
        designsList.add(new Designs(R.drawable.latest_46));
        designsList.add(new Designs(R.drawable.latest_47));
        designsList.add(new Designs(R.drawable.latest_49));
        designsList.add(new Designs(R.drawable.latest_50));
        return designsList;
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
