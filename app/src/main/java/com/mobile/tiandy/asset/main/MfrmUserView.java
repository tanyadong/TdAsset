package com.mobile.tiandy.asset.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.vo.User;


public class MfrmUserView extends BaseView {

    private ImageView closeImg;
    private TextView titleTxt;
    private LinearLayout titleRightLl;
    private TextView jobIdTxt, nameTxt;
    public MfrmUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setInflate() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_user_view, this);
    }

    @Override
    public void initData(Object... data) {
        User user = (User) data[0];
        if (user == null) {
            return;
        }
        jobIdTxt.setText(user.getJobId());
        nameTxt.setText(user.getName());
    }


    @Override
    protected void initViews() {
        titleRightLl = (LinearLayout) view.findViewById(R.id.ll_title_right);
        closeImg = (ImageView) view.findViewById(R.id.img_right);
        closeImg.setImageResource(R.drawable.user_colse);
        titleTxt = (TextView) view.findViewById(R.id.txt_title);
        titleTxt.setText(getResources().getString(R.string.user));
        jobIdTxt = (TextView) findViewById(R.id.user_job_num);
        nameTxt = (TextView) findViewById(R.id.user_username);
    }

    @Override
    protected void addListener() {
        closeImg.setOnClickListener(this);
        titleRightLl.setOnClickListener(this);
    }

    @Override
    protected void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.ll_title_right:
            case R.id.img_right:
                if (super.delegate instanceof MfrmUserView.MfrmLogOffViewDelegate) {
                    ((MfrmUserView.MfrmLogOffViewDelegate) super.delegate).onClickClose();
                }
                break;
            default:
                break;
        }
    }



    /**
      * @date 创建时间 2017/9/6
      * @author tanyadong
      * @Description 登录代理页
    */
    public interface MfrmLogOffViewDelegate {
        //取消
        void onClickClose();
    }
}
