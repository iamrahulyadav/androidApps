package com.mehndidesign.offline2018;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.mehndidesign.offline2018.adapters.FavFullScreenImageAdapter;
import com.mehndidesign.offline2018.adapters.FullScreenImageAdapter;
import com.mehndidesign.offline2018.helper.Designs;
import com.startapp.android.publish.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mehndidesign.offline2018.GridViewActivity.favList;
import static com.mehndidesign.offline2018.SplashActivity.splashObj;

public class FullViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ImageView favImage;
    private ViewPager mViewPager;
    private FullScreenImageAdapter adapter;
    private Intent intent;
    private int position;
    private String designFrom;

    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton fab;

    private LinearLayout share;
    private LinearLayout download;
    private LinearLayout dragMe;
    private LinearLayout fav;
    private int res = 0;
    NavigationView navigationView;
    Toolbar toolbar;
    private GridViewActivity gridViewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isConnectedToInternet()){
            if(StaticDataHandler.getInstance().getShowInterstitialAd() != null)
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(FullViewActivity.this, OurAdActivity.class));
                    }
                }
        }else{
            MyDynamicToast.informationMessage(FullViewActivity.this, "Check your internet connection..");
        }
//        Initialize Variable of the View
        initializeVars();
//        Loading Image Full view and seting it to Adapter
        initializeViewToLoadImages();
//        DownloadImage Function
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    downloadImage();
                    if(!DataHandler.getInstance().isRatingDone(FullViewActivity.this) ) {
                        Intent i = new Intent(FullViewActivity.this, RatingActivity.class);
                        startActivity(i);
                    }
                } else {
                    requestPermission();
                }
            }
        });
//        SAving To Fav List
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    savingToFavList();
                    if(!DataHandler.getInstance().isRatingDone(FullViewActivity.this) ) {
                        Intent i = new Intent(FullViewActivity.this, RatingActivity.class);
                        startActivity(i);
                    }
                } else {
                    requestPermission();
                }
            }

        });

//BottomSheet Behaviour
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        fab.setVisibility(View.INVISIBLE);
                        fab.setImageResource(R.drawable.ic_expand_less_black_24dp);
                        dragMe.setVisibility(View.VISIBLE);
                        fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        dragMe.setVisibility(View.GONE);
                        fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        dragMe.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        fab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
