package com.mobile.tiandy.asset.common.base;


import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;



public class BasicFragmentActivity extends FragmentActivity {
    private int guideResourceId=0;//引导页图片资源id
    private int guideResourceId1=0;//引导页图片资源id
    private FrameLayout frameLayout;
    @Override
    protected void onStart() {
        super.onStart();
    }


    
    /**子类在onCreate中调用，设置引导图片的资源id
     *并在布局xml的根元素上设置android:id="@id/my_content_view"
     * @param resId
     */
    protected void setGuideResId(int resId, int resId1){
        this.guideResourceId = resId;
        this.guideResourceId1 = resId1;
    }
}