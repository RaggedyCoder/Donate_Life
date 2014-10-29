package com.project.bluepandora.blooddonation.activities;

import java.util.ArrayList;
import java.util.List;

import com.project.blupandora.donatelife.R;
import com.project.bluepandora.blooddonation.adapter.SlideMenuAdapter;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.data.Item;
import com.project.bluepandora.blooddonation.data.SlideItem;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.blooddonation.fragments.PlaceholderFragment;
import com.project.bluepandora.blooddonation.fragments.ProfileFragment;
import com.project.bluepandora.blooddonation.fragments.RequestFragment;
import com.project.bluepandora.blooddonation.helpers.URL;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	public static boolean backPressed = false;
	public ActionBar act;
	public Fragment mContent;
	public static ArrayList<Fragment> fragments;
	public static boolean sliding = false;
	DrawerLayout mDrawerLayout;
	protected DrawerSlideListners mDrawerSlideListners;
	ListView mDrawerListView;
	ActionBarDrawerToggle mActionBarDrawerToggle;
	private SlideMenuAdapter listAdapter;
	private List<Item> slideItems;
	public int prevpos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		act = getSupportActionBar();
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}
		if (mContent == null) {
			fragments = new ArrayList<Fragment>();
			fragments.add(new PlaceholderFragment());
			fragments.add(new RequestFragment());
			fragments.add(new ProfileFragment());
			mContent = fragments.get(0);
		}
		mDrawerListView = (ListView) findViewById(R.id.list_slidermenu);

		slideItems = new ArrayList<Item>();

		listAdapter = new SlideMenuAdapter(this, slideItems);
		mDrawerListView.setAdapter(listAdapter);
		if (mContent instanceof RequestFragment) {
			mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
		}
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.app_name, R.string.app_name) {
			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if (mContent instanceof RequestFragment) {
					((DrawerSlideListners) mContent).onDrawerslide(slideOffset);
				}
				Log.d(TAG, "" + slideOffset);
			}
		};
		String[] names = getResources()
				.getStringArray(R.array.nav_drawer_items);
		TypedArray icons = getResources().obtainTypedArray(
				R.array.nav_drawer_icons);
		TypedArray backgrounds = getResources().obtainTypedArray(
				R.array.nav_drawer_backgrounds);

		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

		SlideItem item = null;
		for (int i = 0; i < names.length; i++) {
			item = new SlideItem();
			item.setSlideItem(names[i]);
			item.setIcons(icons.getResourceId(i, -1));
			item.setBackground(backgrounds.getResourceId(i, -1));
			slideItems.add(item);

		}
		icons.recycle();
		backgrounds.recycle();
		mDrawerListView.setOnItemClickListener(new ListItemListner());
		listAdapter.notifyDataSetInvalidated();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_container, mContent).commit();
		if (mContent instanceof PlaceholderFragment) {
			listAdapter.setSelected(0);
			prevpos = 0;
		} else if (mContent instanceof RequestFragment) {
			mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
			listAdapter.setSelected(1);
			prevpos = 1;
		} else if (mContent instanceof ProfileFragment) {
			listAdapter.setSelected(2);
			prevpos = 2;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (id == R.id.action_logout) {
			ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(true);
			pd.setTitle("Logging out.");
			pd.show();
			deleteFile("feed.json");
			UserDataSource userDatabase = new UserDataSource(this);
			userDatabase.open();
			ArrayList<UserInfoItem> items = new ArrayList<UserInfoItem>();
			items = userDatabase.getAllUserItem();
			for (UserInfoItem i : items) {
				userDatabase.deleteUserInfoitem(i);
			}
			pd.dismiss();
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);

	}

	@Override
	public void onBackPressed() {

		if (backPressed) {
			finish();
			AppController.getInstance().getRequestQueue().getCache()
					.remove(URL.URL);
			MainActivity.this.overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			backPressed = false;
			PlaceholderFragment.firstTime = true;
			finish();
			super.onBackPressed();
		} else {

			Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT)
					.show();
			backPressed = true;
			Handler h = new Handler();
			h.postDelayed(new Runnable() {
				public void run() {
					backPressed = false;
				}
			}, 2000);
		}
	}

	public void switchContent() {
		prevpos = 1;
		listAdapter.setSelected(1);
		mContent = fragments.get(1);
		mDrawerListView.setItemChecked(1, true);
		mDrawerListView.setSelection(1);
		mDrawerLayout.closeDrawer(mDrawerListView);
		mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.grow, R.anim.dim)
				.replace(R.id.frame_container, mContent).commit();
	}

	private class ListItemListner implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			listAdapter.setSelected(position);
			if (position > 3) {
				mDrawerLayout.closeDrawer(mDrawerListView);
				return;
			}
			if (prevpos != position) {
				switchContent(position);
				prevpos = position;
			} else {
				mDrawerLayout.closeDrawer(mDrawerListView);
			}
		}

	}

	public void switchContent(int pos) {
		AppController.getInstance().cancelPendingRequests();
		backPressed = false;
		if (pos == 3) {
			mDrawerLayout.closeDrawer(mDrawerListView);
			return;
		}
		if ((mContent instanceof RequestFragment)
				&& (fragments.get(pos) instanceof RequestFragment)) {
			Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
			mDrawerLayout.closeDrawer(mDrawerListView);
			return;
		} else if ((mContent instanceof PlaceholderFragment)
				&& (fragments.get(pos) instanceof PlaceholderFragment)) {
			Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
			mDrawerLayout.closeDrawer(mDrawerListView);
			return;
		} else if ((mContent instanceof ProfileFragment)
				&& (fragments.get(pos) instanceof ProfileFragment)) {
			Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
			mDrawerLayout.closeDrawer(mDrawerListView);
			return;
		}
		PlaceholderFragment.slideChange = true;
		if (pos == 1) {
			mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
		}
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right)
				.replace(R.id.frame_container, fragments.get(pos)).commit();
		mDrawerListView.setItemChecked(pos, true);
		mDrawerListView.setSelection(pos);
		mDrawerLayout.closeDrawer(mDrawerListView);
		mContent = fragments.get(pos);
	}

	public interface DrawerSlideListners {
		public void onDrawerslide(float offset);
	}
}
