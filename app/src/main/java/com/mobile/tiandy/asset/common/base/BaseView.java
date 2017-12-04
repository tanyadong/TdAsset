package com.mobile.tiandy.asset.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public abstract class BaseView extends LinearLayout implements OnClickListener{

	protected Context context;
	protected View view;
	/**
	 *对应controller调用的delegate 
	 **/
	protected Object delegate;
	
	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		setInflate();
		//初始化页面控件
		initViews();
		//增加监听
		addListener();
	}

	@Override
	public void onClick(View arg0) {
		onClickListener(arg0);
	}
	
	/**
	 * 初始化界面xml 
	 */
	protected abstract void setInflate();
	
	/**
	 * Controller调用参数自己定义
	 */
	public abstract void initData(Object... data);
	
	/**
	 * 初始化变量
	 */
	protected abstract void initViews();
	
	/**
	 * 增加事件监听
	 */
	protected abstract void addListener();
	
	/**
	 * 设置接口
	 */
	public void setDelegate(Object delegate){
		this.delegate = delegate;
	}
	
	/**
	 * 点击事件激发方法
	 */
	protected abstract void onClickListener(View arg0);
	
	
}
