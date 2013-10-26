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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.android.sqlite.Pant;
import com.example.android.sqlite.PantDataSource;
import com.example.android.sqlite.Shirt;
import com.example.android.sqlite.ShirtDataSource;

import java.io.*;
import java.util.Arrays;

/**
 * The launchpad activity for this sample project. This activity launches other activities that
 * demonstrate implementations of common animations.
 */
public class MainActivity extends ListActivity {
	/**
	 * This class describes an individual sample (the sample title, and the activity class that
	 * demonstrates this sample).
	 */
	private class Sample {
		private CharSequence title;
		private Class<? extends Activity> activityClass;

		public Sample(int titleResId, Class<? extends Activity> activityClass) {
			this.activityClass = activityClass;
			this.title = getResources().getString(titleResId);
		}

		@Override
		public String toString() {
			return title.toString();
		}
	}

	/**
	 * The collection of all samples in the app. This gets instantiated in {@link
	 * #onCreate(android.os.Bundle)} because the {@link Sample} constructor needs access to {@link
	 * android.content.res.Resources}.
	 */
	private static Sample[] mSamples;
	private ShirtDataSource shirtDataSource;
	private PantDataSource pantDataSource;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		shirtDataSource = new ShirtDataSource(this);
		shirtDataSource.open();
		pantDataSource = new PantDataSource(this);
		pantDataSource.open();
		// Instantiate the list of samples.
		mSamples = new Sample[]{
				new Sample(R.string.add_shirt, CrossfadeActivity.class),
				new Sample(R.string.add_pant, CardFlipActivity.class),
				new Sample(R.string.show_in_pairs, ScreenSlideActivity.class),
/*                new Sample(R.string.title_zoom, ZoomActivity.class),
                new Sample(R.string.title_layout_changes, LayoutChangesActivity.class),*/
		};

		setListAdapter(new ArrayAdapter<Sample>(this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				mSamples));
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		// Launch the sample associated with this list position.
		switch (position) {
			case 0:
				loadImage(0);
				break;
			case 1:
				loadImage(1);
				break;
			case 2:
				startActivity(new Intent(MainActivity.this, mSamples[position].activityClass));
				break;
		}
	}

	private static final int
			PICK_FROM_FILE = 2,
			PICK_FROM_CAMERA = 1;
	private Uri mImageCaptureUri;

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	int addWhat = 0;

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

					Bitmap bitmap = getResizedImageBitmap(
							mImageCaptureUri,
							-1, -1
					);
					if(addWhat == 0){
						shirtDataSource.addShirt(new Shirt(getRealPathFromURI(getImageUri(bitmap))));
					} else if(addWhat == 1) {
						pantDataSource.addPant(new Pant(getRealPathFromURI(getImageUri(bitmap))));
					}
//					original = getResizedImageBitmap(mImageCaptureUri, -1, -1);
//					setBitmap(getBitmap());
					break;
				case PICK_FROM_FILE:
					if(data != null){
						mImageCaptureUri = data.getData();
						Bitmap bitmap1 = getResizedImageBitmap(
								mImageCaptureUri,
								-1,-1
						);
						if(addWhat == 0){
							shirtDataSource.addShirt(new Shirt(getRealPathFromURI(getImageUri(bitmap1))));
						} else if(addWhat == 1) {
							pantDataSource.addPant(new Pant(getRealPathFromURI(getImageUri(bitmap1))));
						}

//						original = getResizedImageBitmap(mImageCaptureUri, -1, -1);
//						setBitmap(getBitmap());
					}
					break;
			}
		}
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
			int max = 250000;
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

}
