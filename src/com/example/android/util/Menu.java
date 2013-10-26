package com.example.android.util;

import com.example.android.wardrobe.R;
import com.example.android.wardrobe.fragments.FavoritesFragment;
import com.example.android.wardrobe.fragments.SelectionFragment;
import com.example.android.wardrobe.fragments.WhatIWoreFragment;

/**
 * User: rakesh
 * Date: 27/10/13
 * Time: 3:29 AM
 */
public enum Menu {
	SELECTION(SelectionFragment.class, R.string.selection, R.drawable.cloth_icon_up, R.drawable.cloth_icon_dn),
	FAVOURITE(FavoritesFragment.class, R.string.favourites, R.drawable.like_icon_up, R.drawable.like_icon_dn),
	HISTORY(WhatIWoreFragment.class, R.string.history, R.drawable.like_settings_up, R.drawable.like_settings_dn);

	public int drawableId;
	public int selectedDrawableId;
	public int labelId;
	public Class<?> klass;

	Menu(Class<?> klass, int labelId, int drawableId, int selectedDrawableId) {
		this.klass = klass;
		this.labelId = labelId;
		this.drawableId = drawableId;
		this.selectedDrawableId = selectedDrawableId;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getSelectedDrawableId() {
		return selectedDrawableId;
	}

	public void setSelectedDrawableId(int selectedDrawableId) {
		this.selectedDrawableId = selectedDrawableId;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public Class<?> getKlass() {
		return klass;
	}

	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}
}
