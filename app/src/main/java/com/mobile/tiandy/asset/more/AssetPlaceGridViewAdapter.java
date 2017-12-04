package com.mobile.tiandy.asset.more;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.vo.Asset;

import java.util.List;

public class AssetPlaceGridViewAdapter extends BaseAdapter implements View.OnClickListener {
	private Context context;
	private List<String> assets;
	private LayoutInflater layoutInflater;
	private AssetPlaceAdapterDelegate delegate;
	public AssetPlaceGridViewAdapter(Context context, List<String> assetList) {
		super();
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.assets = assetList;
	}

	public void update(List<String> data) {
		if (data == null) {
			L.e("data = null!");
			return;
		}
		this.assets = data;
	}


	@Override
	public int getCount() {
		if (assets == null) {
			L.e("data = null!");
			return 0;
		}
		return assets.size();
	}
	public void setDelegate(AssetPlaceAdapterDelegate delegate) {
		this.delegate = delegate;
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
		final Holder holder;
		if (view == null) {
			view = layoutInflater.inflate(
					R.layout.dlg_gridview_item, null);
			holder = new Holder();
			holder.llAssetPlace = (LinearLayout) view
					.findViewById(R.id.ll_asset_realplace);
			holder.assetPlace = (TextView) view.findViewById(R.id.txt_asset_realplace);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		holder.assetPlace.setText(assets.get(position));
		holder.llAssetPlace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (delegate != null) {
					delegate.onClickItem(assets.get(position));
				}
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {

	}

	private class Holder {
		TextView assetPlace;
		LinearLayout llAssetPlace;
	}
	interface AssetPlaceAdapterDelegate {
		void onClickItem(String place);
	}

}
