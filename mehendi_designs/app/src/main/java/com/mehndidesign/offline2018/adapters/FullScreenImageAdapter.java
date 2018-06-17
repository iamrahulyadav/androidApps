package com.mehndidesign.offline2018.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mehndidesign.offline2018.helper.Designs;
import com.mehndidesign.offline2018.R;

import java.util.List;

public class FullScreenImageAdapter extends PagerAdapter {

    public static int finalImage;
    private Context _activity;
    private List<Designs> _imagePaths;
    private LayoutInflater inflater;
    private FloatingActionButton fab;

    public FullScreenImageAdapter(Context _activity, List<Designs> _imagePaths) {
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


        Designs image = _imagePaths.get(position);

        finalImage = image.getDesigns();

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        imgDisplay.setImageResource(finalImage);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
