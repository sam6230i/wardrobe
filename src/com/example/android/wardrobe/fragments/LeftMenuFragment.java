package com.example.android.wardrobe.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.R;

/**
 * Created with IntelliJ IDEA.
 * User: sam
 * Date: 10/27/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class LeftMenuFragment extends Fragment {

	//	private static final int NUM_PAGES = 5;


	public HomeActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View root = inflater.inflate(R.layout.left_menu, container, false);

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
		this.activity = (HomeActivity)activity;
	}

}
