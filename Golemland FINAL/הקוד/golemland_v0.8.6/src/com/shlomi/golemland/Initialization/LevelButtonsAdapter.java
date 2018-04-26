package com.shlomi.golemland.Initialization;

import com.shlomi.golemland.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * This class is for level select menu design only.
 * 
 * @author Shlomi
 * 
 */

public class LevelButtonsAdapter extends BaseAdapter {

	private Context context;
	private String[] items;
	LayoutInflater inflater;

	public LevelButtonsAdapter(Context context, String[] items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.level_cell, null);
		}
		Button button = (Button) convertView.findViewById(R.id.grid_item);
		button.setSoundEffectsEnabled(false);
		button.setText(items[position]);

		return convertView;
	}

	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}// class