//Share Images Via Social Networks
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });


    }

    private void shareImage() {
        Bitmap bitmap;
        int res;
        int item =  mViewPager.getCurrentItem();
        switch (designFrom) {
            case "latest" :
                res = gridViewActivity.latestDesignList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "bridal":
                res = gridViewActivity.bridalDesignList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "glitter" :
                res = gridViewActivity.glitterDesingsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "indian":
                res = gridViewActivity.indanDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "pakistani" :
                res = gridViewActivity.pakistaniDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "arabic" :
                res = gridViewActivity.arabicDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
//            case "indo_arabic" :
//                res = gridViewActivity.indoArabicDesignsList().get(item).getDesigns();
//                bitmap = BitmapFactory.decodeResource(getResources(),
//                        res);
//                break;
            case "moracann" :
                res = gridViewActivity.moracannDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;

            case "goltikka":
                res = gridViewActivity.golTikkaDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;

            case "nail_designs":
                res = gridViewActivity.nailDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;

            case "finger_designs":
                res = gridViewActivity.fingersDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "tattoos":
                res = gridViewActivity.tattoosDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            default:
                bitmap = null;
        }

        if (bitmap !=null) {
            String path = getExternalCacheDir()+"/shareimage.jpg";
            OutputStream out;
            File file=new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            path=file.getPath();
            Uri bmpUri = Uri.parse("file://"+path);

            new Intent();
            Intent shareIntent;
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent,"Share with"));
        } else {
            Toast.makeText(this, "There is Some Error While Sharing. Please try after some time", Toast.LENGTH_SHORT).show();
        }

    }


    private void savingToFavList() {
        int res = 0;
        int item =  mViewPager.getCurrentItem();

        switch (designFrom) {
            case "latest" :
                res = gridViewActivity.latestDesignList().get(item).getDesigns();
                break;
            case "bridal":
                res = gridViewActivity.bridalDesignList().get(item).getDesigns();
                break;
            case "glitter" :
                res = gridViewActivity.glitterDesingsList().get(item).getDesigns();
                break;
            case "indian":
                res = gridViewActivity.indanDesignsList().get(item).getDesigns();
                break;
            case "pakistani" :
                res = gridViewActivity.pakistaniDesignsList().get(item).getDesigns();
                break;
            case "arabic" :
                res = gridViewActivity.arabicDesignsList().get(item).getDesigns();
                break;
//            case "indo_arabic" :
//                res = gridViewActivity.indoArabicDesignsList().get(item).getDesigns();
//                break;
            case "moracann" :
                res = gridViewActivity.moracannDesignsList().get(item).getDesigns();
                break;
            case "goltikka" :
                res = gridViewActivity.golTikkaDesignsList().get(item).getDesigns();
                break;
            case "nail_designs" :
                res = gridViewActivity.nailDesignsList().get(item).getDesigns();
                break;
            case "finger_designs" :
                res = gridViewActivity.fingersDesignsList().get(item).getDesigns();
                break;

            case "tattoos" :
                res = gridViewActivity.tattoosDesignsList().get(item).getDesigns();
                break;
            case "fav_list":
                int itemFav =  mViewPager.getCurrentItem();
                ArrayList<Designs> result = new ArrayList<>();
                for(String stringValue : favList) {
                    try {
                        result.add(new Designs(Integer.parseInt(stringValue)));
                    } catch(NumberFormatException nfe) {
                        Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
                    }
                }
                res = result.get(itemFav).getDesigns();
                break;
        }
        addToSharedPrefs(res);
    }

    private void downloadImage() {
        Bitmap bitmap;
        int res;
        int item =  mViewPager.getCurrentItem();
        switch (designFrom) {
            case "latest" :
                res = gridViewActivity.latestDesignList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "bridal":
                res = gridViewActivity.bridalDesignList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "glitter" :
                res = gridViewActivity.glitterDesingsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "indian":
                res = gridViewActivity.indanDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "pakistani" :
                res = gridViewActivity.pakistaniDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "arabic" :
                res = gridViewActivity.arabicDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
//            case "indo_arabic" :
//                res = gridViewActivity.indoArabicDesignsList().get(item).getDesigns();
//                bitmap = BitmapFactory.decodeResource(getResources(),
//                        res);
//                break;
            case "moracann" :
                res = gridViewActivity.moracannDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "goltikka" :
                res = gridViewActivity.golTikkaDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "nail_designs" :
                res = gridViewActivity.nailDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;

            case "finger_designs" :
                res = gridViewActivity.fingersDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            case "tattoos" :
                res = gridViewActivity.tattoosDesignsList().get(item).getDesigns();
                bitmap = BitmapFactory.decodeResource(getResources(),
                        res);
                break;
            default:
                bitmap = null;
        }
        if (bitmap != null) {
            File folder = Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File dir = new File(folder + "/Mehndi");
            String fileName = getToken(10)+".png";
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(new File(folder + "/Mehndi"), fileName);
            Toast.makeText(FullViewActivity.this, folder + "/Mehndi",
                    Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "There is some Error While downloading your Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViewToLoadImages() {
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        switch (designFrom) {
            case "latest":
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.latestDesignList());
                break;
            case "bridal":
                navigationView.setCheckedItem(R.id.nav_bridal);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.bridalDesignList());
                break;
            case "glitter":
                navigationView.setCheckedItem(R.id.nav_glitter);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.glitterDesingsList());
                break;
            case "indian":
                navigationView.setCheckedItem(R.id.nav_indian);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.indanDesignsList());
                break;
            case "pakistani":
                navigationView.setCheckedItem(R.id.nav_pakistani);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.pakistaniDesignsList());
                break;
            case "arabic":
                navigationView.setCheckedItem(R.id.nav_arabic);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.arabicDesignsList());
                break;
