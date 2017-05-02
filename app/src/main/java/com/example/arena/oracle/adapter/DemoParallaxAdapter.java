package com.example.arena.oracle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


import com.example.arena.oracle.fargment.DemoParallaxFragment;

import java.util.ArrayList;

//继承自Pager Adapter，这个界面不显示时候会释放资源
public class DemoParallaxAdapter extends FragmentStatePagerAdapter {

    private ArrayList<DemoParallaxFragment> mFragments;
    private ViewPager mPager;

    public DemoParallaxAdapter(FragmentManager fm) {
        super(fm);

        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void add(DemoParallaxFragment parallaxFragment) {
        parallaxFragment.setAdapter(this);
        mFragments.add(parallaxFragment);
        notifyDataSetChanged();
        mPager.setCurrentItem(getCount() - 1, true);

    }

    public void remove(int i) {
        mFragments.remove(i);
        notifyDataSetChanged();
    }

    public void remove(DemoParallaxFragment parallaxFragment) {
        mFragments.remove(parallaxFragment);

        int pos = mPager.getCurrentItem();
        notifyDataSetChanged();

        //防止溢出
        mPager.setAdapter(this);
        if (pos >= this.getCount()) {
            pos = this.getCount() - 1;
        }
        mPager.setCurrentItem(pos, true);   //ViewPager切换到那页

    }

    @Override   //默认是POSITION_UNCHANGED,这里是强制pager的刷新
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setPager(ViewPager pager) {
        mPager = pager;
    }
}
