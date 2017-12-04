package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.mobile.tiandy.asset.R;
import com.mobile.tiandy.asset.more.MfrmQRCode;
import com.zbar.lib.camera.CameraManager;

/**
 * @author tanyadong
 * @Description: 扫描消息转发
 * @date 2017/5/11  10:57
 */
public final class CaptureActivityHandler extends Handler {

	DecodeThread decodeThread = null;
	MfrmQRCode activity = null;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}
	public Handler getHandler() {
		return this;
	}
	public CaptureActivityHandler(MfrmQRCode activity) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.auto_focus:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
			break;
		case R.id.restart_preview:
			restartPreviewAndDecode();
			break;
		case R.id.decode_succeeded:
			state = State.SUCCESS;
			activity.handleDecode((String) message.obj);// 解析成功，回调
			break;
		case R.id.decode_failed:
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			break;
		}

	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
		removeMessages(R.id.decode);
		removeMessages(R.id.auto_focus);
	}

	public void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);

		}
	}

	public void restartDecode(byte[] data) {
		state = State.PREVIEW;
		CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
				R.id.decode);
		CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
		CameraManager.get().setPreviewFrame(data);
	}

}
