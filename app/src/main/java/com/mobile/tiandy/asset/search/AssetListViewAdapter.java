package com.mobile.tiandy.asset.search;


import java.util.List;


import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.vo.Asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;

public class AssetListViewAdapter extends BaseAdapter {
	private Context context;
	private AssetListViewAdapterDelegate delegate;
	private List<Asset> assets;
	private LayoutInflater layoutInflater;

	public AssetListViewAdapter(Context context, List<Asset> assetList) {
		super();
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.assets = assetList;
	}

	public void update(List<Asset> data) {
		if (data == null) {
			L.e("data = null!");
			return;
		}
		this.assets = data;
	}

	public void setDelegate(AssetListViewAdapterDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public int getCount() {
		if (assets == null) {
			L.e("data = null!");
			return 0;
		}
		return assets.size();
	}

	@Override
	public Object getItem(int position) {
		return assets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		Holder holder;
		if (view == null) {
			view = layoutInflater.inflate(
					R.layout.item_search_listview_adapter, null);
			holder = new Holder();
			holder.searchAssetId = (TextView) view.findViewById(R.id.srarch_assetnum_txt);
			holder.searchAssetName = (TextView) view.findViewById(R.id.srarch_assetname_txt);
			holder.searchJobNum = (TextView) view.findViewById(R.id.srarch_job_num_txt);
			holder.listItemLl = (LinearLayout) view.findViewById(R.id.search_listview_ll);
			holder.searchNameTxt = (TextView) view.findViewById(R.id.srarch_username_txt);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		if (assets != null) {
			holder.searchAssetId.setText(assets.get(position).getCodeId()); //编号
			holder.searchAssetName.setText(assets.get(position).getName() + ":"); //资产名称
			holder.searchJobNum.setText(assets.get(position).getJobId()); //工号
			holder.searchNameTxt.setText(assets.get(position).getUserName());
			holder.listItemLl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (delegate != null) {
						delegate.onClickItem(assets.get(position));
					}
				}
			});
		}
		return view;
	}

	private class Holder {
		public TextView searchJobNum;
		public TextView searchAssetName;
		public TextView searchAssetId;
		public LinearLayout listItemLl;
		public TextView searchNameTxt;
	}



	public interface AssetListViewAdapterDelegate {

		void onClickItem(Asset asset);// 点击每一个item
	}
}
