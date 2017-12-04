package com.mobile.tiandy.asset.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.common.CircleProgressBarView;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.Asset;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
  * @date 创建时间 2017/9/5
  * @author tanyadong
  * @Description 搜索
*/
public class MfrmSearchView extends BaseView implements BGARefreshLayout.BGARefreshLayoutDelegate,AssetListViewAdapter.AssetListViewAdapterDelegate, AbsListView.OnScrollListener {
	private ImageView searchImg;
	private ImageView userImg;
	private TextView titleTxt;
	private LinearLayout titleLiftLl, titleRightLl;
	private EditText searchEditTxt;
	private ListView searchListView;
	private TextView assetListNoDataTxt;
	private AssetListViewAdapter assetListViewAdapter;
	public CircleProgressBarView circleProgressBarView;
	private ImageView searchDeleteImg;
	private BGARefreshLayout mRefreshLayout;
	public boolean isLoadMore;
	public MfrmSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setInflate() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_search_asset, this);
	}

	@Override
	protected void initViews() {
		titleLiftLl = (LinearLayout) findViewById(R.id.ll_title_left);
		titleRightLl = (LinearLayout) findViewById(R.id.ll_title_right);
		searchImg = (ImageView) findViewById(R.id.img_search);
		searchImg.setImageResource(R.drawable.bottom_search_normol);
		userImg = (ImageView) findViewById(R.id.img_back);
		userImg.setImageResource(R.drawable.user_img);
		searchEditTxt = (EditText) findViewById(R.id.edit_search);
		searchListView = (ListView) findViewById(R.id.search_asset_listview);
		assetListNoDataTxt = (TextView) findViewById(R.id.txt_asset_list_no_data);
		searchDeleteImg = (ImageView) findViewById(R.id.search_delete_imgview);
		circleProgressBarView = (CircleProgressBarView) findViewById(R.id.circleProgressBarView);
		mRefreshLayout = (BGARefreshLayout) findViewById(R.id.mRefreshLayout);
		initFresh();
	}

	/**
	  * @author tanyadong
	  * @Title endRefreshLayout
	  * @Description 停止刷新
	  * @date 2017/9/12 17:28
	*/
	public void endRefreshLayout() {
		mRefreshLayout.endRefreshing();
		mRefreshLayout.endLoadingMore();

	}
	/**
	 * 初始化上下拉刷新控件
	 */
	private void initFresh() {
		mRefreshLayout.setDelegate(this);
		//true代表开启上拉加载更多
		BGANormalRefreshViewHolder bgaNormalRefreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
		mRefreshLayout.setRefreshViewHolder(bgaNormalRefreshViewHolder);
	}

	@Override
	protected void addListener() {
		titleLiftLl.setOnClickListener(this);
		userImg.setOnClickListener(this);
		searchImg.setOnClickListener(this);
		titleRightLl.setOnClickListener(this);
		searchEditTxt.addTextChangedListener(new EditChangedListener());
		searchDeleteImg.setOnClickListener(this);
		searchListView.setOnScrollListener(this);
	}
	/**
	  * @author tanyadong
	  * @Title
	  * @Description  下拉
	  * @date 2017/9/16 14:08
	*/
	@Override
	public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
		if (super.delegate instanceof  MfrmSearchDelegate) {
			((MfrmSearchDelegate) super.delegate).onClickPullDown(searchEditTxt.getText().toString().trim());
		}
	}
	/**
	 * @author tanyadong
	 * @Title onBGARefreshLayoutBeginLoadingMore
	 * @Description  上拉
	 * @date 2017/9/16 14:08
	 */
	@Override
	public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
		if (super.delegate instanceof  MfrmSearchDelegate) {
			 ((MfrmSearchDelegate) super.delegate).onClickLoadMore(searchEditTxt.getText().toString().trim());
		}
		return isLoadMore;
	}


	/**
	  * @author tanyadong
	  * @Title closeKeyBord
	  * @Description 关闭软键盘
	  * @date 2017/9/17 15:21
	*/
	private void closeKeyBord(EditText editText) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0) ;
	}

	@Override
	public void onClickItem(Asset asset) {
		if (super.delegate instanceof MfrmSearchDelegate) {
			((MfrmSearchDelegate) super.delegate).onClickToDetail(asset);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		//有更多
		if(totalItemCount > visibleItemCount){
			//不满一屏
			isLoadMore = true;
		}else{
			isLoadMore = false;
		}
	}

	class EditChangedListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().equals("")) {
				searchDeleteImg.setVisibility(GONE);
			} else if (!s.toString().equals("") && s.toString().length() > 0){
				searchDeleteImg.setVisibility(VISIBLE);

			}
		}
	};
	@Override
	protected void onClickListener(View v) {
		switch (v.getId()) {
			case R.id.ll_title_left:
			case R.id.img_back:
				if (super.delegate instanceof MfrmSearchDelegate) {
					((MfrmSearchDelegate) super.delegate).onClickLogoff();
				}
				break;
			case R.id.ll_title_right:
			case R.id.img_search:
				String searchTxt = searchEditTxt.getText().toString().trim();
				closeKeyBord(searchEditTxt);
				if (super.delegate instanceof MfrmSearchDelegate) {
					((MfrmSearchDelegate) super.delegate).onClickSearch(searchTxt);
				}
				break;
			case R.id.search_delete_imgview:
				searchEditTxt.setText("");
				searchDeleteImg.setVisibility(GONE);
				break;
		default:
			break;
		}
	}
	/**
	 * @author tanyadong
	 * @Title showSearchAssetList
	 * @Description 刷新并显示数据
	 * @date 2017/9/8 14:44
	 */
	public void showSearchAssetList(List<Asset> myAssetList) {
		if (myAssetList == null) {
			L.e("myAssetList == null");
			return;
		}
		if (assetListViewAdapter == null) {
			assetListViewAdapter = new AssetListViewAdapter(context,
					myAssetList);
			searchListView.setAdapter(assetListViewAdapter);
			assetListViewAdapter.setDelegate(this);
		} else {
			assetListViewAdapter.update(myAssetList);
			assetListViewAdapter.notifyDataSetChanged();
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
	 * @author liuchenghe
	 * @Title: initData
	 * @Description: 初始化数据（并没有使用该方法，使用update方法）
	 * @date 2016-9-19 下午7:57:20
	 */
	@Override
	public void initData(Object... data) {

	}


	/**
	  * @date 创建时间 2017/9/5
	  * @author tanyadong
	  * @Description 查找
	*/
	public interface MfrmSearchDelegate {

		void onClickLogoff();

		void  onClickSearch(String searchTxt);

		void onClickPullDown(String searchTxt); //下拉刷新

		void onClickLoadMore(String searchTxt); //上拉加载

		void onClickToDetail(Asset asset); //上啦加载
 	}

}
