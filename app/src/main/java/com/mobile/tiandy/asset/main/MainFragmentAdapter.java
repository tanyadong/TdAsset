package com.mobile.tiandy.asset.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.mobile.tiandy.asset.common.base.BaseFragmentController;

import java.util.List;

/**
  * @date 创建时间 2017/9/5
  * @author tanyadong
  * @Description viewpager adapter
*/
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<BaseFragmentController> list_fragment;
    public MainFragmentAdapter(FragmentManager fm, List<BaseFragmentController> list) {
        super(fm);
        list_fragment = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }
}
