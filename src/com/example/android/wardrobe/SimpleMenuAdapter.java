package com.example.android.wardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sameermhatre
 * Date: 15/11/12
 * Time: 12:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMenuAdapter extends BaseAdapter {
  private final Context context;
  private List<ContextOption> options;
  private LayoutInflater inflater;

  public SimpleMenuAdapter(Context context, List<ContextOption> options) {
    this.context = context;
    this.options = options;
    inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ContextOption option = options.get(position);
    View rowView = inflater.inflate(R.layout.layer_row, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.layername);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.layerimage);
    textView.setText(option.getName());
    if(option.getdDrawable() != null){
      imageView.setImageDrawable(option.getdDrawable());
    }else {
      imageView.setImageResource(option.getDrawable());
    }
    return rowView;
  }

  @Override
  public int getCount() {
    return options.size();
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }
}
