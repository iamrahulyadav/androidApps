package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;

import com.mehndidesign.offline2018.FullViewActivity;
import com.mehndidesign.offline2018.GridViewActivity;

public class ImageAdapter extends BaseAdapter {

    private GridViewActivity gridViewActivity;
    Uri[] mUrls;
    private Context mContext;
    private int imageWidth;
    public ImageAdapter(Context c, Uri[] mUrls, int columnWidth)  {
        mContext = c;
        this.mUrls = mUrls;
        this.imageWidth = columnWidth;
    }
    @Override
    public int getCount() {
        return mUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
            imageView = new ImageView(mContext);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
                imageWidth));
        imageView.setImageURI(mUrls[position]);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, FullViewActivity.class);
                i.putExtra("designType", "fav_list");
                i.putExtra("position", position);
                mContext.startActivity(i);
            }
        });
        return imageView;

    }
}
