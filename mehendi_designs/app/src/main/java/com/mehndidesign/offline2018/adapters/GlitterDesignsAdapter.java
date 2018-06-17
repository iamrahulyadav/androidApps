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

/**
 * Created by ABC on 5/28/2018.
 */

public class GlitterDesignsAdapter extends RecyclerView.Adapter<GlitterDesignsAdapter.GlitterDesignsViewHolder>{
    private Context context;
    List<Designs> designsList;
    private FullScreenImageAdapter fullScreenImageAdapter;


    public GlitterDesignsAdapter(Context context, List<Designs> designsList) {
        this.context = context;
        this.designsList = designsList;
    }

    @Override
    public GlitterDesignsAdapter.GlitterDesignsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_designs_list_item, parent, false);
        return new GlitterDesignsAdapter.GlitterDesignsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GlitterDesignsAdapter.GlitterDesignsViewHolder holder, final int position) {
        Designs latestDesigns = designsList.get(position);
        int latestDesingImage = latestDesigns.getDesigns();

        holder.glitterDesign.setBackgroundResource(latestDesingImage);
        holder.glitterDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "glitter");
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return designsList.size();
    }

    public class GlitterDesignsViewHolder extends RecyclerView.ViewHolder {
        public CardView glitterDesign;
        public GlitterDesignsViewHolder(View itemView) {
            super(itemView);
            glitterDesign = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);
        }
    }
}
