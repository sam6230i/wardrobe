package com.example.android.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class CameraUtils
{
	private static final String TAG = "CameraUtils";

	public static File getPhotoFile(String type)
	{
		String directory_path = Environment.getExternalStorageDirectory().toString() + "/Wordrobe/";
		File f = new File(directory_path);

		if (!f.exists() && !f.mkdirs())
		{
			Log.d(TAG, "Directory can not be created to save image.");
			return null;
		}
		
		File photoFile = null;
		if (!TextUtils.isEmpty(type))
		{
			photoFile = new File(f, "wordrobe_" + type + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
		}
		else
		{
			photoFile = new File(f, "wordrobe_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
		}
		
		return photoFile;
	}
	
	public static String Save_image_to_SD(Bitmap bm, String type)
	{
		String path = "";
		String directory_path = Environment.getExternalStorageDirectory().toString() + "/Wordrobe";
		File file;
		if (!TextUtils.isEmpty(type))
		{
			path = directory_path + "/" + "wordrobe_" + type + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		}
		else
		{
			path = directory_path + "/" + "wordrobe_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		}
		
		file = new File(path);
		OutputStream outStream = null;

		try
		{
			outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();

			Log.d(TAG, "OK, Image Saved to SD at : " + path);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Log.e(TAG, "FileNotFoundException: " + e.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.e(TAG, "IOException: " + e.toString());
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
}
