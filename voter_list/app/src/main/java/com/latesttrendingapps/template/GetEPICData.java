package com.latesttrendingapps.template;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startapp.android.publish.gson.JsonArray;
import com.startapp.android.publish.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetEPICData extends AsyncTask<Object, String, String> {

    private LinearLayout addLayout;
    private TextView epicName, epicFatherName, epicDob, epicSex, epicAddress;
    private ProgressDialog progressDialog;
    private CardView detailsCardView;
    String url;
    Context context;
    String getData, name, fatherName, sex, dob, houseNo,part_name, district, state;
    String formatedDate;
    String errorMessage = null;
    TextView errorMsgTv;
    @Override
    protected String doInBackground(Object... objects) {

        epicName = (TextView) objects[0];
        epicFatherName = (TextView) objects[1];
        epicDob = (TextView) objects[2];
        epicSex = (TextView) objects[3];
        epicAddress = (TextView) objects[4];
        url = (String) objects[5];
        progressDialog = (ProgressDialog) objects[6];
        detailsCardView = (CardView) objects[7];
        errorMsgTv = (TextView) objects[8];
        addLayout = (LinearLayout) objects[9];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            getData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        JSONArray jsonElements;
        if (s !=null && !s.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject("response");
                if (jsonObject.getString("numFound").equals("1")) {
                    jsonElements = jsonObject.getJSONArray("docs");
                    jsonObject = jsonElements.getJSONObject(0);
                    name = jsonObject.getString("name");
                    fatherName = jsonObject.getString("rln_name");
                    sex = jsonObject.getString("gender");
                    dob = jsonObject.getString("dob");
                    houseNo = jsonObject.getString("house_no");
                    part_name = jsonObject.getString("part_name");
                    district = jsonObject.getString("district");
                    state = jsonObject.getString("state");

                    DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                    Date date = (Date) formatter.parse(dob);
                    System.out.println(date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);

                    epicName.setText(name);
                    epicFatherName.setText(fatherName);
                    epicSex.setText(sex);
                    epicDob.setText(formatedDate);
                    epicAddress.setText(houseNo + " , " + part_name + " , " + district + " , " + state);
                } else {
                    throw new JSONException("No Records found");
                }
            } catch (JSONException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
            } catch (ParseException e) {
                errorMessage = e.getMessage();
                e.printStackTrace();
            } finally {
                if (errorMessage != null) {
                    errorMsgTv.setVisibility(View.VISIBLE);
                    errorMsgTv.setText("No Records found");
                } else if(name != null ){
                    addLayout.setVisibility(View.INVISIBLE);
                    detailsCardView.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }
        } else {
            progressDialog.dismiss();
            errorMsgTv.setVisibility(View.VISIBLE);
            errorMsgTv.setText("Please enter a valid EPIC ID");
        }
    }
}
