package com.mobile.tiandy.asset.more;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Hashtable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.common.common.AppMacro;
import com.mobile.tiandy.asset.common.util.CommomDialog;
import com.mobile.tiandy.asset.common.util.L;
import com.mobile.tiandy.asset.common.util.LoginUtils;
import com.mobile.tiandy.asset.common.util.StatusBarUtil;
import com.mobile.tiandy.asset.common.util.T;
import com.mobile.tiandy.asset.common.vo.User;
import com.mobile.tiandy.asset.main.MainActivity;
import com.mobile.tiandy.asset.main.MfrmLoginController;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 二维码扫描
 */
public class MfrmQRCode extends Activity implements Callback, OnResponseListener<String> {

	// 用于判断是哪个activity跳转到二维码扫描 --- 1 代表从实时预览跳转
	private static final int REQUEST_CODE = 0;
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private FrameLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	private TextView titleTxt;
	private ImageView lightImg;
	private boolean isOpen = false;
	private LinearLayout titleBackLl, titleSelectPicLl;
	private ImageView backImg, selectPicImg;
	private RequestQueue queue;
	private Object cancelObject = new Object();
	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welome_controller);
		// 禁止屏幕休眠
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_qrcode);
		int result = StatusBarUtil.StatusBarLightMode(this);
		if (result != 0) {
			StatusBarUtil.initWindows(this, getResources().getColor(R.color.white));
		}
		// 初始化CameraManager
		CameraManager.init(getApplication());
		queue = NoHttp.newRequestQueue();

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		titleBackLl = (LinearLayout) findViewById(R.id.ll_title_left);
		titleSelectPicLl = (LinearLayout) findViewById(R.id.ll_title_right);
		selectPicImg = (ImageView) findViewById(R.id.img_right);
		backImg = (ImageView) findViewById(R.id.img_back);
		backImg.setImageResource(R.drawable.goback_write);
		selectPicImg.setImageResource(R.drawable.qrcode_photo);
		titleTxt = (TextView) findViewById(R.id.txt_title);
		titleTxt.setText(R.string.device_device_qrcode);
		titleTxt.setTextColor(getResources().getColor(R.color.white));
		mContainer = (FrameLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		lightImg = (ImageView) findViewById(R.id.light_img);
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		//设置动画时间
		mAnimation.setDuration(2500);
		//设置重复次数
		mAnimation.setRepeatCount(-1);
		//动画执行模式
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
		addListener();
		getBundleData();
	}
	private void getBundleData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null || "".equals(bundle)) {
			return;
		}
	}
	boolean flag = true;


	protected void light() {
		if (flag == true) {
			flag = false;
			// 打开
			CameraManager.get().openLight();
			isOpen = true;
			setLightState(isOpen);
		} else {
			flag = true;
			// 关闭
			CameraManager.get().offLight();
			isOpen = false;
			setLightState(isOpen);
		}

	}






	private void addListener() {
		titleSelectPicLl.setOnClickListener(new OpenPicture());
		selectPicImg.setOnClickListener(new OpenPicture());
		backImg.setOnClickListener(new ClickBack());
		titleBackLl.setOnClickListener(new ClickBack());
		lightImg.setOnClickListener(new ClickLightSwitch());
	}

	@Override
	public void onStart(int i) {

	}

	@Override
	public void onSucceed(int i, Response<String> response) {
		if (response.responseCode() == AppMacro.RESPONCESUCCESS) {
			String result = (String) response.get();
			if (result == null || "".equals(result)) {
				T.showShort(this, R.string.asset_check_failed);
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.has("code") && jsonObject.getInt("code") == 0) {
					CommomDialog commomDialog = new CommomDialog(this, R.style.dialog);
					commomDialog.show();
					commomDialog.setNegativeButtonShow(true);
					commomDialog.setTitle(getResources().getString(R.string.asset_check_success));
					commomDialog.setListener(new CommomDialog.OnCloseListener() {
						@Override
						public void onClick(Dialog dialog) {
							dialog.dismiss();
							Message message = new Message();
							message.what = R.id.restart_preview;
							if (handler == null) {
								handler = new CaptureActivityHandler(MfrmQRCode.this);
								handler.getHandler().sendMessageDelayed(message, 1000);
							} else {
								handler.getHandler().sendMessageDelayed(message, 1000);
							}
						}
						//继续
						@Override
						public void onClickContinue(Dialog dialog) {
							dialog.dismiss();
							Intent intent = new Intent(MfrmQRCode.this, MainActivity.class);
							startActivity(intent);
						}
					});
				} else if (jsonObject.getInt("code") == -1) {
					if (jsonObject.has("message")) {
						T.showShort(this, jsonObject.getString("message"));
					}
				} else {
					T.showShort(this, R.string.asset_check_failed);
				}
			} catch (JSONException e) {
				T.showShort(this, R.string.asset_check_failed);
				e.printStackTrace();
			}
		} else {
			T.showShort(this, R.string.asset_check_failed);
		}
	}

	@Override
	public void onFailed(int i, Response<String> response) {
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
		T.showShort(this, R.string.asset_check_failed);
	}

	@Override
	public void onFinish(int i) {


	}

	// 闪光灯开关状态
	class ClickLightSwitch implements OnClickListener {
		@Override
		public void onClick(View v) {
			light();
		}
	}

	// 二维码返回事件
	class ClickBack implements OnClickListener {
		@Override
		public void onClick(View v) {
			releaseResource();
			finish();
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		//先释放资源
		releaseResource();
		surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		setLightState(isOpen);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseResource();
	}


	/**
	  * @author tanyadong
	  * @Title releaseResource
	  * @Description 释放相关资源
	  * @date 2017/7/15 10:24
	*/
	private void releaseResource() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().offLight();
		CameraManager.get().closeDriver();
		flag = true;
		isOpen = false;
	}
	// 进入相册
	class OpenPicture implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent();
			if (Build.VERSION.SDK_INT < 19) {
				intent.setAction(Intent.ACTION_GET_CONTENT);
			} else {
				intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			}
			intent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(intent, getResources()
					.getString(R.string.device_device_qrcode_image_select));
			startActivityForResult(wrapperIntent, REQUEST_CODE);
		}
	}

	private void setLightState(boolean isOpen){
		if(isOpen){
			lightImg.setImageResource(R.drawable.qrcode_light_open);
		} else {
			lightImg.setImageResource(R.drawable.qrcode_light);
		}
	}


	 /**
	 * @author tanyadong
	 * @Description:接收扫描相册中返回的数据
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			//zxing解析图片二维码
			String photo_path = null;
			String[] proj = {MediaStore.Images.Media.DATA};
			// 获取选中图片的路径
			Cursor cursor = getContentResolver().query(data.getData(),
					proj, null, null, null);

			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				photo_path = cursor.getString(column_index);
				if (photo_path == null) {
					photo_path = QRCodeScanUtils.getPath(
							getApplicationContext(), data.getData());
				}
			}
			cursor.close();
			Result result = scanningImage(photo_path);
			if (result == null) {
				T.showShort(this,getResources().getString(R.string.device_device_qrcode_image_format_error));
			} else {
				String strResult = recode(result.toString());
				qrcodeCheckAsset(strResult);
			}
		}
	}
	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		CameraManager.get().closeDriver();
		queue.cancelBySign(cancelObject);
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		qrcodeCheckAsset(result);
	}

	private void qrcodeCheckAsset(String result) {
		try {
			final String jobid = result.substring(0,result.indexOf(":"));
			final String codeid = result.substring(result.indexOf(":") + 1 ,result.length());
			User user = LoginUtils.getUserInfo(this);
			if (user != null) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("jobid", jobid);
				bundle.putString("codeid", codeid);
				intent.putExtras(bundle);
				intent.setClass(this, MfrmAssetCheckDetailController.class);
				startActivity(intent);
				finish();
			}
		} catch (Exception e) {
			T.showShort(this,getResources().getString(R.string.device_device_qrcode_image_format_error));
			Message message = new Message();
			message.what = R.id.restart_preview;
			if (handler == null) {
				handler = new CaptureActivityHandler(MfrmQRCode.this);
				handler.getHandler().sendMessageDelayed(message, 1000);
			} else {
				handler.getHandler().sendMessageDelayed(message, 1000);
			}
		}
	}


	/**
	 * @author tanyadong
	 * @Title scanningImage
	 * @Description 扫码二维码
	 * @date 2017/9/19 20:43
	 */
	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();

		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @author tanyadong
	 * @Title recode
	 * @Description 避免乱码
	 * @date 2017/9/19 20:42
	 */
	private String recode(String str) {
		String formart = "";
		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
			} else {
				formart = str;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return formart;
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;
			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			setNeedCapture(true);

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(MfrmQRCode.this);
			handler.restartPreviewAndDecode();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}


}