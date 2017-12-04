package com.mobile.tiandy.asset.mine;


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
import com.mobile.tiandy.asset.more.MfrmQRCode;

import java.util.List;

public class MyAssetListViewAdapter extends BaseAdapter implements View.OnClickListener {
	private Context context;
	private List<Asset> assets;
	private LayoutInflater layoutInflater;

	public MyAssetListViewAdapter(Context context, List<Asset> assetList) {
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
		final Holder holder;
		if (view == null) {
			view = layoutInflater.inflate(
					R.layout.item_my_asset_list, null);
			holder = new Holder();
			holder.assetNumTxt = (TextView) view
					.findViewById(R.id.txt_asset_device_num);
			holder.assetNameTxt = (TextView) view
					.findViewById(R.id.txt_asset_device_name);
			holder.assetStateTxt = (TextView) view
					.findViewById(R.id.txt_asset_process_state);

			holder.assetId = (TextView) view
					.findViewById(R.id.asset_id);
			holder.assetName = (TextView) view
					.findViewById(R.id.asset_name_and_num);
			holder.assetProcessState = (ImageView) view
					.findViewById(R.id.asset_process_state);

			holder.parentItemRl = (RelativeLayout) view.findViewById(R.id.rl_item_parent);
			holder.childItemLl = (LinearLayout) view.findViewById(R.id.ll_item_child);
			holder.parentItemLineView = (ImageView) view.findViewById(R.id.view_item_parent_line);
			holder.bottomLineView = view.findViewById(R.id.bottom_line_view);
			holder.qrcodeImg = (ImageView) view.findViewById(R.id.img_qrcode);
			holder.qrcodeLl = (LinearLayout) view.findViewById(R.id.ll_qrcode);
			holder.assetPlaceTxt = (TextView) view.findViewById(R.id.txt_asset_place);
            view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.assetId.setText("资产"+(position + 1) + ":");
		String codeId = assets.get(position).getCodeId();
		String coid = codeId.substring(codeId.lastIndexOf(".") + 1, codeId.length());
		holder.assetName.setText(assets.get(position).getName() + "(" + coid + ")");
		if (assets.get(position).getState() == 1) {// 选中勾选状态
			holder.assetProcessState
					.setImageResource(R.drawable.myasset_checked);
		} else {// 未选中状态
			holder.assetProcessState
					.setImageResource(R.drawable.myasset_unchecked);
		}
		String assetName = assets.get(position).getName();
		holder.assetNameTxt.setText(assetName);
		holder.assetNumTxt.setText(codeId);
        holder.assetPlaceTxt.setText(assets.get(position).getRealPlace());
		if (assets.get(position).getState() == 1) {// 已认证
			holder.assetStateTxt
					.setText(R.string.authenticated);
			holder.assetStateTxt.setTextColor(context.getResources().getColor(R.color.green_light));
			holder.qrcodeLl.setVisibility(View.GONE);
		} else {// 未选中状态
			holder.assetStateTxt
					.setText(context.getResources().getString(R.string.unauthorized) + context.getResources().getString(R.string.qrcode_authenticate));
			holder.assetStateTxt.setTextColor(context.getResources().getColor(R.color.read));
			holder.qrcodeLl.setVisibility(View.VISIBLE);
		}
		holder.parentItemRl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (assets.get(position).isExpand()) {
					holder.parentItemLineView.setVisibility(View.VISIBLE);
					holder.childItemLl.setVisibility(View.GONE);
					assets.get(position).setExpand(false);
				} else {
					holder.parentItemLineView.setVisibility(View.GONE);
					holder.childItemLl.setVisibility(View.VISIBLE);
					assets.get(position).setExpand(true);
				}
			}
		});
		holder.qrcodeLl.setOnClickListener(this);
		holder.qrcodeImg.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_qrcode:
			case R.id.img_qrcode:
				Intent intent = new Intent(context, MfrmQRCode.class);
				context.startActivity(intent);
				break;
		}
	}

	private class Holder {
		TextView assetId;
		TextView assetName;
		ImageView assetProcessState;

		TextView assetNumTxt;
		TextView assetNameTxt;
		TextView assetStateTxt;
		TextView assetPlaceTxt;
		RelativeLayout parentItemRl;
		LinearLayout childItemLl;
		ImageView parentItemLineView;
		View bottomLineView;
		ImageView qrcodeImg;
		LinearLayout qrcodeLl;
	}

}
