package com.mobile.tiandy.asset.more;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;


public class QRCodeView extends View {

	private Context context;
	public QRCodeView(Context context) {
		super(context);
		this.context=context;

	}
	public QRCodeView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}
	@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
		//绘制透明色
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }


}
