package com.mobile.tiandy.asset.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseFragmentController;
import com.mobile.tiandy.asset.common.util.CommomDialog;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.main.MfrmLoginController;
import com.mobile.tiandy.asset.main.MfrmUserController;


public class MfrmMoreController extends BaseFragmentController implements
		MfrmMoreView.MfrmMoreDelegate {
	private MfrmMoreView mfrmMineAssetView;
	@Override
	protected View onCreateViewFunc(LayoutInflater inflater,
									ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_more_controller,
				null);
		mfrmMineAssetView = (MfrmMoreView) view
				.findViewById(R.id.mfrm_more_view);
		mfrmMineAssetView.setDelegate(this);
		return view;
	}




	@Override
	protected void getBundleData() {

	}

	@Override
	protected void lazyLoad() {

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

	@Override
	public void onClickToUser() {
		Intent intent = new Intent(context, MfrmUserController.class);
		context.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
	}


	/**
	  * @author tanyadong
	  * @Title onClickToQRCode
	  * @Description  跳转到扫码界面
	  * @date 2017/9/7 22:50
	*/
	@Override
	public void onClickToQRCode() {
		Intent intent = new Intent(context, MfrmQRCode.class);
		startActivity(intent);
	}
	/**
	 * @author tanyadong
	 * @Title onClickToAbout
	 * @Description  跳转到关于界面
	 * @date 2017/9/7 22:50
	 */
	@Override
	public void onClickToAbout() {
		Intent intent = new Intent(context, MfrmAboutController.class);
		startActivity(intent);
	}

	@Override
	public void onClickLogoff() {
		new CommomDialog(context, R.style.dialog, getResources().getString(R.string.logoff_hint), new CommomDialog.OnCloseListener() {
			@Override
			public void onClick(Dialog dialog) {
				Intent intent = new Intent(context, MfrmLoginController.class);
				startActivity(intent);
				getActivity().finish();
			}

			@Override
			public void onClickContinue(Dialog dialog) {
				dialog.dismiss();
			}
		}).show();
	}
}
