package com.project.bluepandora.donatelife.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.SMItem;

import java.util.List;

/**
 * Created by tuman on 11/1/2015.
 */
public class SmallMenuAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> feedItems;
    private int selectedPositon;

    public SmallMenuAdapter(Activity activity, List<Item> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        selectedPositon = 0;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedItems.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelected(int position) {
        selectedPositon = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.small_menu_item, parent, false);
            holder = new ViewHolder();
            holder.menuImageButton = (ImageButton) convertView.findViewById(R.id.menu_image_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SMItem item = (SMItem) feedItems.get(position);
        holder.menuImageButton.setImageResource(item.getButtonImage());
        if (position == selectedPositon) {
            holder.menuImageButton.setSelected(true);
        } else {
            holder.menuImageButton.setSelected(false);
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageButton menuImageButton;
    }
}
