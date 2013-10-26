package com.example.android.util;

import android.content.Context;
import com.loopj.android.image.SmartImageView;

/**
 * User: rakesh
 * Date: 27/10/13
 * Time: 5:08 AM
 */
public class JuSmartImageView extends SmartImageView {

	public JuSmartImageView(Context context) {
		super(context);
	}

	public void setLocalImageUrl(String url) {
		setImage(new LocalImageCache(url));
	}
}
