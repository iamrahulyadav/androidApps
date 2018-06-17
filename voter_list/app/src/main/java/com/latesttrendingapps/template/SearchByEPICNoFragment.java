package com.latesttrendingapps.template;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import static com.latesttrendingapps.template.MyUtils.populateAppInstallAdView;
import static com.latesttrendingapps.template.MyUtils.populateContentAdView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByEPICNoFragment extends Fragment {


    private View mMainView;
    private EditText search;
    private Button searchBtn;
    private ImageView clearText, searchImage;
    private ProgressDialog progressDialog;
    private TextView epicName, epicFatherName, epicDob, epicSex, epicAddress, errorMsg;
    private CardView detailsCardView;
    private LinearLayout addsLayout;
    private boolean addNotLoaded = true;
    public SearchByEPICNoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_search_by_epicno, container, false);

        search = (EditText) mMainView.findViewById(R.id.search_view);
        searchBtn = (Button) mMainView.findViewById(R.id.search_btn);
        clearText = (ImageView) mMainView.findViewById(R.id.clear_text_img);

        epicName = (TextView) mMainView.findViewById(R.id.epic_name);
        epicFatherName = (TextView) mMainView.findViewById(R.id.epic_father_name);
        epicDob = (TextView) mMainView.findViewById(R.id.epic_dob);
        epicSex = (TextView) mMainView.findViewById(R.id.epic_sex);
        epicAddress = (TextView) mMainView.findViewById(R.id.epic_address);
        detailsCardView = (CardView) mMainView.findViewById(R.id.card_view);
        errorMsg = (TextView) mMainView.findViewById(R.id.error_message);
        addsLayout = (LinearLayout) mMainView.findViewById(R.id.addsLayout);
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSingleInstall();
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNotLoaded) {
                    errorMsg.setVisibility(View.INVISIBLE);
                    detailsCardView.setVisibility(View.INVISIBLE);
                    addsLayout.setVisibility(View.VISIBLE);
                    search.setText("");
                    clearText.setVisibility(View.INVISIBLE);
                } else {
                    errorMsg.setVisibility(View.INVISIBLE);
                    detailsCardView.setVisibility(View.INVISIBLE);
                    addsLayout.setVisibility(View.VISIBLE);
                    search.setText("");
                    clearText.setVisibility(View.INVISIBLE);
                }


            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clearText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        final String epicNo = search.getText().toString();
        if (epicNo.equals("")) {
            Toast.makeText(getContext(), "Search Area Cannot be Empty", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Fetching Data");
            progressDialog.setMessage("Please while we are processing your request ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    detailsCardView.setVisibility(View.INVISIBLE);
                    if (epicNo.equals("")) {
                        Toast.makeText(getContext(), "Search Area Cannot be Empty", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = getUrl(epicNo);
                        Object[] DataTransfer = new Object[10];
                        DataTransfer[0] = epicName;
                        DataTransfer[1] = epicFatherName;
                        DataTransfer[2] = epicDob;
                        DataTransfer[3] = epicSex;
                        DataTransfer[4] = epicAddress;
                        DataTransfer[5] = url;
                        DataTransfer[6] = progressDialog;
                        DataTransfer[7] = detailsCardView;
                        DataTransfer[8] = errorMsg;
                        DataTransfer[9] = addsLayout;
                        GetEPICData getEPICData = new GetEPICData();
                        getEPICData.execute(DataTransfer);
                    }
                }
            }, 2000);
        }
    }

    private String getUrl(String epicNo) {
        StringBuilder stringBuilder = new StringBuilder("http://electoralsearch.in/VoterSearch/SASSearch?search_type=epic");
        stringBuilder.append("&epic_no="+epicNo);
        return  (stringBuilder.toString());
    }

    private void loadSingleInstall() {

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.ad_unit_id));
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                FrameLayout frameLayout;
                frameLayout = (FrameLayout) mMainView.findViewById(R.id.adAtFragment1);
                @SuppressLint("InflateParams")
                NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
                        .inflate(R.layout.ad_app_install, null);
                populateAppInstallAdView(ad, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                FrameLayout frameLayout = (FrameLayout) mMainView.findViewById(R.id.adAtFragment1);
                @SuppressLint("InflateParams") NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
                        .inflate(R.layout.ad_content, null);
                populateContentAdView(ad, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getContext(), "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
                addNotLoaded = false;
            }
        }).build();

//        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
//                .setNativeAdChoicesIconExpandable(true)
//                .build();
//        AdRequest adRequest = new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .build();
//        adLoader.loadAd(adRequest);

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
