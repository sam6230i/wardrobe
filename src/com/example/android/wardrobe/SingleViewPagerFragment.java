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
 * HomeActivity} samples.</p>
 */
public class SingleViewPagerFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String IMAGE_PATH = "image_path";

    /**
     * The fragment's page number, which is set to the argument value for {@link #IMAGE_PATH}.
     */
    private String imagePath;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static SingleViewPagerFragment create(String imagePath) {
        SingleViewPagerFragment fragment = new SingleViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_PATH, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    public SingleViewPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePath = getArguments().getString(IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.single_view_pager, container, false);

        // Set the title view to show the page number.
//	    File bitmapFile = new File(imagePath);
//	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//	    ((SmartImageView) rootView.findViewById(R.id.image)).setImageBitmap(bitmap);

	    ((JuSmartImageView) rootView.findViewById(R.id.image)).setLocalImageUrl(imagePath);
/*			    ((ImageView) rootView.findViewById(R.id.image)).setText(
			    getString(R.string.title_template_step, imagePath + 1));*/

        return rootView;
    }

/*    public int getPageNumber() {
        return imagePath;
    }*/
}
