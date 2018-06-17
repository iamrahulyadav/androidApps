package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.helper.Designs;
import com.mehndidesign.offline2018.R;

import java.util.List;

/**
 * Created by ABC on 5/24/2018.
 */

public class LatestDesignsAdapter extends RecyclerView.Adapter<LatestDesignsAdapter.LatestDesignsViewHolder> {

    private Context context;
    List<Designs> designsList;
    private FullScreenImageAdapter fullScreenImageAdapter;

    public LatestDesignsAdapter(Context context, List<Designs> designsList) {
        this.context = context;
        this.designsList = designsList;
    }

    @Override
    public LatestDesignsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_designs_list_item, parent, false);
        return new LatestDesignsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LatestDesignsViewHolder holder, final int position) {

        Designs latestDesigns = designsList.get(position);
        int latestDesingImage = latestDesigns.getDesigns();

        holder.latestDesign.setBackgroundResource(latestDesingImage);
        holder.latestDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "latest");
                i.putExtra("position", position);
                context.startActivity(i);
//                fullScreenImageAdapter = new FullScreenImageAdapter(context, designsList );
//                viewPager = (ViewPager) view.findViewById(R.id.fullViewpager);
//                viewPager.setAdapter(fullScreenImageAdapter);
            }
        });

    }

    @Override
    public int getItemCount() {
        return designsList.size();
    }

    public class LatestDesignsViewHolder extends RecyclerView.ViewHolder{

        public CardView latestDesign;


        public LatestDesignsViewHolder(View itemView) {
            super(itemView);
            latestDesign = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);

        }
    }
}