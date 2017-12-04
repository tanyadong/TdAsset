package com.mobile.tiandy.asset.more;

import android.os.Bundle;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.util.AppUtils;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;


/**
 * Created by chenziqiang on 17/5/15.
 */

public class MfrmAboutController extends BaseController implements MfrmAboutView.AboutViewDelegate {
    private MfrmAboutView aboutView;
    private String versionName;

    @Override
    protected void getBundleData() {

    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_controller);
        int result = StatusBarUtil.StatusBarLightMode(this);
        if (result != 0) {
            StatusBarUtil.initWindows(this, getResources().getColor(R.color.white));
        }
        aboutView = (MfrmAboutView) findViewById(R.id.mfrm_about_view);
        aboutView.setDelegate(this);

        versionName = AppUtils.getVersionCode(this);
        aboutView.initData(versionName);

    }

    @Override
    public void onClickBack() {
        finish();
    }


}
