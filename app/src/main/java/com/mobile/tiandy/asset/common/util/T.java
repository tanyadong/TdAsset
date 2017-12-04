package com.mobile.tiandy.asset.common.util;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.mobile.tiandy.asset.BuildConfig;

/**
 * Toast统一管理类
 * 
 */
public class T
{

	private T()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;
	
	public static boolean isDebug = BuildConfig.DEBUG;// 是否需要打印bug

	/**
	 * 短时间显示Toastx
	 * 
	 * @param context
	 * @param message
	 */
	public static void debugShowShort(Context context, CharSequence message)
	{
		if (isDebug)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void debugShowShort(Context context, int message)
	{
		if (isDebug)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void debugShowLong(Context context, CharSequence message)
	{
		if (isDebug)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void debugShowLong(Context context, int message)
	{
		if (isDebug)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 短时间显示Toastx
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message)
	{
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message)
	{
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message)
	{
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message)
	{
		if (isShow)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration)
	{
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration)
	{
		if (isShow)
			Toast.makeText(context, message, duration).show();
	}
	
	    /** 
	    * @author  zheng
	    * @Title: showToast 
	    * @Description: TODO(自定义设置提示的显示时间的长短) 
	    * @date 2016-9-24 上午10:36:23 
	    */
	    public static void showToast(final Activity activity, final String word, final long time){
	        activity.runOnUiThread(new Runnable() { 
	            public void run() {
	                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
	                toast.show();
	                Handler handler = new Handler();
	                    handler.postDelayed(new Runnable() {
	                       public void run() {
	                           toast.cancel(); 
	                       }
	                }, time);
	            }
	        });
	    }

}