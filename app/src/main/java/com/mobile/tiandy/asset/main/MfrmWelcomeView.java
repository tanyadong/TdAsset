package com.mobile.tiandy.asset.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.base.BaseView;


public class MfrmWelcomeView extends BaseView {

    public MfrmWelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setInflate() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_welcome_view, this);
    }

    @Override
    public void initData(Object... data) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void onClickListener(View v) {

    }
}
