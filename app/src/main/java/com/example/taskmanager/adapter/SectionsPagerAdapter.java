package com.example.taskmanager.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.taskmanager.Fragments.HomeFragment;
import com.example.taskmanager.Fragments.FavoriteFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new HomeFragment();
        if (position == 0)
            fragment = new HomeFragment();
        else if (position == 1) {
            fragment = new FavoriteFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
