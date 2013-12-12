package com.example.android.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.example.android.wardrobe.R;
import com.example.android.wardrobe.R.layout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class CustomCamera extends Activity
{

	String TAG = "CustomCamera";

	Bitmap mCapturedPhoto = null;

	Activity mContext;

	String type = "";

	public Uri imageCaptureUri = null;

	public interface CameraIntents
	{
		public static final int PICK_FROM_CAMERA = 1;

		public static final int CROP_FROM_CAMERA = 2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		mContext = this;

		Intent i = getIntent();
		if (i.hasExtra("type"))
		{
			type = i.getStringExtra("type");
		}

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		String directory_path = Environment.getExternalStorageDirectory().toString() + "/Wordrobe/";
		File f = new File(directory_path);
		f.mkdirs();

		if (!TextUtils.isEmpty(type))
		{
			imageCaptureUri = Uri.fromFile(new File(f, "wordrobe_" + type + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		}
		else
		{
			imageCaptureUri = Uri.fromFile(new File(f, "wordrobe_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		}

		imageCaptureUri = Uri.fromFile(new File(f, "wordrobe_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
		Log.d(TAG, "captureAndCropImage CaptureURI : " + imageCaptureUri.toString());
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 96);
		intent.putExtra("outputY", 96);
		intent.putExtra("return-data", true);
		mContext.startActivityForResult(intent, CameraIntents.PICK_FROM_CAMERA);

	}

	public void doCrop()
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(intent, 0);

		int size = list.size();
		if (size == 0)
		{
			return;
		}
		else
		{
			intent.setData(imageCaptureUri);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			Intent i = new Intent(intent);
			ResolveInfo res = list.get(0);
			i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			mContext.startActivityForResult(i, CameraIntents.CROP_FROM_CAMERA);
		}
	}

	public String Save_image_to_SD(Bitmap bm)
	{
		String path = "";
		String meteoDirectory_path = Environment.getExternalStorageDirectory().toString() + "/Wordrobe";
		File file;
		if (!TextUtils.isEmpty(type))
		{
			path = meteoDirectory_path + "/" + "wordrobe_" + type + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		}
		else
		{
			path = meteoDirectory_path + "/" + "wordrobe_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		}
		file = new File(path);
		OutputStream outStream = null;

		try
		{
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();

			Log.d(TAG, "OK, Image Saved to SD");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Log.d(TAG, "FileNotFoundException: " + e.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.d(TAG, "IOException: " + e.toString());
		}
		finally
		{
			if (outStream != null)
			{
				try
				{
					outStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return path;
	}

	public void DeleteImage()
	{
		try
		{
			File f = new File(imageCaptureUri.getPath());
			if (f.exists())
			{
				f.delete();
			}
		}
		catch (Exception e)
		{
			Log.d(TAG, "Error: " + e);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (resultCode != RESULT_OK)
		{
			return;
		}
		else if (resultCode != RESULT_CANCELED)
		{
			finish();
		}

		switch (requestCode)
		{
		case CameraIntents.PICK_FROM_CAMERA:
			doCrop();
			break;

		case CameraIntents.CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();
			if (extras != null)
			{
				Bitmap filePic = extras.getParcelable("data");
				if (filePic != null)
				{
					String path = Save_image_to_SD(filePic);
					data.putExtra("uri", imageCaptureUri.toString());
					data.putExtra("path", path);
					this.setResult(resultCode, data);
					this.finish();
				}
			}

			// DeleteImage();

			break;
		}
	}

}
