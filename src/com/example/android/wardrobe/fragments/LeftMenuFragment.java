package com.example.android.wardrobe.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.util.Menu;
import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.LeftMenuAdapter;
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
	ListView leftList;
	LeftMenuAdapter adapter;
	Button mBtnShirt, mBtnPant, mBtnSurfaceCamShirt, mBtnSurfaceCamPant, mBtnGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = inflater.inflate(R.layout.left_menu, null);
		leftList = (ListView) view.findViewById(R.id.left_list);
		mBtnShirt = (Button) view.findViewById(R.id.left_menu_bottom_btn_shirt);
		mBtnPant = (Button) view.findViewById(R.id.left_menu_bottom_btn_pant);
		mBtnSurfaceCamShirt = (Button) view.findViewById(R.id.left_menu_bottom_btn_cam_shirt);
		mBtnSurfaceCamPant = (Button) view.findViewById(R.id.left_menu_bottom_btn_cam_pant);
		mBtnGridView = (Button) view.findViewById(R.id.left_menu_grid_button);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);    //To change body of overridden methods use File | Settings | File Templates.
		this.activity = (HomeActivity)activity;
	}

	@Override
	public void onStart() {
		super.onStart();
		adapter = new LeftMenuAdapter(activity, R.layout.left_menu_row, R.id.txt_action);
		leftList.setAdapter(adapter);
		leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				Adapter adapter = parent.getAdapter();
				Menu action = (Menu) adapter.getItem(position);
				activity.onActionSelected(action);
			}
		});
		
		OnClickListener btnClickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				activity.onButtonClicked(v.getTag().toString());
			}
		};
		
		mBtnPant.setOnClickListener(btnClickListener);
		mBtnShirt.setOnClickListener(btnClickListener);
		mBtnSurfaceCamPant.setOnClickListener(btnClickListener);
		mBtnSurfaceCamShirt.setOnClickListener(btnClickListener);
		mBtnGridView.setOnClickListener(btnClickListener);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


	}

	public void notifyDataChanged() {
		adapter.notifyDataSetChanged();
	}

}
