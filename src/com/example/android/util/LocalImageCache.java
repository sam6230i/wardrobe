package com.example.android.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.WebImageCache;

public class LocalImageCache implements SmartImage {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;

	private static WebImageCache webImageCache;

	private String url;

	public LocalImageCache(String url) {
		this.url = url;
	}

	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if(webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}

		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if(url != null) {
			bitmap = webImageCache.get(url);
			if(bitmap == null) {
				bitmap = getBitmapFromUrl(url);
				if(bitmap != null){
					webImageCache.put(url, bitmap);
				}
			}
		}

		return bitmap;
	}

	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = BitmapFactory.decodeFile(url);

		return bitmap;
	}

	public static void removeFromCache(String url) {
		if(webImageCache != null) {
			webImageCache.remove(url);
		}
	}
}

