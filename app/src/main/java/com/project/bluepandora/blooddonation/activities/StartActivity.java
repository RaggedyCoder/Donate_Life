package com.project.bluepandora.blooddonation.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.blooddonation.volley.CustomRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//import android.widget.Toast;

public class StartActivity extends Activity {

	String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+Sydney&key=AIzaSyDzg-FZ8O9HUbf3vMsnJYXaCqCV9rS6Roo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserDataSource userDatabase = new UserDataSource(this);
		ArrayList<UserInfoItem> items = null;
		userDatabase.open();
		try {
			items = userDatabase.getAllUserItem();
			// Toast.makeText(this, items.size() + "",
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		if (items != null) {
			if (items.size() == 1) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(this, RegistrationActivity.class);
				startActivity(intent);
				finish();
			}
		} else {

			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			finish();
		}
	}

	public void a() {
		CustomRequest jReq = new CustomRequest(URL, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
					}
				});
		AppController.getInstance().addToRequestQueue(jReq);
	}

}
