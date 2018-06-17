package com.latesttrendingapps.template;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionPageAdapter extends FragmentPagerAdapter {
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                SearchByEPICNoFragment searchByEPICFragment = new SearchByEPICNoFragment();
                return  searchByEPICFragment;
            case 1:
                SearchByDetailsFragment searchByDetails = new SearchByDetailsFragment();
                return searchByDetails;

            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Search by EPIC No.";
            case 1:
                return "Search by Details";
            default:
                return null;
        }

    }
}
