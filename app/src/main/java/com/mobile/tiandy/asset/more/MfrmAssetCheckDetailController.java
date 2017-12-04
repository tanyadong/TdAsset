package com.mobile.tiandy.asset.more;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.common.AppMacro;
import com.mobile.tiandy.asset.common.util.CommomDialog;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.main.MainActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;


public class MfrmAssetCheckDetailController extends BaseController implements
		MfrmAssetCheckDetailView.MfrmAssetCheckDetailDelegate, OnResponseListener<String> {
	private MfrmAssetCheckDetailView mfrmAssetCheckDetailView;
	private String jobId, codeId;
	private Object cancelObject = new Object();
	private RequestQueue queue;
	@Override
	protected void getBundleData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			L.e("bundle == null");
			return;
		}
		jobId = bundle.getString("jobid");
		codeId = bundle.getString("codeid");
	}

	@Override
	protected void onCreateFunc(Bundle savedInstanceState) {
		setContentView(R.layout.activity_asset_check_detail_controller);
		setTheme(R.style.dialog);

		if (Build.VERSION.SDK_INT >= 19) {
			Window window = this.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
			StatusBarUtil.StatusBarLightMode(this);
		}
		mfrmAssetCheckDetailView = (MfrmAssetCheckDetailView)findViewById(R.id.activity_asset_check_detail_view);
		mfrmAssetCheckDetailView.setDelegate(this);
		mfrmAssetCheckDetailView.initData(jobId, codeId);
		queue = NoHttp.newRequestQueue();
	}

	/**
	 * @author liuchenghe
	 * @Title: onResume
	 * @Description: 生命周期重进入
	 * @date 2016-9-19 下午6:57:28
	 */
	@Override
	public void onResume() {
		super.onResume();
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	/**
	 * @author tanyadong
	 * @Title: onClickBack
	 * @Description: 点击返回跳转到扫码界面
	 * @date 2017/12/1 0001 21:39
	 */

	@Override
	public void onClickBack() {
		Intent intent = new Intent();
		intent.setClass(this, MfrmQRCode.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClickAuthentication(String jobId, String codeId, String realPlace, String realSaver) {
		String uri = AppMacro.REQUEST_URL + "/asset/check";
		Request<String> request = NoHttp.createStringRequest(uri);
		request.cancelBySign(cancelObject);
		request.add("jobid", jobId);
		request.add("codeid", codeId);
		request.add("realPlace", realPlace);
		request.add("realSaver", realSaver);
		queue.add(0, request, this);
	}

	/**
	 * @author tanyadong
	 * @Title: onClickSetAttributes
	 * @Description: 设置背景透明度
	 * @date 2017/12/4 0004 9:41
	 */

	@Override
	public void onClickSetAttributes(float bgAlpha) {
		WindowManager.LayoutParams lp= this.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		if (bgAlpha == 1) {
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
		} else {
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
		}
		this.getWindow().setAttributes(lp);
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
					CommomDialog commomDialog = new CommomDialog(this, R.style.dialog);
					commomDialog.show();
					commomDialog.setNegativeButtonShow(true);
					commomDialog.setNegativeButton("否");
					commomDialog.setPositiveButton("是");
					commomDialog.setTitle(getResources().getString(R.string.asset_check_success));
					commomDialog.setListener(new CommomDialog.OnCloseListener() {
						@Override
						public void onClick(Dialog dialog) {
							dialog.dismiss();
							Intent intent = new Intent(MfrmAssetCheckDetailController.this, MainActivity.class);
							startActivity(intent);
							finish();
						}
						//继续
						@Override
						public void onClickContinue(Dialog dialog) {
							dialog.dismiss();
							Intent intent = new Intent(MfrmAssetCheckDetailController.this, MfrmQRCode.class);
							startActivity(intent);
							finish();
						}
					});
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

	@Override
	protected void onPause() {
		super.onPause();
		onClickSetAttributes(1);
	}
}
