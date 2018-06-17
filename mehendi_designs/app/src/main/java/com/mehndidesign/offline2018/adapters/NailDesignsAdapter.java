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

public class NailDesignsAdapter extends RecyclerView.Adapter<NailDesignsAdapter.NailDesingsViewHolder> {


    private List<Designs> nailDesignsList;
    private Context context;


    public NailDesignsAdapter(Context context, List<Designs> nailDesignsList) {
        this.context = context;
        this.nailDesignsList = nailDesignsList;
    }

    @Override
    public NailDesignsAdapter.NailDesingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_designs_list_item, parent, false);
        return new NailDesignsAdapter.NailDesingsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(NailDesignsAdapter.NailDesingsViewHolder holder, final int position) {

        Designs nailDesigns = nailDesignsList.get(position);
        int nailDesignImage = nailDesigns.getDesigns();

        holder.nailDesignsCardView.setBackgroundResource(nailDesignImage);
        holder.nailDesignsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "nail_designs");
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nailDesignsList.size();
    }

    public class NailDesingsViewHolder extends RecyclerView.ViewHolder {

        public CardView  nailDesignsCardView;

        public NailDesingsViewHolder(View itemView) {
            super(itemView);

            nailDesignsCardView = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);
        }
    }
}
