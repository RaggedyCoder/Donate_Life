package com.project.bluepandora.donatelife.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.BloodSpinnerAdapter;
import com.project.bluepandora.donatelife.adapter.DistrictSpinnerAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tuman on 5/11/2014.
 */
public class RegistrationCompleteFragment extends Fragment {

    /**
     * Defines a tag for identifying log entries
     */
    private static String TAG = RegistrationCompleteFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    protected View rootView;
    /**
     * A {@link Spinner} for showing the district's list
     */
    private Spinner districtSpinner;
    /**
     * A {@link android.widget.Spinner} for showing the blood group's list
     */
    private Spinner bloodSpinner;

    /**
     * A Custom BaseAdapter{@link com.project.bluepandora.donatelife.adapter.BloodSpinnerAdapter} for the bloodSpinner.
     */

    private BloodSpinnerAdapter bloodAdapter;

    /**
     * A Custom BaseAdapter{@link com.project.bluepandora.donatelife.adapter.DistrictSpinnerAdapter} for the districtSpinner.
     */

    private DistrictSpinnerAdapter districtAdapter;

    /**
     * A {@link CustomButton} for the Registration
     */
    private CustomButton registrationButton;
    /**
     * An {@link ArrayList} for storing the Name of the District only for this Fragment.
     */
    private ArrayList<DistrictItem> distItems;
    /**
     * An {@link ArrayList} for storing the Name of the District only for this Fragment.
     */
    private ArrayList<BloodItem> bloodItems;

    /**
     * A database{@link ArrayList} for retrieve the stored district list.
     */
    private DistrictDataSource districtDatabase;

    /**
     * A database{@link ArrayList} for retrieve the stored blood group list.
     */
    private BloodDataSource bloodDatabase;

    /**
     * A TextField{@link CustomTextView} for showing the blood group of the registering user.
     */
    private CustomTextView avatar;

    /**
     * An item selection listener for the bloodSpinner.
     */
    private OnItemSelectedListener mbloodSpinnerItemSelectedListener;
    /**
     * An item selection listener for the districtSpinner.
     */
    private OnItemSelectedListener mDistrictItemSelectedListener;
    /**
     * A click detection listener for the Registration Button.
     */
    private View.OnClickListener mRegistarionListener;
    /**
     * A {@link android.app.ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;

    public RegistrationCompleteFragment() {

    }

    public static Fragment newInstance(Bundle bundle) {
        RegistrationCompleteFragment fragment = new RegistrationCompleteFragment();
        fragment.setArguments(bundle);
        Log.i(TAG, "New instance created.");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate method called.");
        districtDatabase = new DistrictDataSource(getActivity());
        bloodDatabase = new BloodDataSource(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView method called.");
        rootView = inflater.inflate(R.layout.fragment_registrationcomplete, container, false);
        bloodSpinner = (Spinner) rootView.findViewById(R.id.blood_group_spinner);
        districtSpinner = (Spinner) rootView.findViewById(R.id.district_spinner);
        registrationButton = (CustomButton) rootView.findViewById(R.id.registration);
        avatar = (CustomTextView) rootView.findViewById(R.id.avatar);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated method called.");
        bloodDatabase.open();
        districtDatabase.open();
        bloodItems = bloodDatabase.getAllBloodItem();
        distItems = districtDatabase.getAllDistrictItem();
        bloodDatabase.close();
        districtDatabase.close();
        bloodAdapter = new BloodSpinnerAdapter(getActivity(), bloodItems);
        districtAdapter = new DistrictSpinnerAdapter(getActivity(), distItems);
        bloodSpinner.setAdapter(bloodAdapter);
        districtSpinner.setAdapter(districtAdapter);
        bloodAdapter.notifyDataSetChanged();
        districtAdapter.notifyDataSetChanged();
        bloodSpinner.setOnItemSelectedListener(mbloodSpinnerItemSelectedListener);
        districtSpinner.setOnItemSelectedListener(mDistrictItemSelectedListener);
        registrationButton.setOnClickListener(mRegistarionListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach method called.");
        mbloodSpinnerItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                avatar.setText(bloodItems.get(bloodSpinner.getSelectedItemPosition()).getBloodName());
                if (avatar.getText().toString().length() > 2) {
                    avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
                } else {
                    avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mDistrictItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mRegistarionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Tags and Value for Registration Request
                 * static final TAG->requestName.  static final Value->register
                 * static final TAG->firstName.    Value will be determined by the user.
                 * static final TAG->lastName.     Value will be determined by the user.
                 * static final TAG->groupId.      Value will be determined by the user.
                 * static final TAG->distId.      Value will be determined by the user.
                 * static final TAG->mobileNumber. Value will be determined by the user.
                 * static final TAG->keyWord.      Value will be determined by the user
                 */
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.REGISTERREQUEST_PARAM);
                params.put(URL.FIRST_NAME_KEY, getArguments().getString("firstName"));
                params.put(URL.LAST_NAME_KEY, getArguments().getString("lastName"));
                params.put(URL.DISTRICTID_TAG, distItems.get(districtSpinner.getSelectedItemPosition()).getDistId() + "");
                params.put(URL.GROUPID_TAG, bloodItems.get(bloodSpinner.getSelectedItemPosition()).getBloodId() + "");
                params.put(URL.MOBILE_TAG, getArguments().getString("mobileNumber"));
                params.put(URL.PASSWORD_TAG, getArguments().getString("password"));
                createProgressDialog();
                getJsonData(params);
            }
        };
    }

    private void createProgressDialog() {
        pd = new ProgressDialog(RegistrationCompleteFragment.this.getActivity());
        pd.setMessage(getActivity().getResources().getString(R.string.loading));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    private void createAlertDialog(final String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (message.equals("Done!")) {
                    getActivity().finish();
                }
            }
        });
        alertDialog.show();
    }

    private void getJsonData(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL.URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        pd.dismiss();
                        try {
                            if (response.getInt("done") == 1) {
                                createAlertDialog("Done!");
                            } else {
                                createAlertDialog("Sorry! something went wrong.");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());

                pd.dismiss();
                Toast.makeText(
                        RegistrationCompleteFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
}
