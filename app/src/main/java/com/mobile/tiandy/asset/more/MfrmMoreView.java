package com.mobile.tiandy.asset.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.search.MfrmSearchView;


/**
  * @date 创建时间 2017/9/5
  * @author tanyadong
  * @Description 更多
*/
public class MfrmMoreView extends BaseView{
	private ImageView scanQrcordImg;
	private ImageView userImg;
	private TextView titleTxt;
	private LinearLayout titleLiftLl, titleRightLl;
	private RelativeLayout scanQrCodeRl, aboutRl, exitLogonRl;

	public MfrmMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setInflate() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_more, this);
	}

	@Override
	protected void initViews() {
		titleLiftLl = (LinearLayout) findViewById(R.id.ll_title_left);
		titleRightLl = (LinearLayout) findViewById(R.id.ll_title_right);
		titleRightLl.setVisibility(INVISIBLE);
		scanQrcordImg = (ImageView) findViewById(R.id.img_right);
		scanQrcordImg.setImageResource(R.drawable.user_img);
		userImg = (ImageView) findViewById(R.id.img_back);
		userImg.setImageResource(R.drawable.user_img);
		titleTxt = (TextView) findViewById(R.id.txt_title);
		titleTxt.setText(getResources().getString(R.string.tabbar_bottom_more));
		scanQrCodeRl = (RelativeLayout) findViewById(R.id.rl_scan_qrcode);
		aboutRl = (RelativeLayout) findViewById(R.id.rl_about);
		exitLogonRl = (RelativeLayout) findViewById(R.id.rl_exit_logon);
	}



	@Override
	protected void addListener() {
		userImg.setOnClickListener(this);
		titleLiftLl.setOnClickListener(this);
		scanQrCodeRl.setOnClickListener(this);
		aboutRl.setOnClickListener(this);
		exitLogonRl.setOnClickListener(this);
	}

	@Override
	protected void onClickListener(View v) {
		switch (v.getId()) {
			case R.id.ll_title_left:
			case R.id.img_back:
				if (super.delegate instanceof MfrmMoreDelegate) {
					((MfrmMoreDelegate) super.delegate).onClickToUser();
				}
				break;
			case R.id.rl_about:
				if (super.delegate instanceof MfrmMoreDelegate) {
					((MfrmMoreDelegate) super.delegate).onClickToAbout();
				}
				break;
			case R.id.rl_scan_qrcode:
				if (super.delegate instanceof MfrmMoreDelegate) {
					((MfrmMoreDelegate) super.delegate).onClickToQRCode();
				}
				break;
			case R.id.rl_exit_logon:
				if (super.delegate instanceof MfrmMoreDelegate) {
					((MfrmMoreDelegate) super.delegate).onClickLogoff();
				}
				break;
		default:
			break;
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
	  * @Description 更多
	*/
	public interface MfrmMoreDelegate {

		void onClickToUser();

		void onClickToQRCode(); //跳转到扫码界面

		void onClickToAbout(); //跳转到关于界面

		void onClickLogoff(); //跳转到关于界面

	}

}
