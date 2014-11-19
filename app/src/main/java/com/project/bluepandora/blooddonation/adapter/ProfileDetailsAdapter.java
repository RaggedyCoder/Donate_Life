package com.project.bluepandora.blooddonation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.blooddonation.data.DistrictItem;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.BloodDataSource;
import com.project.bluepandora.blooddonation.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomButton;
import com.widget.CustomTextView;

import java.util.List;

public class ProfileDetailsAdapter extends BaseAdapter {

    UserInfoItem infoItem;
    List<String> header;
    private DistrictItem distItem;
    private BloodItem bloodItem;
    private Activity activity;
    private LayoutInflater inflater;
    private String district;
    private String bloodGroup;
    private BloodDataSource bloodDatabase;
    private DistrictDataSource districtDatabase;

    public ProfileDetailsAdapter(Activity activity, UserInfoItem infoItem, List<String> header) {
        this.activity = activity;
        this.infoItem = infoItem;
        this.header = header;
        bloodDatabase = new BloodDataSource(activity);
        bloodDatabase.open();
        districtDatabase = new DistrictDataSource(activity);
        districtDatabase.open();
        bloodItem = new BloodItem();
        bloodItem.setBloodId(infoItem.getGroupId());
        bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase
                .bloodItemToCursor(bloodItem));
        distItem = new DistrictItem();
        distItem.setDistId(infoItem.getDistId());
        distItem = districtDatabase.cursorToDistrictItem(districtDatabase
                .districtItemToCursor(distItem));
        bloodDatabase.close();
        districtDatabase.close();
    }

    @Override
    public int getCount() {
        return header.size();
    }


    @Override
    public Object getItem(int position) {
        return header.get(position);
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
            convertView = inflater.inflate(R.layout.profile_details, null);
            holder = new ViewHolder();
            holder.profileHeaderHolder = (RelativeLayout) convertView.findViewById(R.id.profile_header_holder);
//            holder.edit = (CustomButton) convertView.findViewById(R.id.profile_edit);
            holder.header = (CustomTextView) convertView.findViewById(R.id.details_header);
            holder.description = (CustomTextView) convertView.findViewById(R.id.details_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position != 0) {
            holder.profileHeaderHolder.setVisibility(View.GONE);
        } else {
            holder.profileHeaderHolder.setVisibility(View.VISIBLE);
        }
        holder.header.setText(header.get(position));
        holder.description.setText(getDescription(position));
        return convertView;
    }

    private String getDescription(int position) {
        if (position == 0) {
            return infoItem.getFirstName() + " " + infoItem.getLastName();
        } else if (position == 1) {
            return infoItem.getMobileNumber();
        } else if (position == 2) {
            return distItem.getDistName();
        } else if (position == 3) {
            return bloodItem.getBloodName();
        } else if (position == 4) {
            return "No Donation Record Found";
        }
        return null;
    }

    static class ViewHolder {
        RelativeLayout profileHeaderHolder;
        CustomButton edit;
        CustomTextView header;
        CustomTextView description;
    }
}
