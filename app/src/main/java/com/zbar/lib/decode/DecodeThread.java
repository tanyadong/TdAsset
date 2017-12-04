package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Looper;


import com.mobile.tiandy.asset.more.MfrmQRCode;

import java.util.concurrent.CountDownLatch;


/**
 * @author tanyadong
 * @Description: 解码线程
 * @date 2017/5/11  10:55
 */
final class DecodeThread extends Thread {

	MfrmQRCode activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(MfrmQRCode activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
