package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mehndidesign.offline2018.GridViewActivity;
import com.mehndidesign.offline2018.NavigationMain;
import com.mehndidesign.offline2018.helper.Designs;
import com.mehndidesign.offline2018.helper.ExploreDesigns;
import com.mehndidesign.offline2018.R;

import java.util.ArrayList;
import java.util.List;

public class ExploreMehndiDesignsAdapter extends RecyclerView.Adapter<ExploreMehndiDesignsAdapter.DesignsViewHolder> {

    private List<ExploreDesigns> designsList;
    private int textLayer = R.color.explore_text_layer;
    private Context context;
    private List<Designs> indianDesignList;

    public ExploreMehndiDesignsAdapter(Context context, List<ExploreDesigns> designsList) {
        this.context = context;
        this.designsList = designsList;
    }

    @Override
    public DesignsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_mehndi_list_item, parent, false);
        return new DesignsViewHolder(v);
    }

    public class DesignsViewHolder extends RecyclerView.ViewHolder {

        public CardView exploreMehndiCardView;
        public TextView cardMehndiName;


        public DesignsViewHolder(View itemView) {
            super(itemView);

            exploreMehndiCardView = (CardView) itemView.findViewById(R.id.explore_list_item_cardView);
            cardMehndiName = (TextView) itemView.findViewById(R.id.explore_list_item_mehndi_name);

        }
    }

    @Override
    public void onBindViewHolder(DesignsViewHolder holder, final int position) {
        ExploreDesigns exploreDesigns = designsList.get(position);
        int background_image = exploreDesigns.getDesignBackgroundImage();
        String designType = exploreDesigns.getDesignType();

        holder.exploreMehndiCardView.setBackgroundResource(background_image);
//        holder.cardMehndiName.setBackgroundResource(R.color.explore_text_layer);
        holder.cardMehndiName.setText(designType);


        holder.exploreMehndiCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, GridViewActivity.class);
                switch (position) {
                    case 0:
//                        indianDesignArrayList();
                        i.putExtra("designType", "indian");
                        context.startActivity(i);
                        break;
                    case 1:
                        i.putExtra("designType", "pakistani");
                        context.startActivity(i);
                        break;
                    case 2:
                        i.putExtra("designType", "arabic");
                        context.startActivity(i);
                        break;
                    case 3:
                        i.putExtra("designType", "moracann");
                        context.startActivity(i);
                        break;
                    default:
                        System.out.println("Default");
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return designsList.size();
    }


}
