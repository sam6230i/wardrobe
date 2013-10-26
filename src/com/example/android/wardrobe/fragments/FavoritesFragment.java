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
import com.example.android.sqlite.Favourite;
import com.example.android.sqlite.Pant;
import com.example.android.sqlite.Shirt;
import com.example.android.wardrobe.FavoritesPagerFragment;
import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.R;
import com.example.android.wardrobe.SingleViewPagerFragment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 10/27/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class FavoritesFragment extends Fragment {

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

	private CheckBox favorite;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.activity_favorites, container, false);

		activity.favourites = activity.wardrobeDataSource.getAllFavourites();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) root.findViewById(R.id.pager);
		mPagerAdapter = new FavoritesPagerAdapter(activity, activity.getFragmentManager(), activity.favourites);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				activity.invalidateOptionsMenu();
			}
		});


		favorite = (CheckBox)root.findViewById(R.id.favorite);

		favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int position = mPager.getCurrentItem();
				if(!activity.favourites.isEmpty()) {
				Favourite favourite = activity.favourites.get(position);
					activity.wardrobeDataSource.deleteFavourite(
							favourite.getShirtId(), favourite.getPantId()
					);
					activity.favourites.remove(position);
					mPagerAdapter.notifyDataSetChanged();
					mPager.invalidate();
				}
			}
		});

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
		this.activity = (HomeActivity)activity;
	}

	private class FavoritesPagerAdapter extends FragmentStatePagerAdapter {

		private Context context;
		private List list;

		public FavoritesPagerAdapter(Context context, FragmentManager fm, List list) {
			super(fm);
			this.context = context;
			this.list = list;
		}

		@Override
		public android.app.Fragment getItem(int position) {
			Favourite favourite = (Favourite) list.get(position);
			String shirtImagePath = favourite.getShirtPath();
			String pantImagePath = favourite.getPantPath();
			return FavoritesPagerFragment.create(shirtImagePath, pantImagePath);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}


}
