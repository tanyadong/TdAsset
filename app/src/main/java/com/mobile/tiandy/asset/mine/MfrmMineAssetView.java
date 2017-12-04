package com.mobile.tiandy.asset.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.common.CircleProgressBarView;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.vo.Asset;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
  * @date 创建时间 2017/9/5
  * @author tanyadong
  * @Description 我的资产
*/
public class MfrmMineAssetView extends BaseView implements BGARefreshLayout.BGARefreshLayoutDelegate {
	private ImageView scanQrcordImg;
	private ImageView userImg;
	private TextView titleTxt;
	private LinearLayout titleLiftLl, titleRightLl;
	private ListView expandableListView;
	private MyAssetListViewAdapter expandableListViewAdapter;
	public CircleProgressBarView circleProgressBarView;
	private TextView assetListNoDataTxt;
	private BGARefreshLayout bgaRefreshLayout;
	public MfrmMineAssetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setInflate() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_mine_asset, this);
	}

	@Override
	protected void initViews() {
		assetListNoDataTxt = (TextView) findViewById(R.id.txt_asset_list_no_data);
		circleProgressBarView = (CircleProgressBarView) findViewById(R.id.circleProgressBarView);
		titleLiftLl = (LinearLayout) findViewById(R.id.ll_title_left);
		titleRightLl = (LinearLayout) findViewById(R.id.ll_title_right);
		scanQrcordImg = (ImageView) findViewById(R.id.img_right);
		scanQrcordImg.setImageResource(R.drawable.title_qrcode_img);
		userImg = (ImageView) findViewById(R.id.img_back);
		userImg.setImageResource(R.drawable.user_img);
		titleTxt = (TextView) findViewById(R.id.txt_title);
		titleTxt.setText(getResources().getString(R.string.my_asset));
		expandableListView = (ListView) findViewById(R.id.expandablelistview);
		bgaRefreshLayout = (BGARefreshLayout) findViewById(R.id.bgaRefreshLayout);
		initFresh();
	}



	@Override
	protected void addListener() {
		titleLiftLl.setOnClickListener(this);
		titleRightLl.setOnClickListener(this);
		userImg.setOnClickListener(this);
		scanQrcordImg.setOnClickListener(this);
	}

	@Override
	protected void onClickListener(View v) {
		switch (v.getId()) {
			case R.id.ll_title_left:
			case R.id.img_back:
				if (super.delegate instanceof MfrmMineAssetDelegate) {
					((MfrmMineAssetDelegate) super.delegate).onClickLogoff();
				}
				break;
			case R.id.ll_title_right:
			case R.id.img_right:
				if (super.delegate instanceof MfrmMineAssetDelegate) {
					((MfrmMineAssetDelegate) super.delegate).onClickToQRCode();
				}
				break;
		default:
			break;
		}
	}
	/**
	 * @author  tanyadong
	 * @Title: setNoDataView
	 * @Description: 设置没有数据界面显示
	 * @date 2017/4/1 11:06
	 */
	public void setNoDataView(boolean isShow) {
		if (isShow) {
			assetListNoDataTxt.setVisibility(VISIBLE);
		} else {
			assetListNoDataTxt.setVisibility(GONE);
		}
	}

	/**
	 * 初始化上下拉刷新控件
	 */
	private void initFresh() {
		bgaRefreshLayout.setDelegate(this);
		//true代表开启上拉加载更多
		BGANormalRefreshViewHolder bgaNormalRefreshViewHolder = new BGANormalRefreshViewHolder(getContext(), false);
		bgaRefreshLayout.setRefreshViewHolder(bgaNormalRefreshViewHolder);
	}
	/**
	  * @author tanyadong
	  * @Title showMyAssetList
	  * @Description 刷新并显示数据
	  * @date 2017/9/8 14:44
	*/
	public void showMyAssetList(List<Asset> myAssetList) {
		if (myAssetList == null) {
			L.e("myAssetList == null");
			return;
		}
		if (expandableListViewAdapter == null) {
			expandableListViewAdapter = new MyAssetListViewAdapter(context,
					myAssetList);
			expandableListView.setAdapter(expandableListViewAdapter);
		} else {
			expandableListViewAdapter.update(myAssetList);
			expandableListViewAdapter.notifyDataSetChanged();
		}
	}


/**
  * @author tanyadong
  * @Title initData
  * @Description 初始化数据
  * @date 2017/9/5 21:29
*/
	@Override
	public void initData(Object... data) {

	}

	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		if (super.delegate instanceof MfrmMineAssetDelegate) {
			((MfrmMineAssetDelegate) super.delegate).pullDownRefresh();
		}
	}
	/**
	 * @author tanyadong
	 * @Title endRefreshLayout
	 * @Description 停止刷新
	 * @date 2017/9/12 17:28
	 */
	public void endRefreshLayout() {
		bgaRefreshLayout.endRefreshing();
	}
	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		return false;
	}


	/**
	  * @date 创建时间 2017/9/5
	  * @author tanyadong
	  * @Description ”我的“  代理
	*/
	public interface MfrmMineAssetDelegate {

		void onClickLogoff(); // 退出登录

		void onClickToQRCode();

		void pullDownRefresh(); //下拉刷新
	}

}
