/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wardrobe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;
import com.example.android.sqlite.Pant;
import com.example.android.sqlite.PantDataSource;
import com.example.android.sqlite.Shirt;
import com.example.android.sqlite.ShirtDataSource;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 * <p/>
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends SlidingFragmentActivity {
	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
//	private static final int NUM_PAGES = 5;

	private ShirtDataSource shirtDataSource;
	private PantDataSource pantDataSource;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally to access previous
	 * and next wizard steps.
	 */
	private ViewPager mPager;
	private ViewPager mPager1;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;
	private PagerAdapter mPagerAdapter1;

	private List<Shirt> shirts;
	private List<Pant> pants;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		setBehindContentView(R.layout.left_menu);
		shirtDataSource = new ShirtDataSource(this);
		shirtDataSource.open();
		pantDataSource = new PantDataSource(this);
		pantDataSource.open();

		shirts = shirtDataSource.getAllShirts();
		pants = pantDataSource.getAllPants();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(this, getFragmentManager(), shirts);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				invalidateOptionsMenu();
			}
		});


		mPager1 = (ViewPager) findViewById(R.id.pager1);
		mPagerAdapter1 = new ScreenSlidePagerAdapter(this, getFragmentManager(), pants);
		mPager1.setAdapter(mPagerAdapter1);
		mPager1.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				invalidateOptionsMenu();
			}
		});

		SlidingMenu sm = getSlidingMenu();
		sm.setAnimationCacheEnabled(true);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeEnabled(false);
		sm.setBehindScrollScale(0);
		sm.setMode(SlidingMenu.LEFT);
	}

	public void leftMenu(View view) {
		SlidingMenu slidingMenu = getSlidingMenu();
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent(true);
		} else {
			slidingMenu.showMenu(true);
		}
	}

/*	private void toggleMenu(boolean animate) {
		SlidingMenu slidingMenu = getSlidingMenu();
		if (slidingMenu.isMenuShowing() && !slidingMenu.isSecondaryMenuShowing()) {
			slidingMenu.showContent(animate);
		} else {
			slidingMenu.showMenu(animate);
		}
	}*/

	public void addShirt(View view) {
		loadImage(0);
	}

	public void addPant(View view) {
		loadImage(1);
	}

	private static final int
			PICK_FROM_FILE = 2,
			PICK_FROM_CAMERA = 1;
	private Uri mImageCaptureUri;
	int addWhat = 0;

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void loadImage(final int what){
		ContextOption[] items = new ContextOption[]{
				new ContextOption("Take picture from camera", R.drawable.camera),
				new ContextOption("Select picture from gallery", R.drawable.gallery)
		};
		SimpleMenuAdapter contextMenuAdapter = new SimpleMenuAdapter(this, Arrays.asList(items));
		new AlertDialog.Builder(this)
				// .setTitle("Share Appliction")
				.setAdapter(contextMenuAdapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int menuId) {
						Intent intent = null;
						switch (menuId) {
							case 0:
								addWhat = what;
								intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

								mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
										"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".png"));

								intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
								intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());

								try {
									intent.putExtra("return-data", true);

									startActivityForResult(intent, PICK_FROM_CAMERA);
								} catch (ActivityNotFoundException e) {
									e.printStackTrace();
									/**
									 * TODO show message to user
									 */
								}
								break;
							case 1:
								addWhat = what;
								intent = new Intent();

								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);

								startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
								break;
						}
					}
				}).show();
	}

	public Uri getImageUri(Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	  uiHelper.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode){
				case PICK_FROM_CAMERA:

					addShirtPant();
//					original = getResizedImageBitmap(mImageCaptureUri, -1, -1);
//					setBitmap(getBitmap());
					break;
				case PICK_FROM_FILE:
					if(data != null){
						mImageCaptureUri = data.getData();
						addShirtPant();

//						original = getResizedImageBitmap(mImageCaptureUri, -1, -1);
//						setBitmap(getBitmap());
					}
					break;
			}
		}
	}

	private void addShirtPant() {
		Bitmap bitmap = getResizedImageBitmap(
				mImageCaptureUri,
				-1, -1
		);
		if(addWhat == 0){
			shirts.add(shirtDataSource.addShirt(new Shirt(getRealPathFromURI(getImageUri(bitmap)))));
			mPagerAdapter.notifyDataSetChanged();
		} else if(addWhat == 1) {
			pants.add(pantDataSource.addPant(new Pant(getRealPathFromURI(getImageUri(bitmap)))));
			mPagerAdapter1.notifyDataSetChanged();
		}
		Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show();
	}

	private Bitmap getResizedImageBitmap(Uri uri, int widthLimit, int heightLimit) {
		InputStream input = null;
		int mWidth = 0, mHeight = 0;
		try {
			input = this.getContentResolver().openInputStream(uri);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, opt);
			mWidth = opt.outWidth;
			mHeight = opt.outHeight;
		} catch (FileNotFoundException e) {
			// Ignore
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}

		int outWidth = mWidth;
		int outHeight = mHeight;

		int scaleFactor = 1;
/*    int checkWidth = widthLimit;
    int checkHeight = heightLimit;
    if(checkWidth == -1){*/
		int min = Math.min(outWidth, outHeight);
		if(widthLimit == -1 || heightLimit == -1){
			//int max = 260000;
			int max = 1000000;
			int w = outWidth;
			int h = outHeight;
			for(int i = w; i >= 1; i--){
				int nh = Math.round(((float)h * (float)i) / (float)w);
				if(nh * i <= max){
					widthLimit = i;
					heightLimit = nh;
					break;
				}
			}
		}
//      checkWidth = 500; checkHeight = 500;
		//  }
		while ((outWidth / scaleFactor / 2 >= widthLimit && outHeight / scaleFactor / 2 >= heightLimit )) {
			scaleFactor *= 2;
		}
    /*    while ((min / scaleFactor > max1)) {
      scaleFactor *= 2;
    }*/
		// scaleFactor /= 2;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inSampleSize = scaleFactor;
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		try {
			input = this.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
//    int quality = MessageUtils.IMAGE_COMPRESSION_QUALITY;
		Bitmap b = BitmapFactory.decodeStream(input, null, options);
		if (b == null) {
			return null;
		}
		Bitmap bi = Bitmap.createScaledBitmap(b, widthLimit,
				heightLimit, true);
		return bi;
	}



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
	 * sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		private Context context;
		private List list;

		public ScreenSlidePagerAdapter(Context context, FragmentManager fm, List list) {
			super(fm);
			this.context = context;
			this.list = list;
		}

		@Override
		public Fragment getItem(int position) {
			String imagePath = "";
			if(list.get(position) instanceof Shirt) {
				imagePath = ((Shirt) list.get(position)).getImagePath();
			} else {
				imagePath = ((Pant) list.get(position)).getImagePath();
			}
			return ScreenSlidePageFragment.create(imagePath);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}
}
