/*  Copyright (C) 2020-2024 Andreas Shimokawa, José Rebelo, Petr Vaněk

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
package com.example.gr.device.settings;

import static com.example.gr.model.ActivityUser.PREF_USER_ACTIVETIME_MINUTES;
import static com.example.gr.model.ActivityUser.PREF_USER_CALORIES_BURNT;
import static com.example.gr.model.ActivityUser.PREF_USER_DISTANCE_METERS;
import static com.example.gr.model.ActivityUser.PREF_USER_GENDER;
import static com.example.gr.model.ActivityUser.PREF_USER_GOAL_FAT_BURN_TIME_MINUTES;
import static com.example.gr.model.ActivityUser.PREF_USER_GOAL_STANDING_TIME_HOURS;
import static com.example.gr.model.ActivityUser.PREF_USER_GOAL_WEIGHT_KG;
import static com.example.gr.model.ActivityUser.PREF_USER_HEIGHT_CM;
import static com.example.gr.model.ActivityUser.PREF_USER_NAME;
import static com.example.gr.model.ActivityUser.PREF_USER_SLEEP_DURATION;
import static com.example.gr.model.ActivityUser.PREF_USER_STEPS_GOAL;
import static com.example.gr.model.ActivityUser.PREF_USER_STEP_LENGTH_CM;
import static com.example.gr.model.ActivityUser.PREF_USER_WEIGHT_KG;
import static com.example.gr.model.ActivityUser.PREF_USER_YEAR_OF_BIRTH;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.device.DeviceManager;

public class AboutUserPreferencesActivity extends AbstractSettingsActivityV2 {
    @Override
    protected String fragmentTag() {
        return AboutUserPreferencesFragment.FRAGMENT_TAG;
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected PreferenceFragmentCompat newFragment() {
        return new AboutUserPreferencesFragment();
    }

    public static class AboutUserPreferencesFragment extends AbstractPreferenceFragment {
        static final String FRAGMENT_TAG = "ABOUT_USER_PREFERENCES_FRAGMENT";

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            setPreferencesFromResource(R.xml.about_user, rootKey);

            addPreferenceHandlerFor(PREF_USER_NAME, true, false);
            addPreferenceHandlerFor(PREF_USER_YEAR_OF_BIRTH, true, false);
            addPreferenceHandlerFor(PREF_USER_HEIGHT_CM, true, true);
            addPreferenceHandlerFor(PREF_USER_WEIGHT_KG, true, false);
            addPreferenceHandlerFor(PREF_USER_GENDER, true, false);
            addPreferenceHandlerFor(PREF_USER_STEPS_GOAL, true, true);
            addPreferenceHandlerFor(PREF_USER_GOAL_WEIGHT_KG, true, true);
            addPreferenceHandlerFor(PREF_USER_GOAL_STANDING_TIME_HOURS, true, true);
            addPreferenceHandlerFor(PREF_USER_GOAL_FAT_BURN_TIME_MINUTES, true, true);

            addPreferenceHandlerFor(PREF_USER_SLEEP_DURATION, false, true);
            addPreferenceHandlerFor(PREF_USER_STEP_LENGTH_CM, false, true);
            addPreferenceHandlerFor(PREF_USER_DISTANCE_METERS, false, true);

            setInputTypeFor(PREF_USER_YEAR_OF_BIRTH, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_HEIGHT_CM, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_WEIGHT_KG, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_STEPS_GOAL, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_GOAL_WEIGHT_KG, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_GOAL_STANDING_TIME_HOURS, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_GOAL_FAT_BURN_TIME_MINUTES, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_SLEEP_DURATION, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_CALORIES_BURNT, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_ACTIVETIME_MINUTES, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_STEP_LENGTH_CM, InputType.TYPE_CLASS_NUMBER);
            setInputTypeFor(PREF_USER_DISTANCE_METERS, InputType.TYPE_CLASS_NUMBER);
        }

        /**
         * @param prefKey           the pref key that chagned
         * @param sendToDevice      notify all device support classes of the preference change
         * @param refreshDeviceList Ensure that the Control center is re-rendered when user preferences change
         */
        private void addPreferenceHandlerFor(final String prefKey,
                                             final boolean sendToDevice,
                                             final boolean refreshDeviceList) {
            final Preference pref = findPreference(prefKey);
            if (pref == null) {
                LOG.warn("Could not find preference {}", prefKey);
                return;
            }

            pref.setOnPreferenceChangeListener((preference, newVal) -> {
                if (sendToDevice) {
                    ControllerApplication.deviceService().onSendConfiguration(prefKey);
                }
                if (refreshDeviceList) {
                    final Intent refreshIntent = new Intent(DeviceManager.ACTION_REFRESH_DEVICELIST);
                    LocalBroadcastManager.getInstance(requireActivity().getApplicationContext()).sendBroadcast(refreshIntent);
                    return true;
                }
                return true;
            });
        }
    }
}
