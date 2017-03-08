package com.rja.snapchat.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.rja.snapchat.fragment.ChatFragment;
import com.rja.snapchat.fragment.EmptyFragment;
import com.rja.snapchat.fragment.StoriesFragment;

/**
 * Created by rjaylward on 3/3/17
 */

public class MainViewAdapter extends FragmentStatePagerAdapter {

    public MainViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return ChatFragment.create();
            case 1:
                return EmptyFragment.create();
            case 2:
                return StoriesFragment.create();
        };

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
