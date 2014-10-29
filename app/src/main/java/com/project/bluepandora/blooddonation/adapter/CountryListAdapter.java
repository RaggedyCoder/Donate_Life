package com.project.bluepandora.blooddonation.adapter;

import java.util.ArrayList;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.blupandora.donatelife.R;
import com.widget.CustomTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CountryListAdapter extends BaseAdapter {

	// private Activity activity;
	private ArrayList<String> items;
	private LayoutInflater inflater;
	BloodItem bitem;

	public CountryListAdapter(Activity activity, ArrayList<String> items) {

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

		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_item, null);
		}
		CustomTextView textView = (CustomTextView) convertView
				.findViewById(R.id.spinner_item_name);

		String bitem = items.get(position);
		textView.setText(bitem);

		textView.setSelected(true);
		return convertView;
	}

}
