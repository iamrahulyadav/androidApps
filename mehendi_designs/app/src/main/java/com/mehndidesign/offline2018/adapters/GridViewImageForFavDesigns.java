package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.R;
import com.mehndidesign.offline2018.helper.Designs;

import java.util.List;

public class GridViewImageForFavDesigns extends BaseAdapter {

    private Context context;
    private List<String> favList;
    private int imageWidth;
    private String designType;

    public GridViewImageForFavDesigns(Context context, List<String> favList, int imageWidth, String designType) {
        this.context = context;
        this.favList = favList;
        this.imageWidth = imageWidth;
        this.designType = designType;
    }
    @Override
    public int getCount() {
        return favList.size();
    }

    @Override
    public Object getItem(int position) {
        return favList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        String imag = favList.get(position);
        int imageValue = Integer.parseInt(imag);
        Designs designs = new Designs(imageValue);
        int design = designs.getDesigns();
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        imageView.setImageResource(design);
        imageView.setPadding(8, 8, 8, 8);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, FullViewActivity.class);
//                i.putExtra("designType", designType);
//                i.putExtra("position", position);
//                context.startActivity(i);
//            }
//        });
        return imageView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
