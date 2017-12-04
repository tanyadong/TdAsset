package com.mobile.tiandy.asset.common.util;


import android.util.Log;

import com.mobile.tiandy.asset.BuildConfig;


/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L
{
	public static boolean isDebug = BuildConfig.DEBUG;// 是否需要打印bug
	
	static String className;
	
    static String methodName;
    
	private L()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

    private static String createLog(String log){

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append("]");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
    }

    public static void e(String msg){
        if (!isDebug)
            return;

        // Throwable instance must be created before any methods  
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(msg));
    }

    public static void i(String msg){
        if (!isDebug)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(msg));
    }

    public static void d(String msg){
        if (!isDebug)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(msg));
    }

    public static void v(String msg){
        if (!isDebug)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(msg));
    }

    public static void w(String msg){
        if (!isDebug)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(msg));
    }

    public static void wtf(String msg){
        if (!isDebug)
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(msg));
    }
    
    // 下面是传入自定义tag的函数
 	public static void i(String tag, String msg)
 	{
 		if (!isDebug)
            return;
 			Log.i(tag, msg);
 	}

 	public static void d(String tag, String msg)
 	{
 		if (!isDebug)
            return;
 		
 			Log.d(tag, msg);
 	}

 	public static void e(String tag, String msg)
 	{
 		if (!isDebug)
            return;
 		
 			Log.e(tag, msg);
 	}

 	public static void v(String tag, String msg)
 	{
 		if (!isDebug)
            return;
 		
 			Log.v(tag, msg);
 	}

}