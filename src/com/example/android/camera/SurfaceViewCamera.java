package com.example.android.camera;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.example.android.wardrobe.R;

public class SurfaceViewCamera extends Activity implements
		SurfaceHolder.Callback {
	private static final String TAG = "SurfaceViewCamera";

	public static String INTENT_FILTER_PICTURE_SAVE_EVENT = "surfaceview.photo.save.events";

	private Activity mActivity;

	private Button mBtnTakePic;

	private SurfaceView mSurfaceView = null;

	private SurfaceHolder mSurfaceHolder = null;

	private Camera mCamera = null;

	private PictureCallbackEventHandler mPictureCallbackEventHandler = null;

	private boolean inPreView = false;

	private String mType = "";

	private BroadcastReceiver mReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_camera);

		mActivity = this;

		mReceiver = new PhotoReceiver();
		registerReceiver(mReceiver, new IntentFilter(
				INTENT_FILTER_PICTURE_SAVE_EVENT));

		Intent i = getIntent();
		if (i.hasExtra("type")) {
			mType = i.getStringExtra("type");
		}
		mPictureCallbackEventHandler = new PictureCallbackEventHandler(
				mActivity, mType);

		mSurfaceView = (SurfaceView) findViewById(R.id.screen_camera_surfaceView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);

		mBtnTakePic = (Button) findViewById(R.id.screen_camera_button);
		mBtnTakePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				inPreView = false;
				mCamera.takePicture(null, null, mPictureCallbackEventHandler);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		mCamera = Camera.open();
		mCamera.startPreview();
	}

	@Override
	public void onPause() {
		if (inPreView) {
			mCamera.stopPreview();
		}

		mCamera.release();
		mCamera = null;
		inPreView = false;

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}

		super.onPause();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mCamera != null) {
			mCamera.stopPreview();

			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = (size.y) / 2;

			initPreview(width, height);

			mCamera.startPreview();

			inPreView = true;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(mSurfaceHolder);
			} else {
				Log.d(TAG, "camera null ");
			}
		} catch (Exception e) {
			Log.e(TAG, "Exception in setPreviewDisplay() of surfaceCreated : ",
					e);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	private void initPreview(int width, int height) {
		if (mCamera != null && mSurfaceHolder.getSurface() != null) {
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
			} catch (Throwable t) {
				Log.e(TAG, "Exception in setPreviewDisplay()", t);
			}

			try {
				Camera.Parameters parameters = mCamera.getParameters();

				Camera.Size size = getBestPreviewSize(width, height, parameters);

				Display display = this.getWindowManager().getDefaultDisplay();
				switch (display.getRotation()) {
				case Surface.ROTATION_0: // This is display orientation
					if (size.height > size.width)
						parameters.setPreviewSize(size.height, size.width);
					else
						parameters.setPreviewSize(size.width, size.height);
					mCamera.setDisplayOrientation(90);
					break;
				case Surface.ROTATION_90:
					if (size.height > size.width)
						parameters.setPreviewSize(size.height, size.width);
					else
						parameters.setPreviewSize(size.width, size.height);
					mCamera.setDisplayOrientation(0);
					break;
				case Surface.ROTATION_180:
					if (size.height > size.width)
						parameters.setPreviewSize(size.height, size.width);
					else
						parameters.setPreviewSize(size.width, size.height);
					mCamera.setDisplayOrientation(270);
					break;
				case Surface.ROTATION_270:
					if (size.height > size.width)
						parameters.setPreviewSize(size.height, size.width);
					else
						parameters.setPreviewSize(size.width, size.height);
					mCamera.setDisplayOrientation(180);
					break;
				}

				mCamera.setParameters(parameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (size.width > size.height) {
					size.height = size.width;
				} else {
					size.width = size.height;
				}

				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		LayoutParams lp = mSurfaceView.getLayoutParams();
		lp.height = result.height;
		lp.width = result.width;

		mSurfaceView.setLayoutParams(lp);

		return (result);
	}

	public class PhotoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Picture saved callback received.");
			String path = intent.getStringExtra("path");
			Intent data = new Intent();
			data.putExtra("path", path);
			mActivity.setResult(RESULT_OK, data);
			mActivity.finish();
		}

	}

}
