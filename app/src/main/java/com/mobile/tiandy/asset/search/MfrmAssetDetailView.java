package com.mobile.tiandy.asset.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.Asset;
import com.mobile.tiandy.asset.mine.MfrmMineAssetView;


public class MfrmAssetDetailView extends BaseView {

    private TextView assetCodeTxt, assetNameTxt, assetRealPlaceTxt, assetRealPerTxt, assetStateTxt, assetPartTxt,
    titleTxt;
    private LinearLayout titleLeftLl, titleRightLl;
    private ImageView backImg;

    public MfrmAssetDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setInflate() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_asset_detail_view, this);
    }

    @Override
    public void initData(Object... data) {
        Asset asset = (Asset) data[0];
        if (asset == null) {
            return;
        }
        initValues(asset);
    }
    /**
      * @author tanyadong
      * @Title initValues
      * @Description 显示数据
      * @date 2017/9/14 17:25
    */
    private void initValues(Asset asset) {
        titleTxt.setText(getResources().getString(R.string.asset_detail));
        assetCodeTxt.setText(asset.getCodeId());
        assetNameTxt.setText(asset.getName());
        assetPartTxt.setText(asset.getPart());
        assetRealPlaceTxt.setText(asset.getRealPlace());
        assetRealPerTxt.setText(asset.getRealSaver());
        if (asset.getState() == 1) {
            assetStateTxt.setText(getResources().getString(R.string.authenticated));
            assetStateTxt.setTextColor(getResources().getColor(R.color.green_light));
        } else {
            assetStateTxt.setText(getResources().getString(R.string.unauthorized));
            assetStateTxt.setTextColor(getResources().getColor(R.color.read));
        }
    }


    @Override
    protected void initViews() {
        titleLeftLl = (LinearLayout) findViewById(R.id.ll_title_left);
        titleRightLl = (LinearLayout) findViewById(R.id.ll_title_right);
        titleTxt = (TextView) findViewById(R.id.txt_title);
        backImg = (ImageView) findViewById(R.id.img_back);
        backImg.setImageResource(R.drawable.goback);
        titleRightLl.setVisibility(INVISIBLE);
        assetCodeTxt = (TextView) findViewById(R.id.asset_code_txt);
        assetNameTxt = (TextView) findViewById(R.id.asset_name_txt);
        assetPartTxt = (TextView) findViewById(R.id.asset_partname_txt);
        assetRealPlaceTxt = (TextView) findViewById(R.id.asset_realPlace_txt);
        assetRealPerTxt = (TextView) findViewById(R.id.asset_realper_txt);
        assetStateTxt = (TextView) findViewById(R.id.asset_state_txt);
    }

    @Override
    protected void addListener() {
        titleLeftLl.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    @Override
    protected void onClickListener(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_title_left:
            case R.id.img_back:
                if (super.delegate instanceof MfrmAssetDetailDelegate) {
                    ((MfrmAssetDetailDelegate) super.delegate).onClickBack();
                }
                break;
        }
    }
    public interface MfrmAssetDetailDelegate {
        void  onClickBack();
    }
}
