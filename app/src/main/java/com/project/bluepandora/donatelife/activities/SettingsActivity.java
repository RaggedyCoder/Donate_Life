package com.project.bluepandora.donatelife.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.SettingsFragment;
import com.project.bluepandora.util.Utils;

/**
 * Created by tuman on 21/11/2014.
 */
public class SettingsActivity extends PreferenceActivity {

    public SettingsActivity() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Settings");
        if (Utils.hasHoneycomb()) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment()).commit();
        } else {
            setContentView(R.layout.activity_settings_legacy);
            Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
            actionbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            addPreferencesFromResource(R.xml.settings);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
