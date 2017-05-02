package com.example.arena.oracle.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Arena on 2017/4/17.
 */


public class BannerAdapter extends PagerAdapter {

    private List<SimpleDraweeView> bannerImages; // 滑动的图片集合


    public BannerAdapter(List<SimpleDraweeView> bannerImages)
    {
        this.bannerImages = bannerImages;
    }
    @Override
    public int getCount() {
        return bannerImages.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(bannerImages.get(arg1));
        return bannerImages.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }
}

