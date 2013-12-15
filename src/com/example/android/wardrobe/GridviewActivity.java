package com.example.android.wardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;

public class GridviewActivity extends Activity
{

	private static final String TAG = "GridviewActivity";

	private Context mContext;

	private GridView mGridView;

	private ImageArrayAdapter mImageArrayAdapter;

	private String[] imageUrls;

	private int mImageSize = 0;

	private final int mNoOfColumns = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "true" : "false") + " null");
		setContentView(R.layout.screen_gridview_with_loading);

		mContext = this;
		imageUrls = this.getResources().getStringArray(R.array.string_array_images);

		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mImageSize = (metrics.widthPixels - 80) / mNoOfColumns;

		mGridView = (GridView) findViewById(R.id.image_list_gridview);
		mImageArrayAdapter = new ImageArrayAdapter(mContext, R.layout.layout_gridview_item, imageUrls, mImageSize);
		mGridView.setAdapter(mImageArrayAdapter);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			mGridView.setNumColumns(mNoOfColumns);
		}
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			mGridView.setNumColumns(mNoOfColumns + 1);
		}
	}
}