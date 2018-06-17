package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.NavigationMain;
import com.mehndidesign.offline2018.R;
import com.mehndidesign.offline2018.helper.Designs;

import java.util.List;

public class FingersDesignsAdapter extends RecyclerView.Adapter<FingersDesignsAdapter.FingersDesignsViewHolder> {

    private List<Designs> fingersDesignsList;
    private Context context;


    public FingersDesignsAdapter(Context context, List<Designs> fingersDesignsList) {
        this.context = context;
        this.fingersDesignsList = fingersDesignsList;
    }

    @Override
    public FingersDesignsAdapter.FingersDesignsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.latest_designs_list_item, parent, false);
       return new FingersDesignsAdapter.FingersDesignsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FingersDesignsAdapter.FingersDesignsViewHolder holder, final int position) {

        Designs fingerDesigns = fingersDesignsList.get(position);
        int fingerDesignImage = fingerDesigns.getDesigns();

        holder.fingersDesingsCardView.setBackgroundResource(fingerDesignImage);
        holder.fingersDesingsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "finger_designs");
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fingersDesignsList.size();
    }

    public class FingersDesignsViewHolder extends RecyclerView.ViewHolder {

        public CardView fingersDesingsCardView;

        public FingersDesignsViewHolder(View itemView) {
            super(itemView);
            fingersDesingsCardView = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);
        }
    }
}