//            case "indo_arabic":
//                navigationView.setCheckedItem(R.id.nav_indo_arabic);
//                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.indoArabicDesignsList());
//                break;
            case "moracann":
                navigationView.setCheckedItem(R.id.nav_moracann);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.moracannDesignsList());
                break;

            case "goltikka":
                navigationView.setCheckedItem(R.id.nav_gol_tikka);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.golTikkaDesignsList());
                break;

            case "nail_designs":
                navigationView.setCheckedItem(R.id.nav_on_nails);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.nailDesignsList());
                break;

            case "finger_designs":
                navigationView.setCheckedItem(R.id.nav_on_fingers);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.fingersDesignsList());
                break;

            case "tattoos":
                navigationView.setCheckedItem(R.id.nav_tatoos);
                adapter = new FullScreenImageAdapter(FullViewActivity.this, gridViewActivity.tattoosDesignsList());
                break;
            case "fav_list":
                navigationView.setCheckedItem(R.id.nav_fav_list);
                ArrayList<Designs> result = new ArrayList<>();
                for(String stringValue : favList) {
                    try {
                        result.add(new Designs(Integer.parseInt(stringValue)));
                    } catch(NumberFormatException nfe) {
                        Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
                    }
                }
                adapter = new FullScreenImageAdapter(FullViewActivity.this, result);
        }

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeFavSymbol();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeFavSymbol() {
        int resChange = 0;
        int item =  mViewPager.getCurrentItem();
        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString("Fav_List", "");
        String replace = jsonSaved.replace("[","");
        System.out.println(replace);
        String replace1 = replace.replace("]","");
        System.out.println(replace1);
        List<String> myList = new ArrayList<>(Arrays.asList(replace1.split(",")));

        List<Integer> result = new ArrayList<>();
        for(String stringValue : myList) {
            try {
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
            }
        }

        switch (designFrom) {
            case "latest" :
                resChange = gridViewActivity.latestDesignList().get(item).getDesigns();
                break;
            case "bridal":
                resChange = gridViewActivity.bridalDesignList().get(item).getDesigns();
                break;
            case "glitter" :
                resChange = gridViewActivity.glitterDesingsList().get(item).getDesigns();
                break;
            case "indian":
                resChange = gridViewActivity.indanDesignsList().get(item).getDesigns();
                break;
            case "pakistani" :
                resChange = gridViewActivity.pakistaniDesignsList().get(item).getDesigns();
                break;
            case "arabic" :
                resChange = gridViewActivity.arabicDesignsList().get(item).getDesigns();
                break;
//            case "indo_arabic" :
//                resChange = gridViewActivity.indoArabicDesignsList().get(item).getDesigns();
//                break;
            case "moracann" :
                resChange = gridViewActivity.moracannDesignsList().get(item).getDesigns();
                break;

            case "goltikka":
                resChange = gridViewActivity.golTikkaDesignsList().get(item).getDesigns();
                break;

            case "nail_designs":
                resChange = gridViewActivity.nailDesignsList().get(item).getDesigns();
                break;

            case "finger_designs":
                resChange = gridViewActivity.fingersDesignsList().get(item).getDesigns();
                break;
            case "tattoos":
                resChange = gridViewActivity.tattoosDesignsList().get(item).getDesigns();
                break;

        }

        if (result.contains(resChange) || designFrom.equals("fav_list")) {
            favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void initializeVars() {

        favImage = (ImageView) findViewById(R.id.fav_image);
        share = (LinearLayout) findViewById(R.id.layout_share);
        download = (LinearLayout) findViewById(R.id.layout_download);
        dragMe = (LinearLayout) findViewById(R.id.layout_drag);
        fav = (LinearLayout) findViewById(R.id.layout_fav);

        fab = findViewById(R.id.fab);
        View llBottomSheet = findViewById(R.id.include);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        gridViewActivity = new GridViewActivity();
        intent = getIntent();
        mViewPager = (ViewPager) findViewById(R.id.fullViewpager);
        position = intent.getIntExtra("position", 0);
        designFrom = intent.getStringExtra("designType");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    dragMe.setVisibility(View.GONE);
                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    fab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addToSharedPrefs(int res) {

        String stringValue = String.valueOf(res);
        Gson gson = new Gson();
        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString("Fav_List", "");

        if (jsonSaved.contains(stringValue)) {
            String replacedString = jsonSaved.replaceAll(stringValue+",", "");
            replacedString =  replacedString.replaceAll(","+stringValue, "");
            replacedString =  replacedString.replaceAll(stringValue+"", "");
            replacedString = replacedString.replaceAll("\\[\\]", "");

            try {
                if(replacedString.length()!=0){

                    JSONArray jsonArrayProduct;
                    jsonArrayProduct = new JSONArray(replacedString);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Fav_List", String.valueOf(jsonArrayProduct));
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Fav_List", "");
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "You made this picture as unfavorite", Toast.LENGTH_SHORT).show();
            favImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            Integer jsonNewproductToAdd = Integer.valueOf(gson.toJson(res));

            JSONArray jsonArrayProduct= new JSONArray();

            try {
                if(jsonSaved.length()!=0){
                    jsonArrayProduct = new JSONArray(jsonSaved);
                }
                jsonArrayProduct.put(jsonNewproductToAdd);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Fav_List", String.valueOf(jsonArrayProduct));
            editor.commit();
            Toast.makeText(this, "Updated to your favorite list", Toast.LENGTH_SHORT).show();
            favImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.nav_home) {
            intent = new Intent(FullViewActivity.this, NavigationMain.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_bridal) {
            if (!designFrom.equals("bridal")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "bridal");
                startActivity(intent);
            }

        } else if (id == R.id.nav_glitter) {
            if (!designFrom.equals("glitter")) {
                item.setChecked(true);
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "glitter");
                startActivity(intent);
            }
        }
//        else if (id == R.id.nav_men_designs) {
//            if (!designFrom.equals("men_designs")) {
//                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
//                intent.putExtra("designType", "men_designs");
//                startActivity(intent);
//            }
//
//        }
        else if (id == R.id.nav_gol_tikka) {
            if (!designFrom.equals("goltikka")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "goltikka");
                startActivity(intent);
            }
        } else if (id == R.id.nav_on_nails) {
            if (!designFrom.equals("nail_designs")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "nail_designs");
                startActivity(intent);
            }
        } else if (id == R.id.nav_on_fingers) {
            if (!designFrom.equals("finger_designs")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "finger_designs");
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_tatoos) {
            if (!designFrom.equals("tattoos")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "tattoos");
                startActivity(intent);
            }

        } else if (id == R.id.nav_indian) {
            if (!designFrom.equals("indian")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "indian");
                startActivity(intent);
            }

        } else if (id == R.id.nav_pakistani) {
            if (!designFrom.equals("pakistani")) {

                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "pakistani");
                startActivity(intent);

            }

        } else if (id == R.id.nav_arabic) {
            if (!designFrom.equals("arabic")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "arabic");
                startActivity(intent);
            }

        }
//        else if (id == R.id.nav_indo_arabic) {
//            if (!designFrom.equals("indo_arabic")) {
//                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
//                intent.putExtra("designType", "indo_arabic");
//                startActivity(intent);
//            }
//
//        }
        else if (id == R.id.nav_moracann) {
            if (!designFrom.equals("moracann")) {
                intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                intent.putExtra("designType", "moracann");
                startActivity(intent);
            }

        }  else if (id == R.id.nav_fav_list) {
            if (!designFrom.equals("fav_list")) {
                SharedPreferences sharedPref = getApplicationContext().
                        getSharedPreferences("Mehndi_Desings", Context.MODE_PRIVATE);
                String jsonSaved = sharedPref.getString("Fav_List", "");
                if (jsonSaved.equals("[]") || jsonSaved.equals("")) {
                    Toast.makeText(FullViewActivity.this, "There are No items in your favorite list", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent = new Intent(FullViewActivity.this, GridViewActivity.class);
                    intent.putExtra("designType", "fav_list");
                    startActivity(intent);
                }
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static String getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                        Toast.makeText(FullViewActivity.this, "Permission Denied, You cannot download the pictures", Toast.LENGTH_LONG).show();

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
        new AlertDialog.Builder(FullViewActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }
}
