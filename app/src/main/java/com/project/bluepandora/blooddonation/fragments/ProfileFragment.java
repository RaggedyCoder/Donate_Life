package com.project.bluepandora.blooddonation.fragments;

import java.util.ArrayList;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.blooddonation.data.DistrictItem;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.BloodDataSource;
import com.project.bluepandora.blooddonation.datasource.DistrictDataSource;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.widget.CustomTextView;
import com.project.blupandora.donatelife.R;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	private UserDataSource userDatabase;
	private ArrayList<UserInfoItem> userInfo;
	private BloodDataSource bloodDatabase;
	private DistrictDataSource districtDatabase;
	private DistrictItem distItem;
	private BloodItem bloodItem;
	private TextView avatar;
	private TextView userName;
	private TextView mobileNumber;
	private TextView districtName;
	private TextView bloodGroup;
	private TextView profileDetail;
	private TextView profileEdit;

	private TextView fullName;

	private TextView mobileNumber_show;
	private TextView districtName_show;
	private TextView bloodGroup_show;
	private TextView fullName_show;

	private CustomTextView mTitle;
	private Drawable mActionBarBackgroundDrawable;
	private View mCustomView;

	public ProfileFragment() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				"MuseoSansRounded-700.otf");
		userDatabase = new UserDataSource(getActivity());
		userDatabase.open();
		bloodDatabase = new BloodDataSource(getActivity());
		bloodDatabase.open();
		districtDatabase = new DistrictDataSource(getActivity());
		districtDatabase.open();
		userInfo = userDatabase.getAllUserItem();
		bloodItem = new BloodItem();
		bloodItem.setBloodId(userInfo.get(0).getGroupId());
		bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase
				.bloodItemToCursor(bloodItem));

		distItem = new DistrictItem();
		distItem.setDistId(userInfo.get(0).getDistId());
		distItem = districtDatabase.cursorToDistrictItem(districtDatabase
				.districtItemToCursor(distItem));
		bloodDatabase.close();
		userDatabase.close();
		districtDatabase.close();
		avatar = (TextView) rootView.findViewById(R.id.avatar);
		avatar.setTypeface(tf);
		avatar.setText(bloodItem.getBloodName());

		userName = (TextView) rootView.findViewById(R.id.username);
		userName.setText(userInfo.get(0).getFirstName());
		userName.setTypeface(tf);

		mobileNumber = (TextView) rootView
				.findViewById(R.id.profile_mobile_number);
		mobileNumber.setTypeface(tf);
		mobileNumber.setText(userInfo.get(0).getMobileNumber());

		districtName = (TextView) rootView.findViewById(R.id.profile_area_name);
		districtName.setText(distItem.getDistName());
		districtName.setTypeface(tf);

		bloodGroup = (TextView) rootView.findViewById(R.id.profile_blood_group);
		bloodGroup.setText(bloodItem.getBloodName());
		bloodGroup.setTypeface(tf);

		fullName = (TextView) rootView.findViewById(R.id.profile_full_name);
		fullName.setText(userInfo.get(0).getFirstName() + " "
				+ userInfo.get(0).getLastName());
		fullName.setTypeface(tf);

		bloodGroup_show = (TextView) rootView
				.findViewById(R.id.profile_blood_group_show);
		bloodGroup_show.setTypeface(tf);
		mobileNumber_show = (TextView) rootView
				.findViewById(R.id.profile_mobile_number_show);
		mobileNumber_show.setTypeface(tf);

		districtName_show = (TextView) rootView
				.findViewById(R.id.profile_area_name_show);
		districtName_show.setTypeface(tf);
		fullName_show = (TextView) rootView
				.findViewById(R.id.profile_full_name_show);
		fullName_show.setTypeface(tf);

		profileDetail = (TextView) rootView.findViewById(R.id.profile_details);
		profileDetail.setTypeface(tf);

		profileEdit = (TextView) rootView.findViewById(R.id.profile_edit);
		profileEdit.setTypeface(tf);
		mActionBarBackgroundDrawable = getResources().getDrawable(
				R.drawable.actionbar_background);

		mActionBarBackgroundDrawable.setAlpha(255);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		mCustomView = mInflater.inflate(R.layout.request_feed_actionbar, null);

		mTitle = (CustomTextView) mCustomView
				.findViewById(R.id.actionbar_title_text);

		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setDisplayShowTitleEnabled(false);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setDisplayHomeAsUpEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setHomeButtonEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setDisplayShowCustomEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setDisplayShowHomeEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setCustomView(mCustomView);
		mTitle.setText(R.string.profile);
		return rootView;
	}

}
