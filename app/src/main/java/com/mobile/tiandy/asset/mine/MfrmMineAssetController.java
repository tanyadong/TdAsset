package com.mobile.tiandy.asset.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseFragmentController;
import com.mobile.tiandy.asset.common.common.AppMacro;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.Asset;
import com.mobile.tiandy.asset.common.vo.User;
import com.mobile.tiandy.asset.main.MfrmUserController;
import com.mobile.tiandy.asset.more.MfrmQRCode;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


public class MfrmMineAssetController extends BaseFragmentController implements
		MfrmMineAssetView.MfrmMineAssetDelegate, OnResponseListener {
	private MfrmMineAssetView mfrmMineAssetView;
	private RequestQueue queue;
	private static final int GET_ASSET_LIST = 0;
	private Object cancelObject = new Object();
	private List<Asset> assetList;
	private boolean isRefresh;
	private boolean mHasLoadedOnce;
	private User user;
	private boolean isPrepared;
	@Override
	protected View onCreateViewFunc(LayoutInflater inflater,
									ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_mine_asset_controller,
				null);
		mfrmMineAssetView = (MfrmMineAssetView) view
				.findViewById(R.id.mfrm_mine_asset_view);
		mfrmMineAssetView.setDelegate(this);
		queue = NoHttp.newRequestQueue();
		assetList = new ArrayList<>();
		isPrepared = true;
		user = LoginUtils.getUserInfo(getActivity());
		return view;
	}


	/**
	  * @author tanyadong
	  * @Title getMyAsset
	  * @Description 获取我的资产
	  * @date 2017/9/8 19:07
	*/
	private void getMyAsset(String jobId) {
		String uri = AppMacro.REQUEST_URL + "/asset/list";
		Request<String> request = NoHttp.createStringRequest(uri);
		request.cancelBySign(cancelObject);
		request.add("jobid", jobId);
		queue.add(GET_ASSET_LIST, request, this);
	}

	@Override
	protected void getBundleData() {

	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		}
		if (user == null) {
			return;
		}
		getMyAsset(user.getJobId());
	}


	/**
	 * @author tanyadong
	 * @Title: onResume
	 * @Description: 生命周期重进入
	 * @date 2016-9-19 下午6:57:28
	 */
	@Override
	public void onResume() {
		super.onResume();
		isRefresh = false;
		lazyLoad();
	}


	

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		queue.cancelBySign(cancelObject);
	}

	@Override
	public void onClickLogoff() {
		Intent intent = new Intent(context, MfrmUserController.class);
		context.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
	}

	@Override
	public void onClickToQRCode() {
		Intent intent = new Intent(context, MfrmQRCode.class);
		startActivity(intent);
	}
	/**
	  * @author tanyadong
	  * @Title pullDownRefresh
	  * @Description 下拉刷新
	  * @date 2017/9/12 17:21
	*/
	@Override
	public void pullDownRefresh() {
		isRefresh = true;
		if (user == null) {
			mfrmMineAssetView.endRefreshLayout();
			return;
		}
		getMyAsset(user.getJobId());
	}

	@Override
	public void onStart(int i) {
		if (isRefresh) {
			return;
		}
		if (mfrmMineAssetView.circleProgressBarView != null) {
			mfrmMineAssetView.circleProgressBarView.showProgressBar();
		}
	}

	@Override
	public void onSucceed(int i, Response response) {
		if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
			if (assetList != null && assetList.size() > 0) {
				assetList.clear();
			}
			String result = (String) response.get();
			if (result == null || "".equals(result)) {
				T.showShort(context, R.string.get_myasset_failed);
				mfrmMineAssetView.setNoDataView(true);
				return;
			}
			assetList = analyzeMyAssetData(result);
			mfrmMineAssetView.showMyAssetList(assetList);
		}
	}

	@Override
	public void onFailed(int i, Response response) {
		mfrmMineAssetView.circleProgressBarView.hideProgressBar();
		if (assetList != null && assetList.size() != 0) {
			assetList.clear();
		}
		mfrmMineAssetView.setNoDataView(true);
		Exception exception = response.getException();
		if (exception instanceof NetworkError) {
			T.showShort(context, R.string.network_error);
			return;
		}
		if (exception instanceof UnKnownHostError) {
			T.showShort(context, R.string.network_unknown_host_error);
			return;
		}
		if (exception instanceof SocketTimeoutException) {
			T.showShort(context, R.string.network_socket_timeout_error);
			return;
		}
		T.showShort(context, R.string.get_myasset_failed);
	}

	@Override
	public void onFinish(int i) {
		isRefresh = false;
		mfrmMineAssetView.endRefreshLayout();
		if (mfrmMineAssetView.circleProgressBarView != null) {
			mfrmMineAssetView.circleProgressBarView.hideProgressBar();
		}
	}

	/**
	  * @author tanyadong
	  * @Title analyzeMyAssetData
	  * @Description 解析我的资产
	  * @date 2017/9/9 19:55
	*/
	private List<Asset> analyzeMyAssetData(String result) {
		List<Asset> assetList = new ArrayList<>();
		if (result == null || result.equals("")) {
			T.showShort(context, R.string.get_myasset_failed);
			mfrmMineAssetView.setNoDataView(true);
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.has("code") && jsonObject.optInt("code") == 0) {
				JSONArray jsonArray = jsonObject.getJSONArray("content");
				if (jsonArray.length() <= 0) {
					mfrmMineAssetView.setNoDataView(true);
					return null;
				} else {
					mfrmMineAssetView.setNoDataView(false);
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					Asset asset = new Asset();
					JSONObject jsonObjectContent = jsonArray.getJSONObject(i);
					asset.setState(jsonObjectContent.getInt("state"));
					asset.setType(jsonObjectContent.getString("type"));
					asset.setCodeId(jsonObjectContent.getString("codeId"));
					asset.setJobId(jsonObjectContent.getString("jobId"));
					asset.setName(jsonObjectContent.getString("name"));
					asset.setRealPlace(jsonObjectContent.getString("realPlace"));
					asset.setRealSaver(jsonObjectContent.getString("realSaver"));
					assetList.add(asset);
				}
			} else {
				T.showShort(context, R.string.get_myasset_failed);
				mfrmMineAssetView.setNoDataView(true);
				return null;
			}
		} catch (JSONException e) {
			T.showShort(context, R.string.get_myasset_failed);
			mfrmMineAssetView.setNoDataView(true);
			e.printStackTrace();
		}
		return assetList;
	}
}
