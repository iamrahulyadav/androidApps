package com.mehndidesign.offline2018.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.helper.Designs;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by ABC on 5/25/2018.
 */

public class GridViewImageAdapter extends BaseAdapter {

    private Context context;
    private List<Designs> designsList;
    private List<String> favList;
    private int imageWidth;
    private String designType;

    public GridViewImageAdapter(Context context, List<Designs> designsList, int imageWidth, String designType) {
        this.context = context;
        this.designsList = designsList;
        this.imageWidth = imageWidth;
        this.designType = designType;
    }

    @Override
    public int getCount() {
        return designsList.size();
    }

    @Override
    public Object getItem(int i) {
        return designsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        Designs designs = designsList.get(position);
        int designsView = designs.getDesigns();

        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        imageView.setImageResource(designsView);
        imageView.setPadding(8, 8, 8, 8);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, FullViewActivity.class);
//                i.putExtra("designType", designType);
//                i.putExtra("position", position);
//                context.startActivity(i);
//
//            }
//        });
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Intent i = new Intent(context, FullViewActivity.class);
//                        i.putExtra("designType", designType);
//                        i.putExtra("position", position);
//                        context.startActivity(i);
//                        break;
//                }
//                return true;
//            }
//        });
        return imageView;
    }
}
