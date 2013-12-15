package com.example.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader {

	private static final String TAG = "ImageLoader";

	MemoryCache memoryCache=new MemoryCache();
	static FileCache fileCache;
	private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	Context mContext;

	public ImageLoader(Context context) {
		mContext = context;
		fileCache=new FileCache(context);
		executorService=Executors.newFixedThreadPool(5);
	}

	public void DisplayImage(String url, final ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap=memoryCache.get(url);
		if(bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView, null);
		}
	}
	
	public void DisplayImage(String url, ImageView imageView, ProgressBar progressBar) {
		imageViews.put(imageView, url);
		Bitmap bitmap=memoryCache.get(url);
		if(bitmap != null) {
			progressBar.setVisibility(View.GONE);
			imageView.setImageBitmap(bitmap);
		} else {
			progressBar.setVisibility(View.VISIBLE);
			queuePhoto(url, imageView, progressBar);
		}
	}

	private void queuePhoto(String url, ImageView imageView, ProgressBar progressBar) {
		PhotoToLoad p=new PhotoToLoad(url, imageView, progressBar);
		executorService.submit(new PhotosLoader(p));
	}

	private static Bitmap getBitmap(String url)
	{
		if (TextUtils.isEmpty(url))
		{
			return null;
		}
		else
		{
			// from SD cache
			File f = fileCache.getFile(url);
			Bitmap b = decodeFile(f);
			if (b != null)
			{
				return b;
			}
			else {
				// from web
				InputStream is = null;
				OutputStream os = null;

				try
				{
					URL imageUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
					conn.setConnectTimeout(30000);
					conn.setReadTimeout(30000);
					conn.setInstanceFollowRedirects(true);

					is = conn.getInputStream();
					os = new FileOutputStream(f);

					if ((is != null) && (os != null))
					{
						copyStream(is, os);
					}
					Bitmap bitmap = decodeFile(f);
					return bitmap;
				}
				catch (Exception ex)
				{
					Log.e(TAG, TAG + " getBitmap:: for url  (" + url + ")", ex);
					return null;
				}
				finally
				{
					try
					{
						if (is != null)
						{
							is.close();
						}
						if (os != null)
						{
							os.close();
						}
					}
					catch (IOException e)
					{
						Log.w(TAG, " getBitmap:: closing inputstream-ouputstream ", e);
					}

				}
			}
		}
	}

	//decodes image and scales it to reduce memory consumption
	private static Bitmap decodeFile(File f) {
		try {
			//decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 160;
			int width_tmp=o.outWidth, height_tmp=o.outHeight;
			int scale=1;

			while(true) {
				if(((width_tmp/2)<REQUIRED_SIZE) || ((height_tmp/2)<REQUIRED_SIZE))
				{
					break;
				}
				width_tmp/=2;
				height_tmp/=2;
				scale*=2;
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}
		return null;
	}
	
	public static void copyStream(InputStream is, OutputStream os)
	{
		final int buffer_size = 1024;
		try
		{
			byte[] bytes = new byte[buffer_size];
			for (;;)
			{
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
				{
					break;
				}
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex)
		{
		}
	}

	//Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ProgressBar progressBar;
		public PhotoToLoad(String u, ImageView i, ProgressBar p) {
			url=u;
			imageView=i;
			progressBar = p;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad=photoToLoad;
		}

		@Override
		public void run() {
			if(imageViewReused(photoToLoad))
			{
				return;
			}
			Bitmap bmp=getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);

			if(imageViewReused(photoToLoad)) {
				return;
			}
			BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
			Activity a=(Activity)photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag=imageViews.get(photoToLoad.imageView);
		if((tag==null) || !tag.equals(photoToLoad.url)) {
			return true;
		}

		return false;
	}

	//Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p){
			bitmap=b;
			photoToLoad=p;
		}

		@Override
		public void run() {
			if(imageViewReused(photoToLoad)) {
				return;
			}

			if(bitmap!=null) {
				if(photoToLoad.progressBar != null)
				{
					photoToLoad.progressBar.setVisibility(View.GONE);
				}
				photoToLoad.imageView.setImageBitmap(bitmap);
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public class FileCache {
		private File cacheDir;
		public FileCache(Context context) {
			//Find the dir to save cached images
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				cacheDir=new File(android.os.Environment.getExternalStorageDirectory().toString()+"/Wordrobe/", "LazyList");
			} else {
				cacheDir=context.getCacheDir();
			}

			if(!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
		}
		
		public File getFile(String url) {
			//I identify images by hashcode. Not a perfect solution, good for the demo.
			String filename=String.valueOf(url.hashCode());
			//String filename = URLEncoder.encode(url);
			File f = new File(cacheDir, filename);
			return f;
		}
		public void clear() {
			File[] files=cacheDir.listFiles();
			for(File f:files) {
				f.delete();
			}
		}
	}
	public class MemoryCache {
		private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();

		public Bitmap get(String id) {
			if(!cache.containsKey(id)) {
				return null;
			}
			SoftReference<Bitmap> ref=cache.get(id);
			return ref.get();
		}

		public void put(String id, Bitmap bitmap) {
			cache.put(id, new SoftReference<Bitmap>(bitmap));
		}
		public void clear() {
			cache.clear();
		}
	}


	public void fetchRemoteImage(final String url, final RemoteImageLoader ril) {

		(new Thread() {
			@Override
			public void run(){
				if((url == null) || (url.length() == 0)){
					return;
				}
				Bitmap bmp = getBitmap(url);
				if(ril != null){
					ril.onLoad(bmp);
				}
			}
		}).start();

	}

	public interface RemoteImageLoader {
		public void onLoad(Bitmap bmp);
	}

}
