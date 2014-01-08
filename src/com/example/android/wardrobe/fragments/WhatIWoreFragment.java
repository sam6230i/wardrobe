package com.example.android.wardrobe.fragments;

import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sqlite.Wore;
import com.example.android.wardrobe.FavoritesPagerFragment;
import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.R;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 10/27/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class WhatIWoreFragment extends Fragment {

	//	private static final int NUM_PAGES = 5;


	/**
	 * The pager widget, which handles animation and allows swiping horizontally to access previous
	 * and next wizard steps.
	 */
	public ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	public PagerAdapter mPagerAdapter;

	private HomeActivity activity;
	private TextView timeView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.activity_what_i_wore, container, false);

		timeView = (TextView) root.findViewById(R.id.time);
		activity.wores = activity.wardrobeDataSource.getWhatIWore();

		if(!activity.wores.isEmpty()) {
			timeView.setText(activity.wores.get(0).getWoreDateStr());
		}

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) root.findViewById(R.id.pager);
		mPagerAdapter = new WhatIWorePagerAdapter(activity, activity.getFragmentManager(), activity.wores);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				timeView.setText(activity.wores.get(position).getWoreDateStr());

				activity.invalidateOptionsMenu();
			}
		});

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
		this.activity = (HomeActivity)activity;
	}

	private class WhatIWorePagerAdapter extends FragmentStatePagerAdapter {

		private Context context;
		private List list;

		public WhatIWorePagerAdapter(Context context, android.app.FragmentManager fragmentManager, List list) {
			super(fragmentManager);
			this.context = context;
			this.list = list;
		}

		@Override
		public android.app.Fragment getItem(int position) {
			Wore wore = (Wore) list.get(position);
			String shirtImagePath = wore.getShirtPath();
			String pantImagePath = wore.getPantPath();
			return FavoritesPagerFragment.create(shirtImagePath, pantImagePath);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}


}
