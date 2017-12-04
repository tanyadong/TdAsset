package com.mobile.tiandy.asset.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.vo.User;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

public class MfrmUserController extends BaseController implements MfrmUserView.MfrmLogOffViewDelegate {

    private MfrmUserView mfrmUserView;

    @Override
    protected void getBundleData() {
    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_controller);
        if (Build.VERSION.SDK_INT >= 19) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
            StatusBarUtil.StatusBarLightMode(this);
        }
        mfrmUserView = (MfrmUserView) findViewById(R.id.activity_user_view);
        mfrmUserView.setDelegate(this);
        User user = LoginUtils.getUserInfo(this);
        if (user == null) {
            return;
        }
        mfrmUserView.initData(user);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
      * @author tanyadong
      * @Title onClickClose
      * @Description  取消
      * @date 2017/9/7 19:49
    */
    @Override
    public void onClickClose() {
        finish();
        overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
    }
}
