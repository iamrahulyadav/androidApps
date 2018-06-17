package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mehndidesign.offline2018.R;
import com.mehndidesign.offline2018.helper.Designs;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FavFullScreenImageAdapter extends PagerAdapter {
    private Context _activity;
    private List<Uri> _imagePaths;
    private LayoutInflater inflater;

    public FavFullScreenImageAdapter(Context _activity, List<Uri> _imagePaths) {
        this._activity = _activity;
        this._imagePaths = _imagePaths;
    }

    @Override
    public int getCount() {
        return _imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        imgDisplay.setImageURI(_imagePaths.get(position));
//        Picasso.with(_activity).load(_imagePaths.get(position)).into(imgDisplay);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
//        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
