package com.olgaz.testnytimesmostpopular.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.olgaz.testnytimesmostpopular.api.ApiClient;
import com.olgaz.testnytimesmostpopular.fragments.MostPopularFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MostPopularFragment.newInstance(ApiClient.EMAILED);
            case 1:
                return MostPopularFragment.newInstance(ApiClient.SHARED);
            case 2:
                return MostPopularFragment.newInstance(ApiClient.VIEWED);
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
