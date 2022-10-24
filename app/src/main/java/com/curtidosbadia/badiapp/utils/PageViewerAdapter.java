package com.curtidosbadia.badiapp.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PageViewerAdapter  extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private int count;

    public PageViewerAdapter(FragmentManager fm){ super(fm);}

    public void setItems(ArrayList<Fragment> fragments){
        if(fragments != null){
            this.fragments = fragments;
            count = fragments.size();
        }else{
            count = 0;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount(){
        return count;
    }

}