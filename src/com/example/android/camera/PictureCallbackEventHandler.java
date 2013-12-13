package com.example.android.camera;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

public class PictureCallbackEventHandler implements PictureCallback
{
	private static final String TAG = "PictureCallbackEventHandler";

	private Activity mActivity;

	String mType = "";

	File pictureFile = null;

	static Bitmap mutableBitmap;

	public PictureCallbackEventHandler(Context context, String type)
	{
		mActivity = (Activity) context;
		mType = type;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera)
	{
		String path = "";
		Bitmap bmp;
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 5;
			options.inPurgeable = true;
			options.inInputShareable = true;
			bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);

			Display display = mActivity.getWindowManager().getDefaultDisplay();
			int rotation = 0;
			switch (display.getRotation())
			{
			case Surface.ROTATION_0:
				rotation = 90;
				break;
			case Surface.ROTATION_90:
				rotation = 0;
				break;
			case Surface.ROTATION_180:
				rotation = 270;
				break;
			case Surface.ROTATION_270:
				rotation = 180;
				break;
			}

			Matrix mat = new Matrix();
			mat.postRotate(rotation);
			mutableBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), mat, true);

			path = CameraUtils.Save_image_to_SD(mutableBitmap, mType);

			Intent newIntent = new Intent();
			newIntent.putExtra("path", path);
			newIntent.setAction(SurfaceViewCamera.INTENT_FILTER_PICTURE_SAVE_EVENT);
			mActivity.sendBroadcast(newIntent);

		}
		catch (Exception error)
		{
			Log.e(TAG, "File at :" + path + "  not saved: " + error);
		}

	}

}
