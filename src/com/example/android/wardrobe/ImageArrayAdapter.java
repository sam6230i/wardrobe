package com.example.android.wardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.android.util.ImageLoader;

public class ImageArrayAdapter extends ArrayAdapter<String>
{
	private final int mLayoutResourceId;

	private String[] mImageUrls = null;

	private ImageLoader mImageLoader;

	private int mImageSize = 0;

	public ImageArrayAdapter(Context context, int layoutResourceId, String[] urls, int imageSize)
	{
		super(context, layoutResourceId, urls);
		mLayoutResourceId = layoutResourceId;
		mImageUrls = urls;
		mImageSize = imageSize;
		mImageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount()
	{
		return mImageUrls.length;
	}

	@Override
	public long getItemId(int position)
	{
		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		ProgressBar progressBar;
		
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(mLayoutResourceId, null);
		}

		imageView = (ImageView) convertView.findViewById(R.id.list_row_image_imageview);
		progressBar = (ProgressBar) convertView.findViewById(R.id.list_row_image_progressBar);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mImageSize, mImageSize);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		imageView.setLayoutParams(layoutParams);
		
		mImageLoader.DisplayImage(getItem(position), imageView, progressBar);

		return convertView;
	}
}
