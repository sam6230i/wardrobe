package com.example.android.util;

import android.content.Context;
import android.util.AttributeSet;
import com.loopj.android.image.SmartImageView;

/**
 * User: rakesh
 * Date: 27/10/13
 * Time: 5:08 AM
 */
public class JuSmartImageView extends SmartImageView {

	public JuSmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public JuSmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public JuSmartImageView(Context context) {
		super(context);
	}

	public void setLocalImageUrl(String url) {
		setImage(new LocalImageCache(url));
	}
}
