package com.example.android.wardrobe;

import android.graphics.drawable.Drawable;

/**
 * Created by IntelliJ IDEA.
 * User: sameermhatre
 * Date: 7/10/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextOption {
  private String name;
  private int drawable;
  private Drawable dDrawable;
  
  public ContextOption(String name, int drawable){
    this.name = name;
    this.drawable = drawable;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDrawable() {
    return drawable;
  }

  public void setDrawable(int drawable) {
    this.drawable = drawable;
  }

  public Drawable getdDrawable() {
    return dDrawable;
  }

  public void setdDrawable(Drawable dDrawable) {
    this.dDrawable = dDrawable;
  }
}
