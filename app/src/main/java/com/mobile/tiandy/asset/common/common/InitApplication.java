package com.mobile.tiandy.asset.common.common;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.support.compat.BuildConfig;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.util.CommomDialog;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.main.MainActivity;
import com.mobile.tiandy.asset.more.MfrmQRCode;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.decode.CaptureActivityHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 */
public class InitApplication extends Application implements OnResponseListener<String> {
	private static InitApplication instance;
	private RequestQueue queue;
	private Object cancelObject = new Object();
	private List<String> listPlace;
	public static InitApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		NoHttp.initialize(this);
		Logger.setDebug(true);
		Logger.setTag("NoHttp");
		initNoHttp();

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		if (!FilePathInit()) {
			Log.e(TAG, "!FilePathInit()");
		}
		queue = NoHttp.newRequestQueue();
		listPlace = new ArrayList<>();

		getAssetPlace();
	}

	private void getAssetPlace() {
		String uri = AppMacro.REQUEST_URL + "/place/list";
		Request<String> request = NoHttp.createStringRequest(uri);
		request.cancelBySign(cancelObject);
		queue.add(0, request, this);
	}

	public boolean FilePathInit() {

		try {
			// 检测是否已经创建
			File dir = new File(AppMacro.APP_PATH);
			// 检测/创建数据库的文件夹
			if (dir.exists()) {
			} else {
				dir.mkdir();
			}

			// 崩溃日志文件夹
			dir = new File(AppMacro.CRASH_MESSAGE_PATH);
			if (dir.exists()) {
			} else {
				dir.mkdir();
			}
		} catch (Exception e) {
			Log.e(TAG, "error " + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}


	private void initNoHttp() {
		Logger.setDebug(true); // 开启NoHttp调试模式。
		Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。

		NoHttp.initialize(this, new NoHttp.Config()
				.setConnectTimeout(10 * 1000) // 全局连接超时时间，单位毫秒。
				.setReadTimeout(10 * 1000) // 全局服务器响应超时时间，单位毫秒。
				.setCacheStore(
						new DBCacheStore(this) // 配置缓存到数据库。
								.setEnable(false)// true启用缓存，fasle禁用缓存。
				)
				.setCookieStore(
						new DBCookieStore(this)
								.setEnable(false) // true启用自动维护Cookie，fasle禁用自动维护Cookie。
				)
				.setNetworkExecutor(new URLConnectionNetworkExecutor()) // 使用HttpURLConnection做网络层。
		);
	}

	@Override
	public void onStart(int i) {

	}

	@Override
	public void onSucceed(int i, Response<String> response) {
		if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
			String result = (String) response.get();
			if (result == null || "".equals(result)) {
				T.showShort(this, R.string.asset_check_failed);
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.has("code") && jsonObject.getInt("code") == 0) {
					JSONArray jsonArray  = jsonObject.getJSONArray("content");
					listPlace.clear();
					for (i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
						listPlace.add(jsonObject1.optString("placeName"));
					}
					LoginUtils.saveAssetPlace(this, listPlace);
				} else if (jsonObject.getInt("code") == -1) {
					if (jsonObject.has("message")) {
						T.showShort(this, jsonObject.getString("message"));
					}
				} else {
					T.showShort(this, R.string.asset_check_failed);
				}
			} catch (JSONException e) {
				T.showShort(this, R.string.asset_check_failed);
				e.printStackTrace();
			}
		} else {
			T.showShort(this, R.string.asset_check_failed);
		}
	}

	@Override
	public void onFailed(int i, Response<String> response) {
		Exception exception = response.getException();
		if (exception instanceof NetworkError) {
			T.showShort(this, R.string.network_error);
			return;
		}
		if (exception instanceof UnKnownHostError) {
			T.showShort(this, R.string.network_unknown_host_error);
			return;
		}
		if (exception instanceof SocketTimeoutException) {
			T.showShort(this, R.string.network_socket_timeout_error);
			return;
		}
		T.showShort(this, R.string.asset_check_failed);
	}

	@Override
	public void onFinish(int i) {

	}
}
