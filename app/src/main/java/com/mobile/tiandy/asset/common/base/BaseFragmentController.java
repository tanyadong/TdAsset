package com.mobile.tiandy.asset.common.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public abstract class BaseFragmentController extends Fragment {

	protected Context context;
	private View currentView;
	private FrameLayout frameLayout;
	
	/**
	 * 初始化界面xml 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view = super.onCreateView(inflater, container, savedInstanceState);
		getBundleData();
		currentView = onCreateViewFunc(inflater, container, savedInstanceState);
		this.context = currentView.getContext();
		return currentView;
	}

	public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
		currentView.setLayoutParams(layoutParams);
	}

	public FrameLayout.LayoutParams getCurrentViewParams() {
		if (currentView == null) {
			return null;
		}
		return (LayoutParams) currentView.getLayoutParams();
	}
	/**
	 * 获取参数
	 */
	protected abstract View onCreateViewFunc(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	

	/** Fragment当前状态是否可见 */
	protected boolean isVisible;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	/**
	 * 获取参数
	 */
	protected abstract void getBundleData();
	/**
	 * 可见
	 */
	protected void onVisible() {
		lazyLoad();
	}
	/**
	 * 不可见
	 */
	protected void onInvisible() {

	}


	/**
	 * 延迟加载
	 * 子类必须重写此方法
	 */
	protected abstract void lazyLoad();

}
