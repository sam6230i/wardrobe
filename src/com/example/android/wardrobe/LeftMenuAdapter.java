package com.example.android.wardrobe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.util.Menu;

/**
 * User: rakesh
 * Date: 27/10/13
 * Time: 3:29 AM
 */
public class LeftMenuAdapter extends ArrayAdapter {
	HomeActivity homeActivity;

	public LeftMenuAdapter(HomeActivity homeActivity, int resource, int textViewResourceId) {
		super(homeActivity, resource, textViewResourceId, new Menu[]{Menu.SELECTION, Menu.FAVOURITE, Menu.HISTORY});
		this.homeActivity = homeActivity;;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.left_menu_row, null);
			holder = new ViewHolder();
			holder.imgAction = (ImageView) convertView.findViewById(R.id.img_action);
			holder.textView = (TextView) convertView.findViewById(R.id.txt_action);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Menu item = (Menu) getItem(position);
		holder.textView.setText(getContext().getResources().getString(item.getLabelId()));

		if (item.getKlass() == homeActivity.currentFragment.getClass()) {
			holder.imgAction.setImageResource(item.getSelectedDrawableId());
		} else {
			holder.imgAction.setImageResource(item.getDrawableId());
		}


		return convertView;
	}

	static class ViewHolder {
		public ImageView imgAction;
		public TextView textView;
	}
}
