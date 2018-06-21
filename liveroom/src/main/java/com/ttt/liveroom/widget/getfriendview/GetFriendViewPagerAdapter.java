package com.ttt.liveroom.widget.getfriendview;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by 刘景 on 2017/06/11.
 */

public class GetFriendViewPagerAdapter extends PagerAdapter {
    private List<View> mPageViews;

    public GetFriendViewPagerAdapter(List<View> pageViews) {
        super();
        this.mPageViews = pageViews;
    }

    // 显示数目
    @Override
    public int getCount() {
        return mPageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        if (((ViewPager) arg0).getChildAt(arg1) == null) {
            ((ViewPager) arg0).addView(mPageViews.get(arg1));
            return mPageViews.get(arg1);
        } else {
            return null;
        }
    }
}
