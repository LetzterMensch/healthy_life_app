
package com.example.gr.controller.device.settings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.example.gr.R;
import com.example.gr.controller.activity.BaseActivity;


public abstract class AbstractSettingsActivityV2 extends BaseActivity implements
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    protected abstract String fragmentTag();

    protected abstract PreferenceFragmentCompat newFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag());
            if (fragment == null) {
                fragment = newFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, fragment, fragmentTag())
                    .commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(final PreferenceFragmentCompat caller, final PreferenceScreen preferenceScreen) {
        final PreferenceFragmentCompat fragment = newFragment();
        final Bundle args;
        if (fragment.getArguments() != null) {
            args = fragment.getArguments();
        } else {
            args = new Bundle();
        }
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, fragment, preferenceScreen.getKey())
                .addToBackStack(preferenceScreen.getKey())
                .commit();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Simulate a back press, so that we don't actually exit the activity when
                // in a nested PreferenceScreen
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(final CharSequence title) {
        final ActionBar actionBar = getSupportActionBar();
        findViewById(R.id.action_bar_back_img).
                setOnClickListener(v -> {
                    getOnBackPressedDispatcher().onBackPressed();
                });
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
