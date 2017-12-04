package com.mobile.tiandy.asset.more;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.ScreenUtil;
import com.mobile.tiandy.asset.common.util.T;

import java.util.ArrayList;
import java.util.List;


/**
  * @date 创建时间 2017/9/5
  * @author tanyadong
  * @Description 更多
*/
public class MfrmAssetCheckDetailView extends BaseView implements AssetPlaceGridViewAdapter.AssetPlaceAdapterDelegate{
	private ImageView backImg;
	private TextView titleTxt, authenticationTxt, assetIdTxt, assetJobidTxt, assetPlaceCancelTxt;
	private LinearLayout titleLiftLl, titleRightLl, assetDetailContentLl;
	private EditText inputPersonEdit;
	private TextView selectPlaceTxt;
	private PopupWindow popupWindow;
	private RelativeLayout showAssetPlaceLl;
	private GridView gridView;
	private RelativeLayout assetRealPlaceRl;
	private List<String> list;
	private AssetPlaceGridViewAdapter assetPlaceGridViewAdapter;
	private String jobid;
	private String codeid;
	public MfrmAssetCheckDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setInflate() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.activity_asset_check_detail_view, this);
	}

	@Override
	protected void initViews() {
		titleLiftLl = (LinearLayout) findViewById(R.id.ll_title_left);
		titleRightLl = (LinearLayout) findViewById(R.id.ll_title_right);
		titleRightLl.setVisibility(INVISIBLE);
		authenticationTxt = (TextView) findViewById(R.id.txt_authentication);
		backImg = (ImageView) findViewById(R.id.img_back);
		backImg.setImageResource(R.drawable.goback);
		titleTxt = (TextView) findViewById(R.id.txt_title);
		titleTxt.setText(getResources().getString(R.string.asset_info));
		inputPersonEdit = (EditText) findViewById(R.id.edit_asset_person);
		selectPlaceTxt = (TextView) findViewById(R.id.edit_asset_realplace);
		assetIdTxt = (TextView) findViewById(R.id.txt_asset_id);
		assetJobidTxt = (TextView) findViewById(R.id.txt_asset_jobid);
		assetRealPlaceRl = (RelativeLayout) findViewById(R.id.rl_asset_realplace);
		assetDetailContentLl = (LinearLayout) findViewById(R.id.ll_asset_detail_content);
		initPopupWindow();
		inputPersonEdit.setFilters(new InputFilter[]{new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!isChinese(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		}, new InputFilter.LengthFilter(6)});
	}

	/**
	 * @author tanyadong
	 * @Title: initPopupWindow
	 * @Description: 初始化popupwindow
	 * @date 2017/12/2 0002 10:47
	 */

	private void initPopupWindow() {
		showAssetPlaceLl = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.dlg_show_asset_place_popup_view, null);
		gridView = (GridView) showAssetPlaceLl.findViewById(R.id.asset_place_gridview);
		assetPlaceCancelTxt = (TextView) showAssetPlaceLl.findViewById(R.id.txt_asset_place_cancel);
		popupWindow = new PopupWindow(showAssetPlaceLl);
		// 设置弹出窗体可点击
		popupWindow.setAnimationStyle(R.style.take_photo_anim);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		//设置PopupWindow弹出窗体的高
		popupWindow.setHeight(ScreenUtil.getScreenHeight(context) / 2);
		list = new ArrayList<>();
		list = LoginUtils.getAssetPlace(context);
		assetPlaceGridViewAdapter = new AssetPlaceGridViewAdapter(context, list);
		gridView.setAdapter(assetPlaceGridViewAdapter);
		assetPlaceGridViewAdapter.setDelegate(this);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				if (delegate instanceof MfrmAssetCheckDetailDelegate) {
					((MfrmAssetCheckDetailDelegate)delegate).onClickSetAttributes((float) 1);
				}
			}
		});
	}

	/**
	 * @author tanyadong
	 * @Title: showPopupWindow
	 * @Description: 显示窗体
	 * @date 2017/12/2 0002 11:45
	 */
	
	public void  showPopupWindow() {
		// 设置弹出窗体显示时的动画，从底部向上弹出
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(this, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		if (super.delegate instanceof MfrmAssetCheckDetailDelegate) {
			((MfrmAssetCheckDetailDelegate) super.delegate).onClickSetAttributes((float) 0.5);
		}
	}

	@Override
	protected void addListener() {
		backImg.setOnClickListener(this);
		titleLiftLl.setOnClickListener(this);
		authenticationTxt.setOnClickListener(this);
		assetRealPlaceRl.setOnClickListener(this);
		assetPlaceCancelTxt.setOnClickListener(this);
	}

	@Override
	protected void onClickListener(View v) {
		switch (v.getId()) {
			case R.id.ll_title_left:
			case R.id.img_back:
				if (super.delegate instanceof MfrmAssetCheckDetailDelegate) {
					((MfrmAssetCheckDetailDelegate) super.delegate).onClickBack();
				}
				break;
			case R.id.txt_authentication:
				String assetPerson = inputPersonEdit.getText().toString().trim();
				String realPlace = selectPlaceTxt.getText().toString();
				if (assetPerson.equals("") || assetPerson.isEmpty()) {
					T.showShort(context, "请输入认证人员姓名");
					return;
				}
				if (realPlace.equals(getResources().getString(R.string.asset_search_place))) {
					T.showShort(context, getResources().getString(R.string.asset_search_place));
					return;
				}
				if (super.delegate instanceof MfrmAssetCheckDetailDelegate) {
					((MfrmAssetCheckDetailDelegate) super.delegate).onClickAuthentication(jobid, codeid, realPlace, assetPerson);
				}
				break;
			case R.id.rl_asset_realplace:
				showPopupWindow();
				break;
			case R.id.txt_asset_place_cancel:
				popupWindow.dismiss();
		default:
			break;
		}
	}
//	public void onClickSetAttributes(float bgAlpha) {
//		WindowManager.LayoutParams lp= thisgetWindow().getAttributes();
//		lp.alpha = bgAlpha;
//		if (bgAlpha == 1) {
//			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
//		} else {
//			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
//		}
//		this.getWindow().setAttributes(lp);
//	}
	/**
	 * @author tanyadong
	 * @Title: isChinese
	 * @Description: 判断只输入汉字
	 * @date 2017/12/1 0001 21:56
	 */

	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	/**
	 * @author tanyadong
	 * @Title: initData
	 * @Description: 初始化工号和资产编号
	 * @date 2017/12/2 0002 10:29
	 */
	
	@Override
	public void initData(Object... data) {
		if (data == null) {
			return;
		}
		jobid = (String) data[0];
		codeid = (String) data[1];
		assetIdTxt.setText(codeid);
		assetJobidTxt.setText(jobid);
	}



	@Override
	public void onClickItem(String place) {
		selectPlaceTxt.setText(place);
		if (super.delegate instanceof MfrmAssetCheckDetailDelegate) {
			((MfrmAssetCheckDetailDelegate) super.delegate).onClickSetAttributes((float) 1.0);
		}
		popupWindow.dismiss();

	}

	/**
	  * @date 创建时间 2017/9/5
	  * @author tanyadong
	  * @Description 更多
	*/
	public interface MfrmAssetCheckDetailDelegate {

		void onClickBack();

		void onClickAuthentication(String jobId, String codeId, String realPlace, String realSaver); //立即认证

		void onClickSetAttributes(float bgAlpha);//设置activity背景透明度
	}

}
