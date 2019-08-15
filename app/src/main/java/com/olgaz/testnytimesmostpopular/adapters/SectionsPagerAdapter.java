package com.olgaz.testnytimesmostpopular.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.olgaz.testnytimesmostpopular.fragments.MostEmailedFragment;
import com.olgaz.testnytimesmostpopular.fragments.MostSharedFragment;
import com.olgaz.testnytimesmostpopular.fragments.MostViewedFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MostEmailedFragment();
            case 1:
                return new MostSharedFragment();
            case 2:
                return new MostViewedFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "most emailed";
            case 1:
                return "most shared";
            case 2:
                return "most viewed";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
