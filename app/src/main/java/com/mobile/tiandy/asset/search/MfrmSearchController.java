package com.mobile.tiandy.asset.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseFragmentController;
import com.mobile.tiandy.asset.common.common.AppMacro;
import com.mobile.tiandy.asset.common.common.InitApplication;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.Asset;
import com.mobile.tiandy.asset.main.MfrmUserController;
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

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


public class MfrmSearchController extends BaseFragmentController implements
		MfrmSearchView.MfrmSearchDelegate, OnResponseListener {
	private MfrmSearchView mfrmSearchView;
	private RequestQueue queue;
	private static final int SEARCH_ASSET_LIST = 1;
	private List<Asset> assetList;
	private int pageNo = 0;
	private static final int PAGE_SIZE = 10;//每页数据条数
	private static final int FIRST_PAGE = 0;//第几页
	private boolean refreshList = false;
	private boolean loadMoreList = false;
	private Object cancelObject = new Object();
	private int lastCount = 0;//上次请求数据个数
	private boolean mHasLoadedOnce;

	private boolean isPrepared;
	@Override
	protected View onCreateViewFunc(LayoutInflater inflater,
									ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_search_asset_controller,
				null);
		mfrmSearchView = (MfrmSearchView) view
				.findViewById(R.id.mfrm_search_asset_view);
		mfrmSearchView.setDelegate(this);
		queue = NoHttp.newRequestQueue();
		assetList = new ArrayList<>();
		isPrepared = true;
		refreshList = false;
		loadMoreList = false;
		lazyLoad();
		return view;
	}

	/**
	  * @author tanyadong
	  * @Title getSearchAssetData
	  * @Description 获取搜索数据
	  * @date 2017/9/9 10:42
	*/
	private void getSearchAssetData(String param, int pageNo) {
		String uri = AppMacro.REQUEST_URL + "/asset/query";
		Request<String> request = NoHttp.createStringRequest(uri);
		request.cancelBySign(cancelObject);
		request.add("param", param);
		request.add("page", pageNo);
		request.add("limit", PAGE_SIZE);
		queue.add(SEARCH_ASSET_LIST, request, this);
	}

	@Override
	protected void getBundleData() {
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		}
		getSearchAssetData("", FIRST_PAGE);
		mHasLoadedOnce = true;
	}

	@Override
	public void onResume() {
		super.onResume();
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
	public void onClickSearch(String strSearch) {
		refreshList = false;
		loadMoreList = false;
		getSearchAssetData(strSearch, FIRST_PAGE);
	}

	/**
	  * @author tanyadong
	  * @Title onClickPullDown
	  * @Description 下拉刷新
	  * @date 2017/9/16 14:10
	*/
	@Override
	public void onClickPullDown(String strSearch) {
		refreshList = true;
		getSearchAssetData(strSearch, FIRST_PAGE);
	}

	/**
	 * @author tanyadong
	 * @Title onClickLoadMore
	 * @Description 上啦加载
	 * @date 2017/9/16 14:10
	 */
	@Override
	public void onClickLoadMore(String strSearch) {
		loadMoreList = true;
		getSearchAssetData(strSearch, pageNo);
	}

	@Override
	public void onClickToDetail(Asset asset) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		intent.setClass(context, MfrmAssetDetailController.class);
		bundle.putSerializable("asset", asset);
		intent.putExtras(bundle);
		startActivity(intent);
	}


	@Override
	public void onStart(int i) {
		if (refreshList == true || loadMoreList == true) {
			return;
		}
		mfrmSearchView.circleProgressBarView.showProgressBar();
	}

	@Override
	public void onSucceed(int i, Response response) {

		if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
			String result = (String) response.get();
			assetList = analyzeAssetsData(result);
			mfrmSearchView.showSearchAssetList(assetList);
		}
	}

	/**
	  * @author tanyadong
	  * @Title analyzeAssetsData
	  * @Description 解析查询到的资产
	  * @date 2017/9/9 20:57
	*/
	private List<Asset> analyzeAssetsData(String result) {
		if (!loadMoreList) {
			if (assetList != null) {
				assetList.clear();
			}
		}
		if (null == result || "".equals(result)) {
			T.showShort(context, R.string.get_myasset_failed);
			reloadNoDataList();
			L.e("result == null");
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.has("code") && jsonObject.optInt("code") == 0) {
				JSONArray jsonArray = jsonObject.optJSONArray("content");
				mfrmSearchView.isLoadMore = true;
				if (jsonArray.length() <= 0) {
					if (loadMoreList) {
						mfrmSearchView.isLoadMore = false;
						T.showShort(getActivity(), R.string.check_asset_no_more);
					} else {
						reloadNoDataList();
					}
					return null;
				} else {
					mfrmSearchView.setNoDataView(false);
				}
				if (assetList == null){
					assetList = new ArrayList<>();
				}
				int arrCount = 0;
				if (assetList != null) {
					arrCount = assetList.size();
				}
				if (jsonArray.length() >= PAGE_SIZE) {
					pageNo++;
				} else {
					if (lastCount < PAGE_SIZE && arrCount > 0) {
						int index = (pageNo - 1) * PAGE_SIZE;//开始从某一位移除
						for (int i = index; i < arrCount; i++) {
							if (i >= index && i < index + lastCount) {
								if (index < assetList.size()){
									assetList.remove(index);
								}
							}
						}
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					Asset asset = new Asset();
					JSONObject jsonObjectContent = jsonArray.getJSONObject(i);
					asset.setState(jsonObjectContent.getInt("state"));
					asset.setType(jsonObjectContent.getString("type"));
					asset.setCodeId(jsonObjectContent.getString("codeId"));
					asset.setJobId(jsonObjectContent.getString("jobId"));
					asset.setUserName(jsonObjectContent.optString("user"));
					asset.setName(jsonObjectContent.getString("name"));
					asset.setBoard(jsonObjectContent.getString("board"));
					asset.setBox(jsonObjectContent.getString("box"));
					asset.setBuild(jsonObjectContent.getString("build"));
					asset.setCenter(jsonObjectContent.getString("center"));
					asset.setCost(jsonObjectContent.getString("cost"));
					asset.setCostIt(jsonObjectContent.getString("costIt"));
					asset.setCount(jsonObjectContent.getString("count"));
					asset.setCpu(jsonObjectContent.getString("cpu"));
					asset.setDisk(jsonObjectContent.getString("disk"));
					asset.setFloor(jsonObjectContent.getString("floor"));
					asset.setHardDriver(jsonObjectContent.getString("hardDriver"));
					asset.setLeavePlace(jsonObjectContent.getString("leavePlace"));
					asset.setMemory(jsonObjectContent.getString("memory"));
					asset.setModel(jsonObjectContent.getString("model"));
					asset.setMoney(jsonObjectContent.getString("money"));
					asset.setOther(jsonObjectContent.getString("other"));
					asset.setPart(jsonObjectContent.getString("part"));
					asset.setPlace(jsonObjectContent.getString("place"));
					asset.setRealPlace(jsonObjectContent.getString("realPlace"));
					asset.setSaver(jsonObjectContent.getString("saver"));
					asset.setRealSaver(jsonObjectContent.getString("realSaver"));
					asset.setPrice(jsonObjectContent.getString("price"));
					asset.setSoftDriver(jsonObjectContent.getString("softDriver"));
					asset.setTime(jsonObjectContent.getString("time"));
					asset.setVideoCard(jsonObjectContent.getString("videoCard"));
					assetList.add(asset);
				}
				lastCount = jsonArray.length();
			} else {
				T.showShort(context, R.string.get_myasset_failed);
				reloadNoDataList();
				return null;
			}
		} catch (JSONException e) {
			T.showShort(context, R.string.get_myasset_failed);
			reloadNoDataList();
			e.printStackTrace();
		}
		return  assetList;
	}

	/**
	  * @author tanyadong
	  * @Title reloadNoDataList
	  * @Description 无数据刷新列表
	  * @date 2017/9/9 20:59
	*/
	private void reloadNoDataList() {
		if (assetList == null || assetList.size() <= 0) {
			mfrmSearchView.setNoDataView(true);
			mfrmSearchView.showSearchAssetList(assetList);
		}
	}

	@Override
	public void onFailed(int i, Response response) {
		if (refreshList == true) {
			if (assetList != null) {
				assetList.clear();
			}
		}
		Exception exception = response.getException();
		reloadNoDataList();
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
		if (exception instanceof ConnectException) {
			T.showShort(context, R.string.network_error);
			return;
		}
		T.showShort(context, R.string.login_failed);
	}

	@Override
	public void onFinish(int i) {
		mfrmSearchView.circleProgressBarView.hideProgressBar();
		mfrmSearchView.endRefreshLayout();
		refreshList = false;
		loadMoreList = false;
	}
}
