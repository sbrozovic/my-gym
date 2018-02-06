package com.example.sara.mygym.Modules.Welcome.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.sara.mygym.Common.Fragments.AboutUsFragment;
import com.example.sara.mygym.Modules.PriceList.Fragments.PriceListFragment;
import com.example.sara.mygym.Modules.Welcome.Fragments.LoginFragment;

/**
 * Created by Sara on 27.10.2017..
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PriceListFragment();
            case 1:
                return new LoginFragment();
            case 2:
                return new AboutUsFragment();
            default:
                return new LoginFragment();
        }
    }

}