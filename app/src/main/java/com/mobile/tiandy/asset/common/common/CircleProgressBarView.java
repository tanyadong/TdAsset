package com.mobile.tiandy.asset.common.common;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mobile.tiandy.asset.R;

public class CircleProgressBarView extends LinearLayout {
	/*
	 * 注：本类为自定义控件，在各个activity调用的时候只需将本类作为View引入到xml文件中，
	 * 特别注意在对应的activity中需要处理此view的OnTouchListener事件，防止用户操作穿透此view直接操作底层界面 引用方式：
	 */
	private Context context;
	private View view;
	private static final int VIDEOLADINGTIMER = 120000;
	private static final int handLerHideTimer = 3000;
	public ImageView imgView;
	// 以下两个参数为系统参数
	public CircleProgressBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.include_progressbar_view, this);
		imgView = (ImageView) findViewById(R.id.img_gif);
		//加载gif加载图
		Glide.with(getContext()).load(R.drawable.gif_background_img).into(imgView);
		view.setOnTouchListener(new MyTouch());
	}

	// 在activity中调用此方法显示此view
	public void showProgressBar() {
		view.setVisibility(View.VISIBLE);
	}

	// 在activity中调用此方法隐藏此view
	public void hideProgressBar() {
		view.setVisibility(View.GONE);
	}

	// 在activity中调用此方法3秒之后自动隐藏view
	public void handLerHide() {
		view.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				hideProgressBar();
			}
		}, handLerHideTimer); // 启动动画持续3秒钟
	}

	private class MyTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			return true;
		}

	}
}
