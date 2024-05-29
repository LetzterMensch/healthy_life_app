
package com.example.gr.device.settings;


import androidx.preference.PreferenceFragmentCompat;

import com.example.gr.device.GBDevice;


public class DeviceSettingsActivity extends AbstractSettingsActivityV2 {
    public static final String MENU_ENTRY_POINT = "MENU_ENTRY_POINT";

    public enum MENU_ENTRY_POINTS {
        DEVICE_SETTINGS,
        AUTH_SETTINGS,
        APPLICATION_SETTINGS
    }

    @Override
    protected String fragmentTag() {
        return DeviceSpecificSettingsFragment.FRAGMENT_TAG;
    }

    @Override
    protected PreferenceFragmentCompat newFragment() {
        final GBDevice device = getIntent().getParcelableExtra(GBDevice.EXTRA_DEVICE);
        final MENU_ENTRY_POINTS menu_entry = (MENU_ENTRY_POINTS) getIntent().getSerializableExtra(MENU_ENTRY_POINT);

        return DeviceSpecificSettingsFragment.newInstance(device, menu_entry);
    }
}
