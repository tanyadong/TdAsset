package com.mobile.tiandy.asset.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MfrmWelcomController extends BaseController {

    private Timer timer;
    private MyTimerTask task;
    private MyHandler myHandler;
    private int WHAT = 1;
    private User user;

    @Override
    protected void getBundleData() {
    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welome_controller);
        //延时1.5秒跳转
        myHandler = new MyHandler();
        timer = new Timer();
        task = new MyTimerTask();
        timer.schedule(task, 1500);
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            myHandler.sendEmptyMessage(WHAT);
        }
    }

    /**
     * @author zengCheng
     * @Description: 延迟1.5秒跳转
     * @date 2017.03.22
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                gotoLoginView();
            }
        }
    }

//    /**
//     * @author zengCheng
//     * @Description: 获取配置文件中user信息
//     * @date   2017.0.23
//     */
//    private void getUserInfo() {
//        user = LoginUtils.getUserInfo(this);
//        if (user == null) {
//            gotoLoginView();
//            return;
//        }
//        if (user.isAutoLogin()) {
//            gotoAutoLogin(user);
//        } else {
//            gotoLoginView();
//        }
//    }





    /**
     * @author zengCheng
     * @Description: 跳转登录页面
     * @date   2017.0.23
     */
    private void gotoLoginView() {
        Intent intent = new Intent();
        intent.setClass(this, MfrmLoginController.class);
        startActivity(intent);
        finish();
    }

    /**
     * @author zengCheng
     * @Description: 跳转主界面
     * @date 2017.03.22
     */
    public void gotoMainView() {
        Intent intent = new Intent();
        intent.setClass(this, MfrmLoginController.class);
        startActivity(intent);
        finish();
    }

    /**
     * @author zengCheng
     * @Description: 检验登录的返回消息
     * @date 2017.03.22
     */
    private void checkLoginResult(String result) {
        if (null == result || "".equals(result)) {
            L.e("result == null");
            gotoLoginView();
            return;
        }

        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("ret") && null != jsonResult.getString("ret") && !"".equals(jsonResult.getString("ret"))) {
                int ret = Integer.parseInt(jsonResult.getString("ret"));
                if (ret == 0) {

                } else {
                    gotoLoginView();
                }
            }
        } catch (JSONException e) {
            gotoLoginView();
            e.printStackTrace();
        }
    }



    /**
     * @author zengCheng
     * @Description: 保存登录信息
     * @date 2017.03.22
     */
    private void saveUserInfo(){
        LoginUtils.saveUserInfo(this, user);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
