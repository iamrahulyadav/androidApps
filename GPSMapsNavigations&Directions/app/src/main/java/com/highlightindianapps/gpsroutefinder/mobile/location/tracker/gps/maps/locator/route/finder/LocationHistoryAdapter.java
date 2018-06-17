package com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateAppInstallAdView;
import static com.highlightindianapps.gpsroutefinder.mobile.location.tracker.gps.maps.locator.route.finder.MyUtils.populateContentAdView;

/**
 * Created by ABC on 4/18/2018.
 */

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.ViewHolder> {

    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int NATIVE_APP_INSTALL_AD_VIEW_TYPE = 1;

    private static final int NATIVE_CONTENT_AD_VIEW_TYPE = 2;

    private List<Object> locationHistoryList;
    private Context context;

    public LocationHistoryAdapter(List<Object> locationHistoryList, Context context) {
        this.locationHistoryList = locationHistoryList;
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

                default:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.location_history_list_item, parent, false);
                    return new ViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = locationHistoryList.get(position);
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
        switch (viewType) {
            case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                NativeAppInstallAd appInstallAd = (NativeAppInstallAd) locationHistoryList.get(position);
                populateAppInstallAdView(appInstallAd, (NativeAppInstallAdView) holder.itemView);
                break;
            case NATIVE_CONTENT_AD_VIEW_TYPE:
                NativeContentAd contentAd = (NativeContentAd) locationHistoryList.get(position);
                populateContentAdView(contentAd, (NativeContentAdView) holder.itemView);
                break;
            case MENU_ITEM_VIEW_TYPE:
            default:
                LocationHistoryStorage student = (LocationHistoryStorage) locationHistoryList.get(position);

                holder.textName.setText(student.getAddress());
                holder.textMobile.setText(student.getmTime());
        }

    }

    @Override
    public int getItemCount() {
        return locationHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textMobile;
        public CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.location_history_address);
            textMobile = (TextView) itemView.findViewById(R.id.location_history_time);
            card = (CardView) itemView.findViewById(R.id.location_history_card);
        }
    }

}

