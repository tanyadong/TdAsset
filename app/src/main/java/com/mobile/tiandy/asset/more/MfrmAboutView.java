package com.mobile.tiandy.asset.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;
import com.mobile.tiandy.asset.common.util.AlignTextView;


/**
 * Created by chenziqiang on 17/5/15.
 */

public class MfrmAboutView extends BaseView {
    private ImageView backL;
    private TextView appDescritionTv;
    private String versionName;
    private TextView versionTv, titleTxt;
    public MfrmAboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setInflate() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_about_view, this);
    }

    @Override
    public void initData(Object... data) {
        if (data == null) {
            return;
        }
        versionName = (String) data[0];
        if (versionName != null || !"".equals(versionName)) {
            //设置版本号
            initVersionText();
        }

    }

    private void initVersionText() {
        String[] temp = versionName.split("\\.");
        if (temp.length < 3) {
            versionTv.setText(this.getResources().getText(R.string.app_version_mark) + versionName + ".0");
        } else {
            versionTv.setText(this.getResources().getText(R.string.app_version_mark) + versionName);
        }

    }

    @Override
    protected void initViews() {
        backL = (ImageView) findViewById(R.id.img_back);
        titleTxt = (TextView) findViewById(R.id.txt_title);
        titleTxt.setText(getResources().getString(R.string.morte_about));
        backL.setImageResource(R.drawable.goback);
        versionTv = (TextView) findViewById(R.id.txt_app_version);
        appDescritionTv = (TextView) findViewById(R.id.txt_about_content);
        appDescritionTv.setText(getResources().getString(R.string.company_introduce));
    }

    @Override
    protected void addListener() {
        backL.setOnClickListener(this);
    }

    @Override
    protected void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (super.delegate instanceof AboutViewDelegate) {
                    ((AboutViewDelegate) super.delegate).onClickBack();
                }
                break;
            default:
                break;
        }

    }

    public interface AboutViewDelegate {

        void onClickBack();//点击返回

    }
}
