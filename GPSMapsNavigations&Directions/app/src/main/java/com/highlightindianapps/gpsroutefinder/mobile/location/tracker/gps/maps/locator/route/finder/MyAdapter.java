package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateContentAdView;

/**
 * Created by ABC on 4/23/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int NATIVE_APP_INSTALL_AD_VIEW_TYPE = 1;

    private static final int NATIVE_CONTENT_AD_VIEW_TYPE = 2;

    private List<Object> nearByPlacesLists;

    private Context context;

    private SharedPreferences sharedPreferencesLat, sharedPreferencesLang;
    String latitude, langitude;

    public MyAdapter(List<Object> nearByPlacesLists, Context context) {
        this.nearByPlacesLists = nearByPlacesLists;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType) {
            case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                View nativeAppInstallLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_app_install,
                        parent, false);
                return new ViewHolder(nativeAppInstallLayoutView);
            case NATIVE_CONTENT_AD_VIEW_TYPE:
                View nativeContentLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_content,
                        parent, false);
                return new ViewHolder(nativeContentLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                // Fall through.
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.near_by_places_list_item, parent, false);
                return new ViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = nearByPlacesLists.get(position);
        if (recyclerViewItem instanceof NativeAppInstallAd) {
            return NATIVE_APP_INSTALL_AD_VIEW_TYPE;
        } else if (recyclerViewItem instanceof NativeContentAd) {
            return NATIVE_CONTENT_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
//        final NearByPlacesList student = nearByPlacesLists.get(position);

        switch (viewType) {

            case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                NativeAppInstallAd appInstallAd = (NativeAppInstallAd) nearByPlacesLists.get(position);
                populateAppInstallAdView(appInstallAd, (NativeAppInstallAdView) holder.itemView);
                break;
            case NATIVE_CONTENT_AD_VIEW_TYPE:
                NativeContentAd contentAd = (NativeContentAd) nearByPlacesLists.get(position);
                populateContentAdView(contentAd, (NativeContentAdView) holder.itemView);
                break;
            case MENU_ITEM_VIEW_TYPE:
            default:

                final NearByPlacesList student = (NearByPlacesList) nearByPlacesLists.get(position);
                holder.textName.setText(student.getName());
                holder.distance.setText(student.getDistance());
//        holder.placeType.setText(student.getPlaceType());
                holder.vicinity.setText(student.getVicinity());
//        holder.placeTypeImage.set(student.getPlaceType());
//
                holder.listViewCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent banksIntent = new Intent(context, MapsActivity.class);
                        banksIntent.putExtra("Map", "NearByPlaces");
                        banksIntent.putExtra("placeType", student.getPlaceType());
                        banksIntent.putExtra("destination", student.getVicinity());
                        banksIntent.putExtra("startAddress", student.getAddress());
                        context.startActivity(banksIntent);
//                Toast.makeText(context, "You Clicked " +student.getSname(), Toast.LENGTH_LONG).show();
                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return nearByPlacesLists.size();
    }

//    @Override
//    public int getItemViewType(int position)
//    {
//        if (position % 4 == 0)
//            return AD_TYPE;
//        return CONTENT_TYPE;
//    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textName, duration, vicinity, distance;;
        public CardView listViewCard;
        public ImageView placeTypeImage;


        public ViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.name);
//            distance = (TextView) itemView.findViewById(R.id.distance);
            listViewCard = (CardView) itemView.findViewById(R.id.place_type_card);
//            placeType = (TextView) itemView.findViewById(R.id.place_type);
            distance = (TextView) itemView.findViewById(R.id.distance);
            vicinity = (TextView) itemView.findViewById(R.id.vicinity);
//            placeTypeImage = (ImageView) itemView.findViewById(R.id.place_type_image);
        }
    }
}

