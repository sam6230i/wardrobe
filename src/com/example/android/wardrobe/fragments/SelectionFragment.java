package com.example.android.wardrobe.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import com.example.android.sqlite.Pant;
import com.example.android.sqlite.Shirt;
import com.example.android.sqlite.WardrobeDataSource;
import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.R;
import com.example.android.wardrobe.ScreenSlidePageFragment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 10/27/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectionFragment extends Fragment {

	//	private static final int NUM_PAGES = 5;


	/**
	 * The pager widget, which handles animation and allows swiping horizontally to access previous
	 * and next wizard steps.
	 */
	public ViewPager mPager;
	public ViewPager mPager1;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	public PagerAdapter mPagerAdapter;
	public PagerAdapter mPagerAdapter1;


	private HomeActivity activity;

	private CheckBox favorite;
	private CheckBox selection;
	private ImageButton shuffle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.activity_screen_slide, container, false);

		activity.shirts = activity.wardrobeDataSource.getAllShirts();
		activity.pants = activity.wardrobeDataSource.getAllPants();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) root.findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(activity, activity.getFragmentManager(), activity.shirts);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				check();
				activity.invalidateOptionsMenu();
			}
		});


		mPager1 = (ViewPager) root.findViewById(R.id.pager1);
		mPagerAdapter1 = new ScreenSlidePagerAdapter(activity, activity.getFragmentManager(), activity.pants);
		mPager1.setAdapter(mPagerAdapter1);
		mPager1.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				check();
				activity.invalidateOptionsMenu();
			}
		});

		favorite = (CheckBox)root.findViewById(R.id.favorite);

		favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int shirtPosition = mPager.getCurrentItem();
				int pantPosition = mPager1.getCurrentItem();
				if(isChecked) {
					activity.wardrobeDataSource.addFavourite(
							activity.shirts.get(shirtPosition).getId(),
							activity.pants.get(pantPosition).getId());
				} else {
					activity.wardrobeDataSource.deleteFavourite(
							activity.shirts.get(shirtPosition).getId(),
							activity.pants.get(pantPosition).getId());
				}
			}
		});

		selection = (CheckBox)root.findViewById(R.id.selection);

		selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int shirtPosition = mPager.getCurrentItem();
				int pantPosition = mPager1.getCurrentItem();
				if(isChecked) {
					activity.wardrobeDataSource.addToWore(
							activity.shirts.get(shirtPosition).getId(),
							activity.pants.get(pantPosition).getId());
				} else {
					activity.wardrobeDataSource.deleteFromWore(
							activity.shirts.get(shirtPosition).getId(),
							activity.pants.get(pantPosition).getId());
				}

			}
		});

		shuffle = (ImageButton) root.findViewById(R.id.shuffle);

		shuffle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int shirtRandom = (int) (Math.random() * activity.shirts.size());
				int pantRandom = (int) (Math.random() * activity.pants.size());
				mPager.setCurrentItem(shirtRandom);
				mPager1.setCurrentItem(pantRandom);
			}
		});

		check();
		return root;
	}

	public void check() {
		int shirtPosition = mPager.getCurrentItem();
		int pantPosition = mPager1.getCurrentItem();
		if(activity.wardrobeDataSource.isWornToday(
				activity.shirts.get(shirtPosition).getId(),
				activity.pants.get(pantPosition).getId())) {
			selection.setChecked(true);
		} else {
			selection.setChecked(false);
		}

		if(activity.wardrobeDataSource.isFavourite(
				activity.shirts.get(shirtPosition).getId(),
				activity.pants.get(pantPosition).getId())) {
			favorite.setChecked(true);
		} else {
			favorite.setChecked(false);
		}
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
		this.activity = (HomeActivity)activity;
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		private Context context;
		private List list;

		public ScreenSlidePagerAdapter(Context context, FragmentManager fm, List list) {
			super(fm);
			this.context = context;
			this.list = list;
		}

		@Override
		public android.app.Fragment getItem(int position) {
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
