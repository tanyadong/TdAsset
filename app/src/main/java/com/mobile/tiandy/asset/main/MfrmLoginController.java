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
import com.mobile.tiandy.asset.common.common.AppMacro;
import com.mobile.tiandy.asset.common.common.InitApplication;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.User;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;


public class MfrmLoginController extends BaseController implements MfrmLoginView.MfrmLoginViewDelegate, OnResponseListener<String> {

    private MfrmLoginView mfrmLoginView;
    private Object cancelObject = new Object();
    private RequestQueue queue;
    private static final int LOGON_IN = 0;
    private User user;
    @Override
    protected void getBundleData() {

    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login_controller);
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
            StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
            StatusBarUtil.StatusBarLightMode(this);
        }
        mfrmLoginView = (MfrmLoginView) findViewById(R.id.activity_login_view);
        mfrmLoginView.setDelegate(this);
        queue = NoHttp.newRequestQueue();
        user = LoginUtils.getUserInfo(this);
        if (user == null) {
            return;
        }
        mfrmLoginView.initData(user);
    }

    /**
      * @author tanyadong
      * @Title onClickLogin
      * @Description 点击登录
      * @date 2017/9/6 22:16
    */
    @Override
    public void onClickLogin(String jobId, String password) {
        user = LoginUtils.getUserInfo(this);
        if (user == null) {
            user = new User();
        }
        user.setPassword(password);
        user.setJobId(jobId);
        LoginUtils.saveUserInfo(this, user);
        if (jobId == null || "".equals(jobId) || password == null || "".equals(password)) {
            L.e("username == null || password == null");
            return;
        }
        String uri = AppMacro.REQUEST_URL + "/user/login";
        Request<String> request = NoHttp.createStringRequest(uri);
        request.setCancelSign(cancelObject);
        request.add("jobid", jobId);
        request.add("password", password);
        queue.add(LOGON_IN, request, this);
    }
    /**
      * @author tanyadong
      * @Title onClickRegister
      * @Description 点击注册
      * @date 2017/9/6 22:15
    */
    @Override
    public void onClickRegister() {
        Intent intent = new Intent(this, MfrmRegisterController.class);
        startActivity(intent);
    }


    @Override
    public void onFailed(int i, Response response) {
        mfrmLoginView.circleProgressBarView.hideProgressBar();
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {
            T.showShort(this, R.string.network_error);
            return;
        }
        if (exception instanceof UnKnownHostError) {
            T.showShort(this, R.string.network_unknown_host_error);
            return;
        }
        if (exception instanceof SocketTimeoutException) {
            T.showShort(this, R.string.network_socket_timeout_error);
            return;
        }
        T.showShort(this, R.string.login_failed);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        queue.cancelBySign(cancelObject);
    }

    @Override
    public void onStart(int i) {
        if (mfrmLoginView.circleProgressBarView != null) {
            mfrmLoginView.circleProgressBarView.showProgressBar();
        }
    }

    @Override
    public void onSucceed(int i, Response<String> response) {
        if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
            String result = (String) response.get();
            if (result == null || "".equals(result)) {
                T.showShort(this, R.string.login_failed);
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("code") && jsonObject.getInt("code") == 0) {
                    JSONObject jsonUser = jsonObject.optJSONObject("content");
                    if (user == null) {
                        user = new User();
                    }
                    user.setJobId(jsonUser.optString("jobId"));
                    user.setName(jsonUser.optString("name"));
                    user.setPassword(jsonUser.optString("password"));
                    LoginUtils.saveUserInfo(this, user);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    T.showShort(this, R.string.login_failed);
                }
            } catch (JSONException e) {
                T.showShort(this, R.string.login_failed);
                e.printStackTrace();
            }
        } else {
            T.showShort(this, R.string.login_failed);
        }
    }


    @Override
    public void onFinish(int i) {
        if (mfrmLoginView.circleProgressBarView != null) {
            mfrmLoginView.circleProgressBarView.hideProgressBar();
        }
    }
}
