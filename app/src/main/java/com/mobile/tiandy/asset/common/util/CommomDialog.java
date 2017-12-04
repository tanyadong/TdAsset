package com.mobile.tiandy.asset.common.util;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mobile.tiandy.asset.R;


public class CommomDialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private EditText updateEdit;
    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private View lineView;
    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }
    public CommomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }
    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommomDialog(Context context, int themeResId, String title, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog setTitle(String title){
        this.title = title;
        titleTxt.setText(title);
        return this;
    }

    public void updateTitle(String title){
        this.title = title;
        titleTxt.setText(title);
    }

    /**
     * @author tanyadong
     * @Title setNegativeButtonShow
     * @Description 设置取消按钮显示与隐藏
     * @date 2017/9/21 9:20
     */
    public CommomDialog setNegativeButtonShow(boolean flag){
        if (flag) {
            cancelTxt.setVisibility(View.VISIBLE);
            submitTxt.setBackground(getContext().getResources().getDrawable(R.drawable.bg_common_dialog_right));
            lineView.setVisibility(View.VISIBLE);
        } else {
            cancelTxt.setVisibility(View.GONE);
            submitTxt.setBackground(getContext().getResources().getDrawable(R.drawable.bg_common_dialog_bottom));
            lineView.setVisibility(View.GONE);
        }
        return this;
    }

    public CommomDialog setListener(OnCloseListener listener){
        this.listener = listener;
        return this;
    }
    public CommomDialog setPositiveButton(String name){
        submitTxt.setText(name);
        return this;
    }

    public CommomDialog setNegativeButton(String name){
        cancelTxt.setText(name);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.dialog_ok);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        lineView = findViewById(R.id.bottom_min_line);
        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }
        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this);
                }
                break;
            case R.id.dialog_ok:
                this.dismiss();
                if(listener != null){
                    listener.onClickContinue(this);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog);
        void onClickContinue(Dialog dialog);
    }
}