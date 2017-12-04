package com.mobile.tiandy.asset.search;

import android.content.Intent;
import android.os.Bundle;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.vo.Asset;
import com.mobile.tiandy.asset.main.MainActivity;
import com.mobile.tiandy.asset.main.MfrmRegisterController;


public class MfrmAssetDetailController extends BaseController implements MfrmAssetDetailView.MfrmAssetDetailDelegate {

    private MfrmAssetDetailView mfrmAssetDetailView;
    private Asset asset;
    @Override
    protected void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        asset = (Asset) bundle.getSerializable("asset");
    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        int result = StatusBarUtil.StatusBarLightMode(this);
        if (result != 0) {
            StatusBarUtil.initWindows(this, getResources().getColor(R.color.white));
        }
        setContentView(R.layout.activity_asset_detail_controller);
        mfrmAssetDetailView = (MfrmAssetDetailView) findViewById(R.id.activity_asset_detail_view);
        mfrmAssetDetailView.setDelegate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mfrmAssetDetailView.initData(asset);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickBack() {
        finish();
    }
}
