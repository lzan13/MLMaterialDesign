package net.melove.demo.design.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lzan13 on 2016/11/30.
 */

public class MLFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles;
    private Fragment[] mFragments;

    public MLFragmentPagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        mTabTitles = titles;
        mFragments = fragments;
    }

    @Override public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override public int getCount() {
        return mTabTitles.length;
    }
}
