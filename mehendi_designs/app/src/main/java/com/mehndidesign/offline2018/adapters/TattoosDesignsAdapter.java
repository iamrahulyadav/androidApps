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

public class TattoosDesignsAdapter extends RecyclerView.Adapter<TattoosDesignsAdapter.TattosViewHolder> {

    private List<Designs> tattosDesignsList;
    private Context context;


    public TattoosDesignsAdapter(Context context, List<Designs> tattosDesignsList) {

        this.context = context;
        this.tattosDesignsList = tattosDesignsList;

    }

    @Override
    public TattoosDesignsAdapter.TattosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_designs_list_item, parent, false);
        return new TattoosDesignsAdapter.TattosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TattoosDesignsAdapter.TattosViewHolder holder, final int position) {

        Designs designs = tattosDesignsList.get(position);
        int tattooImage = designs.getDesigns();

        holder.tattosCardView.setBackgroundResource(tattooImage);
        holder.tattosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullViewActivity.class);
                i.putExtra("designType", "tattoos");
                i.putExtra("position", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tattosDesignsList.size();
    }

    public class TattosViewHolder extends RecyclerView.ViewHolder {

        public CardView tattosCardView;

        public TattosViewHolder(View itemView) {
            super(itemView);

            tattosCardView = (CardView) itemView.findViewById(R.id.latest_designs_item_cardView);

        }
    }
}
