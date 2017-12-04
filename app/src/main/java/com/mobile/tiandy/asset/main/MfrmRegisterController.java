package com.mobile.tiandy.asset.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseController;
import com.mobile.tiandy.asset.common.common.AppMacro;
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
import java.util.Timer;

public class MfrmRegisterController extends BaseController implements MfrmRegisterView.MfrmRegisterDelegate, OnResponseListener<String> {

    private MfrmRegisterView mfrmRegisterView;
    private Object cancelObject = new Object();
    private RequestQueue queue;
    @Override
    protected void getBundleData() {

    }

    @Override
    protected void onCreateFunc(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_controller);
        int result = StatusBarUtil.StatusBarLightMode(this);
        if (result != 0) {
            StatusBarUtil.initWindows(this, getResources().getColor(R.color.white));
        }
        mfrmRegisterView = (MfrmRegisterView) findViewById(R.id.activity_register_view);
        mfrmRegisterView.setDelegate(this);
        queue = NoHttp.newRequestQueue();
    }

    /**
      * @author tanyadong
      * @Title onClickRegister
      * @Description 点击注册
      * @date 2017/9/6 22:15
    */
    @Override
    public void onClickRegister(String jobID, String userName, String password) {
        User user = LoginUtils.getUserInfo(this);
        if (user == null) {
            user = new User();
        }
        user.setJobId(jobID);
        user.setName(userName);
        user.setPassword(password);
        LoginUtils.saveUserInfo(this, user);
        String uri = AppMacro.REQUEST_URL + "/user/register";
        Request<String> request = NoHttp.createStringRequest(uri);
        request.setCancelSign(cancelObject);
        request.add("jobid", jobID);
        request.add("name", userName);
        request.add("password",password);
        queue.add(0, request, this);
    }


    @Override
    public void onClickBack() {
        finish();
    }


    @Override
    public void onFailed(int i, Response response) {
        mfrmRegisterView.circleProgressBarView.hideProgressBar();

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
        T.showShort(this, R.string.register_failed);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        queue.cancelBySign(cancelObject);

    }

    @Override
    public void onStart(int i) {
        if (mfrmRegisterView.circleProgressBarView != null) {
            mfrmRegisterView.circleProgressBarView.showProgressBar();
        }
    }

    @Override
    public void onSucceed(int i, Response<String> response) {
        if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
            String result = (String) response.get();
            if (result == null || "".equals(result)) {
                T.showShort(this, R.string.register_failed);
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("code") && jsonObject.getInt("code") == 0) {
                    Intent intent = new Intent(this, MfrmLoginController.class);
                    startActivity(intent);
                    finish();
                } else {
                    T.showShort(this, R.string.register_failed);
                }
            } catch (JSONException e) {
                T.showShort(this, R.string.register_failed);
                e.printStackTrace();
            }
        } else {
            T.showShort(this, R.string.register_failed);
        }
    }

    @Override
    public void onFinish(int i) {
        if (mfrmRegisterView.circleProgressBarView != null) {
            mfrmRegisterView.circleProgressBarView.hideProgressBar();
        }
    }
}
