package com.project.bluepandora.blooddonation.activities;
/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;

import java.util.ArrayList;

//import android.widget.Toast;

public class StartActivity extends Activity {

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

}
