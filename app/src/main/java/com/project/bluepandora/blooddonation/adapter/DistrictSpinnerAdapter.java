package com.project.bluepandora.blooddonation.adapter;

import java.util.ArrayList;

import com.project.bluepandora.blooddonation.data.DistrictItem;
import com.project.blupandora.donatelife.R;
import com.widget.CustomTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DistrictSpinnerAdapter extends BaseAdapter {

	// private Activity activity;
	private ArrayList<DistrictItem> items;
	private LayoutInflater inflater;

	public DistrictSpinnerAdapter(Activity activity,
			ArrayList<DistrictItem> items) {
		this.items = items;
		// this.activity = activity;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int position) {

		return items.get(position);
	}

	@Override
	public long getItemId(int position) {

		DistrictItem ditem = (DistrictItem) items.get(position);
		return ditem.getDistId();
	}

	public int getId(int position) {

		DistrictItem ditem = (DistrictItem) items.get(position);
		return ditem.getDistId();
	}

	public int getItemPosition(int id) {
		int position = 0;
		for (DistrictItem dItem : items) {
			if (dItem.getDistId() == id) {
				return position;
			}
			position++;
		}
		return -1;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_item, null);
		}
		CustomTextView textView = (CustomTextView) convertView
				.findViewById(R.id.spinner_item_name);

		DistrictItem ditem = items.get(position);
		textView.setText(ditem.getDistName());

		textView.setSelected(true);
		return convertView;
	}

}
