package com.project.bluepandora.blooddonation.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.*;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.project.bluepandora.blooddonation.activities.MainActivity;
import com.project.bluepandora.blooddonation.activities.RegistrationActivity;
import com.project.bluepandora.blooddonation.adapter.CountryListAdapter;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.blooddonation.helpers.URL;
import com.project.bluepandora.blooddonation.jsonperser.JSONParser;
import com.project.bluepandora.blooddonation.volley.CustomRequest;
import com.project.blupandora.donatelife.R;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class CheckRegistrationFragment extends Fragment {

	public CustomTextView contryCode;
	private JSONParser parse;
	public Spinner spinner;
	public Button nextActivity;
	public ProgressDialog pd;
	public final String TAG = RegistrationActivity.class.getSimpleName();
	public ArrayList<String> countryCodes;
	public CustomEditText mobileNumber;
	public CustomEditText password;
	public CustomTextView warningText;
	public JSONObject jsonObject;
	private Drawable mActionBarBackgroundDrawable;
	public HashMap<String, String> params = new HashMap<String, String>();
	public String MOBILE_CHECK = "mobileNumber";
	public View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_registration_check,
				container, false);
		nextActivity = (Button) rootView.findViewById(R.id.registration_submit);
		spinner = (Spinner) rootView.findViewById(R.id.registration_country);

		parse = new JSONParser(getActivity());
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("Select a Country");
		categories.add("Bangladesh");

		countryCodes = new ArrayList<String>();
		countryCodes.add("");
		mobileNumber = (CustomEditText) rootView
				.findViewById(R.id.registration_phone);

		password = (CustomEditText) rootView.findViewById(R.id.check_reg_pass);
		countryCodes.add("880");
		warningText = (CustomTextView) rootView.findViewById(R.id.warningText);
		contryCode = (CustomTextView) rootView
				.findViewById(R.id.registration_cc);
		warningText.setVisibility(View.GONE);
		mActionBarBackgroundDrawable = getResources().getDrawable(
				R.drawable.actionbar_background);
		mActionBarBackgroundDrawable.setAlpha(255);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setBackgroundDrawable(mActionBarBackgroundDrawable);
		CountryListAdapter dataAdapter = new CountryListAdapter(getActivity(),
				categories);
		spinner.setAdapter(dataAdapter);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mobileNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count > 0 && (s.charAt(0) != '1')) {
					mobileNumber.setText("");
				}
				if (warningText.getVisibility() == View.VISIBLE) {
					warningText.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (warningText.getVisibility() == View.VISIBLE) {
					warningText.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				contryCode.setText(countryCodes.get(position));
				contryCode.setError(null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		nextActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mobileNumber.clearFocus();
				password.clearFocus();
				if (mobileNumber.getText().length() == 0) {
					warningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					mobileNumber.setError(getActivity().getResources()
							.getString(R.string.Warning_no_number));
					warningText.setText(R.string.Warning_no_number);
					warningText.setVisibility(View.VISIBLE);
					return;
				} else if (mobileNumber.getText().length() < 10) {
					warningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					mobileNumber.setError(getActivity().getResources()
							.getString(R.string.Warning_number_short));
					warningText.setText(R.string.Warning_number_short);
					warningText.setVisibility(View.VISIBLE);
					return;
				}
				if (contryCode.getText().length() == 0) {
					warningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					warningText.setText(R.string.Warning_select_a_country);
					contryCode.setError(getActivity().getResources().getString(
							R.string.Warning_select_a_country));
					warningText.setVisibility(View.VISIBLE);
					return;
				} else if (warningText.getVisibility() == View.VISIBLE) {
					warningText.setVisibility(View.GONE);
				}

				pd = new ProgressDialog(CheckRegistrationFragment.this
						.getActivity());
				pd.setMessage(getActivity().getResources().getString(
						R.string.loading));
				pd.setIndeterminate(false);
				pd.setCancelable(false);
				pd.show();
				params = new HashMap<String, String>();
				params.put(URL.REQUEST_NAME, URL.BLOODLIST_PARAM);
				getJsonData(params);
				params = new HashMap<String, String>();
				params.put(URL.REQUEST_NAME, URL.DISTRICTLIST_PARAM);
				getJsonData(params);
				params = new HashMap<String, String>();
				params.put(URL.REQUEST_NAME, URL.HOSPITALLIST_PARAM);
				getJsonData(params);
				params = new HashMap<String, String>();
				params.put(URL.REQUEST_NAME, URL.REGISTER_CHECK);
				params.put(MOBILE_CHECK, "0" + mobileNumber.getText());
				getJsonData(params);
			}
		});
	}

	private void parseJsonregdata(JSONObject response) {
		try {
			boolean data = response.getBoolean("reg");
			if (data) {
				if (password.getText().length() == 0) {
					warningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					warningText.setText(R.string.Warning_enter_a_password);
					password.setError(getActivity().getResources().getString(
							R.string.Warning_enter_a_password));
					warningText.setVisibility(View.VISIBLE);
					pd.dismiss();
					return;
				}
				params = new HashMap<String, String>();
				params.put(URL.REQUEST_NAME, URL.USER_INFO);
				params.put(MOBILE_CHECK, "" + 0 + mobileNumber.getText());
				params.put(URL.PASSWORD_TAG, "" + password.getText());
				getJsonData(params);

			} else {
				if (password.getText().length() != 0) {
					warningText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					warningText.setText(R.string.Warning_remove_the_password);
					password.setError(getActivity().getResources().getString(
							R.string.Warning_remove_the_password));
					warningText.setVisibility(View.VISIBLE);
					pd.dismiss();
				} else {
					pd.dismiss();
					RegistrationActivity ra = (RegistrationActivity) getActivity();
					ra.changeFragement("" + mobileNumber.getText());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJsonUserInfo(JSONObject response) {

		int data = -1;
		try {
			data = response.getInt("done");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		if (data == 0) {
			warningText.setText(R.string.Warning_wrong_password);
			warningText.setVisibility(View.VISIBLE);
			pd.dismiss();
			return;
		} else if (data == 1) {
			UserDataSource userDataBase = new UserDataSource(getActivity());
			userDataBase.open();
			UserInfoItem item = new UserInfoItem();
			JSONArray feedArray = null;
			try {
				feedArray = response.getJSONArray("profile");
			} catch (JSONException e) {

				e.printStackTrace();
			}
			if (feedArray != null) {
				for (int i = 0; i < feedArray.length(); i++) {
					JSONObject temp = null;
					try {
						temp = (JSONObject) feedArray.get(i);
					} catch (JSONException e) {

						e.printStackTrace();
					}
					if (temp != null) {
						try {
							item.setFirstName(temp.getString("firstName"));
							item.setLastName(temp.getString("lastName"));
							item.setKeyWord("" + password.getText());
							item.setMobileNumber("" + 0
									+ mobileNumber.getText());
							item.setGroupId(Integer.parseInt(temp
									.getString("groupId")));
							item.setDistId(Integer.parseInt(temp
									.getString("distId")));
							pd.dismiss();
							try {
								userDataBase.createUserInfoItem(item);
								userDataBase.close();
								Intent intent = new Intent(getActivity(),
										MainActivity.class);
								getActivity().startActivity(intent);
								getActivity().finish();
							} catch (Exception e) {
								// Toast.makeText(getActivity(), e.getMessage(),
								// Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {

							e.printStackTrace();
						}
					}
				}
			}

		}
	}

	public void getJsonData(final HashMap<String, String> params) {

		CustomRequest jsonReq = new CustomRequest(Method.POST, URL.URL, params,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						VolleyLog.d(TAG, "Response: " + response.toString());
						if (response != null) {

							if (params.containsValue(URL.BLOODLIST_PARAM)) {
								parse.parseJsonBlood(response);
							} else if (params
									.containsValue(URL.DISTRICTLIST_PARAM)) {
								parse.parseJsonDistrict(response);
							} else if (params
									.containsValue(URL.HOSPITALLIST_PARAM)) {
								parse.parseJsonHospital(response);
							} else if (params.containsValue(""
									+ password.getText())
									&& params.containsValue("" + 0
											+ mobileNumber.getText())) {
								/*
								 * Toast.makeText(
								 * CheckRegistrationFragment.this
								 * .getActivity(), response.toString() + ":/",
								 * Toast.LENGTH_LONG).show();
								 */
								parseJsonUserInfo(response);
							} else if (params.containsValue("" + 0
									+ mobileNumber.getText())) {
								/*
								 * Toast.makeText(
								 * CheckRegistrationFragment.this
								 * .getActivity(), response.toString(),
								 * Toast.LENGTH_LONG) .show();
								 */
								parseJsonregdata(response);
							}
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						VolleyLog.d(TAG, "Error: " + error.getMessage());
						pd.dismiss();
						Toast.makeText(
								CheckRegistrationFragment.this.getActivity(),
								error.getMessage(), Toast.LENGTH_LONG).show();

					}
				});
		AppController.getInstance().addToRequestQueue(jsonReq);
	}

}
