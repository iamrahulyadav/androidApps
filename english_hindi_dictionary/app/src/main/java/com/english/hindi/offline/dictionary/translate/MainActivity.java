package com.english.hindi.offline.dictionary.translate;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.english.hindi.offline.dictionary.translate.SplashActivity.splashObj;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayAdapter<String> adapter;
    private EditText inputSearch;
    private ProgressDialog mProgressDialog;
    Handler handler;
    RelativeLayout meaningCardView;
    //    private CardView meaningCardView;
    private TextView englishWord, hindiMeaning;
    private com.wang.avi.AVLoadingIndicatorView aviLoader;

    private ImageView cleartext;

    private Integer ratingLocal = 0;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference databaseReference;
    Integer ratingValueFromFrirebase;

    Boolean rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_app_bar);
        setSupportActionBar(toolbar);

        initializeVars();
        aviLoader = (com.wang.avi.AVLoadingIndicatorView)findViewById(R.id.aviGetSearchNum);
        if(isConnectedToInternet()){
            if (StaticDataHandler.getInstance().getShowInterstitialAd() != null) {
                if(StaticDataHandler.getInstance().getShowInterstitialAd().equals("true")){
                    if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                        splashObj.mInterstitialAd.show();
                    }else {
                        startActivity(new Intent(MainActivity.this, OurAdActivity.class));
                    }
                }
            }
        }else{
            MyDynamicToast.informationMessage(MainActivity.this, "Check your internet connection..");
        }
        adapter = new MoreListAdapter();
        lv.setAdapter(adapter);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                cleartext.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);
                meaningCardView.setVisibility(View.INVISIBLE);
                MainActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initializeVars() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseInstance.getReference("ratingCount");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingValueFromFrirebase = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError + "", Toast.LENGTH_SHORT).show();

            }
        });
        englishWord = (TextView) findViewById(R.id.meaning_for_word);
        hindiMeaning = (TextView) findViewById(R.id.meaning);
        meaningCardView = (RelativeLayout) findViewById(R.id.meaning_cardView);
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.search_view);

        cleartext = (ImageView) findViewById(R.id.cross1);
        cleartext.setVisibility(View.INVISIBLE);
        cleartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                cleartext.setVisibility(View.INVISIBLE);
            }
        });

    }
    class MoreListAdapter extends ArrayAdapter<String>{

        MoreListAdapter() {
            super(MainActivity.this, R.layout.list_item, getList());
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row;
            final String listItem = getItem(position);
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.list_item, parent, false);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(R.id.english_word);
            CardView cardView = (CardView) row.findViewById(R.id.item_card);

            tv.setText(listItem);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aviLoader.setVisibility(View.VISIBLE);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callSearchFun(listItem);
                        }
                    }, 1000);
                }
            });
            return row;
        }
    }

    private void callSearchFun(String item) {
        JSONObject jsonObject;
        try {
            jsonObject = loadJSONFromAsset(item);
            englishWord.setText(jsonObject.getString("eng_word"));
            hindiMeaning.setText(jsonObject.getString("hindi_meaning"));
            lv.setVisibility(View.INVISIBLE);
            meaningCardView.setVisibility(View.VISIBLE);
            aviLoader.setVisibility(View.GONE);
            ratingLocal++;
            if (ratingLocal.equals(ratingValueFromFrirebase)) {

                if(!DataHandler.getInstance().isRatingDone(MainActivity.this) ) {
                    Intent i = new Intent(MainActivity.this, RatingActivity.class);
                    startActivity(i);
                }
                ratingLocal = 0;
            }
            if(isConnectedToInternet()){
                if(StaticDataHandler.getInstance().getAdonMainScreen() != null) {
                    if(StaticDataHandler.getInstance().getAdonMainScreen().equals("true")){
                        if(splashObj != null && splashObj.mInterstitialAd.isLoaded()){
                            splashObj.mInterstitialAd.show();
                        }else {
                            startActivity(new Intent(MainActivity.this,OurAdActivity.class));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public JSONObject loadJSONFromAsset(String number) throws JSONException {
        String json = null;
        String name;
        JSONObject searchObject = new JSONObject();
        try {
            InputStream is = getAssets().open("dictionary.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                JSONObject currObject = array.getJSONObject(i);
                name = currObject.getString("eng_word");

                if(name.equals(number))
                {
                    searchObject = currObject;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return searchObject;
    }

    public List<String> getList() {
        List<String> questions = new ArrayList();
        List<String> questionsids = new ArrayList();
        StringBuffer sb = new StringBuffer();
        String json = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "dictionary.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String myjsonstring = sb.toString();
        // Try to parse JSON
        try {
            JSONArray jsonArray = new JSONArray(myjsonstring);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);
                int id = jsonObj.getInt("id");
                String eng_word = jsonObj.getString("eng_word");
                questions.add(jsonObj.getString("eng_word"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return questions;
    }

    @Override
    public void onBackPressed() {

        if (meaningCardView.getVisibility() == View.VISIBLE) {
            meaningCardView.setVisibility(View.INVISIBLE);
            lv.setVisibility(View.VISIBLE);
        } else if (ratingLocal.equals(ratingValueFromFrirebase)) {
            Intent i = new Intent(MainActivity.this, RatingActivity.class);
            startActivity(i);
        } else {
            super.onBackPressed();
        }

    }
}
