package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.R;
import com.mehndidesign.offline2018.helper.Designs;

import java.util.List;

public class GolTikkaDesignsAdapter extends RecyclerView.Adapter<GolTikkaDesignsAdapter.GolTikkaDesingsViewHolder> {

    private Context context;
    List<Designs> designsList;

    public GolTikkaDesignsAdapter(Context context, List<Designs> designsList) {
        this.context = context;
        this.designsList = designsList;
    }

    @Override
    public GolTikkaDesignsAdapter.GolTikkaDesingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_designs_list_item, parent, false);
        return new GolTikkaDesignsAdapter.GolTikkaDesingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GolTikkaDesignsAdapter.GolTikkaDesingsViewHolder holder, final int position) {
        Designs goltikkaDesigns = designsList.get(position);
        int goltikkaDesingImage = goltikkaDesigns.getDesigns();

        holder.latestDesign.setBackgroundResource(goltikkaDesingImage);
        holder.latestDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "goltikka");
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return designsList.size();
    }

    public class GolTikkaDesingsViewHolder extends RecyclerView.ViewHolder {
        public CardView latestDesign;

        public GolTikkaDesingsViewHolder(View itemView) {
            super(itemView);
            latestDesign = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);
        }
    }
}
