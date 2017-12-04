package com.mobile.tiandy.asset.main;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.common.CircleProgressBarView;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.T;


public class MfrmRegisterView extends BaseView {

    private TextView registerBtn;
    private LinearLayout registerLl;
    private EditText passwordEditTxt, userNameEdit, jonNumEdit;
    private ImageView title_backImg;
    private TextView titleTxt;
    private ImageView titltRightImg;
    private LinearLayout showPassWordLl;
    private ImageButton showPassWordImg;
    private boolean isShowPassword;
    private LinearLayout titleLiftLl;
    public CircleProgressBarView circleProgressBarView;

    public MfrmRegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setInflate() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_register_view, this);
    }

    @Override
    public void initData(Object... data) {

    }


    @Override
    protected void initViews() {
        circleProgressBarView = (CircleProgressBarView) findViewById(R.id.circleProgressBarView);
        registerLl = (LinearLayout) findViewById(R.id.ll_register);
        passwordEditTxt = (EditText) findViewById(R.id.edit_password);
        userNameEdit = (EditText) findViewById(R.id.edit_user_name);
        jonNumEdit = (EditText) findViewById(R.id.edit_job_num);
        titleLiftLl = (LinearLayout) findViewById(R.id.ll_title_left);
        title_backImg = (ImageView) findViewById(R.id.img_back);
        title_backImg.setImageResource(R.drawable.goback);
        titleTxt = (TextView) findViewById(R.id.txt_title);
        titleTxt.setText(R.string.register);
        titltRightImg = (ImageView) findViewById(R.id.img_right);
        titltRightImg.setVisibility(INVISIBLE);
        showPassWordImg = (ImageButton) findViewById(R.id.password_img_btn);
    }

    @Override
    protected void addListener() {
        registerLl.setOnClickListener(this);
        title_backImg.setOnClickListener(this);
        titleLiftLl.setOnClickListener(this);
        showPassWordImg.setOnClickListener(this);
    }

    @Override
    protected void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.ll_register:
                String jobId = jonNumEdit.getText().toString().trim();
                String username = userNameEdit.getText().toString().trim();
                String password = passwordEditTxt.getText().toString().trim();
                if (!checkInfo(username, password, jobId)) {
                    return;
                }
                if (super.delegate instanceof MfrmRegisterDelegate) {
                    ((MfrmRegisterDelegate) super.delegate).onClickRegister(jobId, username, password);
                }
                break;
            case R.id.ll_title_left:
            case R.id.img_back:
                if (super.delegate instanceof MfrmRegisterDelegate) {
                    ((MfrmRegisterDelegate) super.delegate).onClickBack();
                }
                break;
            case R.id.password_img_btn:
                if (isShowPassword) {
                    passwordEditTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPassword = false;
                    showPassWordImg.setBackgroundResource(R.drawable.device_add_password_close);
                } else {
                    passwordEditTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassword = true;
                    showPassWordImg.setBackgroundResource(R.drawable.device_add_password_open);
                }
                break;
            default:
                break;
        }
    }

    /**
     * @author tanyadong
     * @Description: 检验输入字段
     * @date 2017.0.23
     */
    private boolean checkInfo(String username, String password, String jobId) {
        if (null == jobId || "".equals(jobId)) {
            T.showShort(context, R.string.job_num_is_empty);
            return false;
        }
        if (null == username || "".equals(username)) {
            T.showShort(context, R.string.name_is_empty);
            return false;
        }
        if (null == password || "".equals(password)) {
            T.showShort(context, R.string.password_is_empty);
            return false;
        }
        return true;
    }

   /**
     * @date 创建时间 2017/9/6
     * @author tanyadong
     * @Description
   */
    public interface MfrmRegisterDelegate {
        //点击注册
        void onClickRegister(String jobID, String userName, String passwor);
        //点击返回
        void onClickBack();
    }
}
