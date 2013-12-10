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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.util.JuSmartImageView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * com.example.android.wardrobe.HomeActivity} samples.</p>
 */
public class FavoritesPagerFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String SHIRT_IMAGE_PATH = "shirt_image_path";
    public static final String PANT_IMAGE_PATH = "pant_image_path";

    /**
     * The fragment's page number, which is set to the argument value for {@link #SHIRT_IMAGE_PATH}.
     */
    private String shirtImagePath, pantImagePath;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FavoritesPagerFragment create(String shirtImagePath, String pantImagePath) {
        FavoritesPagerFragment fragment = new FavoritesPagerFragment();
        Bundle args = new Bundle();
        args.putString(SHIRT_IMAGE_PATH, shirtImagePath);
        args.putString(PANT_IMAGE_PATH, pantImagePath);
        fragment.setArguments(args);
        return fragment;
    }

    public FavoritesPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shirtImagePath = getArguments().getString(SHIRT_IMAGE_PATH);
	    pantImagePath = getArguments().getString(PANT_IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.favorites_screen, container, false);

        // Set the title view to show the page number.
//	    File bitmapFile = new File(shirtImagePath);
//	    Bitmap shirtBitmap = BitmapFactory.decodeFile(shirtImagePath);
//	    Bitmap pantBitmap = BitmapFactory.decodeFile(pantImagePath);
	    ((JuSmartImageView) rootView.findViewById(R.id.shirtImage)).setLocalImageUrl(shirtImagePath);
	    ((JuSmartImageView) rootView.findViewById(R.id.pantImage)).setLocalImageUrl(pantImagePath);
/*			    ((ImageView) rootView.findViewById(R.id.image)).setText(
			    getString(R.string.title_template_step, shirtImagePath + 1));*/

        return rootView;
    }

/*    public int getPageNumber() {
        return shirtImagePath;
    }*/
}
