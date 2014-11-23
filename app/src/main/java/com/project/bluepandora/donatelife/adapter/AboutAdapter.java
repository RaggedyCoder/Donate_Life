package com.project.bluepandora.donatelife.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.data.AboutItem;
import com.widget.CustomTextView;

import java.util.List;

/**
 * Created by tuman on 23/11/2014.
 */
public class AboutAdapter extends BaseAdapter {

    private List<AboutItem> items;
    private Activity activity;
    private LayoutInflater inflater;

    public AboutAdapter(Activity activity, List<AboutItem> items) {
        this.items = items;
        this.activity = activity;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sliding_menu_row, null);
            holder = new ViewHolder();
            holder.header = (CustomTextView) convertView.findViewById(R.id.about_header);
            holder.body = (CustomTextView) convertView.findViewById(R.id.about_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.header.setText(items.get(position).getHeader());
        holder.body.setText(items.get(position).getBody());
        return convertView;
    }

    private static class ViewHolder {
        CustomTextView header;
        CustomTextView body;
    }
}
