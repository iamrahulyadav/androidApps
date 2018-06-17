package com.lock.voicescreenlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OurAdActivity extends AppCompatActivity implements View.OnClickListener {
        int num = 0;
        int num1 = 0;
        TextView loading;
        List<Integer> list1= new ArrayList<Integer>();
        ImageView close, ad_img;
    ProgressDialog progressDialog;
    Button no_thanks,btn_install;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_our_ad);

        progressDialog = new ProgressDialog(OurAdActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);
        ad_img = (ImageView) findViewById(R.id.ad_img);
        ad_img.setOnClickListener(this);
//        loading = (TextView) findViewById(R.id.loading);
//        no_thanks = (Button) findViewById(R.id.no_thanks);
//        no_thanks.setOnClickListener(this);
//        btn_install = (Button) findViewById(R.id.btn_install);
//        btn_install.setOnClickListener(this);


        checkNumber();
        switch (num){
            case 1:
                    Glide.with(this).load(StaticDataHandler.getInstance().getOurApp1iconIn()).thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    finish();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    dismissDialog();
                                    return false;
                                }
                            })
                            .into(ad_img);
                break;
            case 2:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp2iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 3:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp3iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 4:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp4iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 5:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp5iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 6:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp6iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 7:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp7iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 8:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp8iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);

                break;
            case 9:
                Glide.with(this).load(StaticDataHandler.getInstance().getOurApp9iconIn()).thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                               finish();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                dismissDialog();
                                return false;
                            }
                        })
                        .into(ad_img);


                break;


        }

    }

    private void  checkNumber() {

        String s = getSharedPreferences("template",MODE_PRIVATE).getString("Clicked_Ads", "11,12");
        String[] playlists = s.split(",");
            if(playlists.length >0 && playlists.length <11){
                getNumber();
                if(list1.size()> 0){
                    Collections.shuffle(list1);
                    num = list1.get(0);

                }else {
                    finish();
                }

        }
        else if( playlists.length >= 11){
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.close:
                finish();
                break;

            case R.id.ad_img:
            default:
                    clickEvent();
                break;
        }
    }

    private void clickEvent() {
        Intent myIntent = null;
        switch (num){
            case 1:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp1LinkIn()));
                break;
            case 2:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp2LinkIn()));
                break;
            case 3:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp3LinkIn()));
                break;
            case 4:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp4LinkIn()));
                break;
            case 5:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp5LinkIn()));
                break;
            case 6:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp6LinkIn()));
                break;
            case 7:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp7LinkIn()));
                break;
            case 8:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp8LinkIn()));
                break;
            case 9:
            default:
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticDataHandler.getInstance().getOurApp9LinkIn()));
                break;

        }
        if(myIntent != null){
            String s = getSharedPreferences("template",MODE_PRIVATE).getString("Clicked_Ads", "11,12");
            getSharedPreferences("template", MODE_PRIVATE).edit().putString("Clicked_Ads", s+","+num).apply();
            getNumber();
            startActivity(myIntent);
            finish();
        }

    }

    public void getNumber() {
        String p = getSharedPreferences("template",MODE_PRIVATE).getString("Priority", "1");
        int p1 = 3 * Integer.parseInt(p);
        if(p1 > 9){
            finish();
        }
        int[] a=  new int[p1];

        for(int i =0; i<p1;i++){
           a[i] = i+1;
        }
//        int[] a = {1,2,3,4,5,6,7,8,9};
        String s = getSharedPreferences("template",MODE_PRIVATE).getString("Clicked_Ads", "11,12");
        String[] ss = s.split(",");
        int[] b= new int[ss.length];
        for(int i =0; i<ss.length;i++){
            b[i] = Integer.parseInt(ss[i]);
        }

         findMissingNumber(a, b);
    }
    private  void findMissingNumber(int[] a, int[] b) {
        list1.clear();
        for (int n : a) {
            if (!isPresent(n, b)) {
                list1.add(n);
            }

        }

        if(list1.size() == 1 ){
            int p = Integer.parseInt(getSharedPreferences("template",MODE_PRIVATE).getString("Priority", "1")) ;
            if(p != 3){
                getSharedPreferences("template", MODE_PRIVATE).edit().putString("Priority",p+1+"").apply();
            }


        }
    }

    private  boolean isPresent(int n, int[] b) {
        for (int i : b) {
            if (n == i) {
                return true;
            }
        }
        return false;
    }

    private void dismissDialog(){
        if(!isFinishing()){
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
                progressDialog =null;
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }
}