
package com.example.gr.device.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.example.gr.R;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractPreferenceFragment extends PreferenceFragmentCompat {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPreferenceFragment.class);

    private final SharedPreferencesChangeHandler sharedPreferencesChangeHandler = new SharedPreferencesChangeHandler();

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();

        reloadPreferences(sharedPreferences, getPreferenceScreen());

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesChangeHandler);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateActionBarTitle();
    }

    private void updateActionBarTitle() {
        try {
            CharSequence title = getPreferenceScreen().getTitle();
            if (StringUtils.isBlank(title)) {
                title = requireActivity().getTitle();
            }
            ((AbstractSettingsActivityV2) requireActivity()).setActionBarTitle(title);
        } catch (final Exception e) {
            LOG.error("Failed to update action bar title", e);
        }
    }

    @Override
    public void onStop() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(sharedPreferencesChangeHandler);

        super.onStop();
    }

    @Override
    public void onDisplayPreferenceDialog(final Preference preference) {
//        DialogFragment dialogFragment;
//        if (preference instanceof XTimePreference) {
//            dialogFragment = new XTimePreferenceFragment();
//        } else if (preference instanceof DragSortListPreference) {
//            dialogFragment = new DragSortListPreferenceFragment();
//        } else if (preference instanceof EditTextPreference) {
//            dialogFragment = MaterialEditTextPreferenceDialogFragment.newInstance(preference.getKey());
//        } else if (preference instanceof ListPreference) {
//            dialogFragment = MaterialListPreferenceDialogFragment.newInstance(preference.getKey());
//        } else if (preference instanceof MultiSelectListPreference) {
//            dialogFragment = MaterialMultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
//        } else {
//            super.onDisplayPreferenceDialog(preference);
//            return;
//        }
//        final Bundle bundle = new Bundle(1);
//        bundle.putString("key", preference.getKey());
//        dialogFragment.setArguments(bundle);
//        dialogFragment.setTargetFragment(this, 0);
//        if (getFragmentManager() != null) {
//            dialogFragment.show(getFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
//        }
        super.onDisplayPreferenceDialog(preference);
        return;
    }

    /**
     * Keys of preferences which should print its values as a summary below the preference name.
     */
    protected Set<String> getPreferenceKeysWithSummary() {
        return Collections.emptySet();
    }

    protected void onSharedPreferenceChanged(final Preference preference) {
        // Nothing to do
    }

    public void setInputTypeFor(final String preferenceKey, final int editTypeFlags) {
        final EditTextPreference textPreference = findPreference(preferenceKey);
        if (textPreference != null) {
            textPreference.setOnBindEditTextListener(editText -> editText.setInputType(editTypeFlags));
        }
    }

    /**
     * Reload the preferences in the current screen. This is needed when the user enters or exists a PreferenceScreen,
     * otherwise the settings won't be reloaded by the {@link SharedPreferencesChangeHandler}, as the preferences return
     * null, since they're not visible.
     *
     * @param sharedPreferences the {@link SharedPreferences} instance
     * @param preferenceGroup   the {@link PreferenceGroup} for which preferences will be reloaded
     */
    private void reloadPreferences(final SharedPreferences sharedPreferences, final PreferenceGroup preferenceGroup) {
        if (preferenceGroup == null) {
            return;
        }

        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            final Preference preference = preferenceGroup.getPreference(i);

            LOG.debug("Reloading {}", preference.getKey());

            if (preference instanceof PreferenceCategory) {
                reloadPreferences(sharedPreferences, (PreferenceCategory) preference);
                continue;
            }

            sharedPreferencesChangeHandler.onSharedPreferenceChanged(sharedPreferences, preference.getKey());
        }
    }

    /**
     * Handler for preference changes, update UI accordingly (if device updates the preferences).
     */
    private class SharedPreferencesChangeHandler implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(final SharedPreferences prefs, final String key) {
            LOG.debug("Preference changed: {}", key);

            if (key == null) {
                LOG.warn("Preference null, ignoring");
                return;
            }

            final Preference preference = findPreference(key);
            if (preference == null) {
                LOG.warn("Preference {} not found", key);

                return;
            }

            if (preference instanceof SeekBarPreference) {
                final SeekBarPreference seekBarPreference = (SeekBarPreference) preference;
                seekBarPreference.setValue(prefs.getInt(key, seekBarPreference.getValue()));
            } else if (preference instanceof SwitchPreferenceCompat) {
                final SwitchPreferenceCompat switchPreference = (SwitchPreferenceCompat) preference;
                switchPreference.setChecked(prefs.getBoolean(key, switchPreference.isChecked()));
            } else if (preference instanceof ListPreference) {
                final ListPreference listPreference = (ListPreference) preference;
                listPreference.setValue(prefs.getString(key, listPreference.getValue()));
            } else if (preference instanceof EditTextPreference) {
                final EditTextPreference editTextPreference = (EditTextPreference) preference;
                editTextPreference.setText(prefs.getString(key, editTextPreference.getText()));
            } else if (preference instanceof PreferenceScreen) {
                // Ignoring
            } else {
                LOG.warn("Unknown preference class {} for {}, ignoring", preference.getClass(), key);
            }

            if (getPreferenceKeysWithSummary().contains(key)) {
                final String summary;

                // For multi select preferences, let's set the summary to the values, comma-delimited
                if (preference instanceof MultiSelectListPreference) {
                    final Set<String> prefSetValue = prefs.getStringSet(key, Collections.emptySet());
                    if (prefSetValue.isEmpty()) {
                        summary = requireContext().getString(R.string.not_set);
                    } else {
                        final MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
                        final CharSequence[] entries = multiSelectListPreference.getEntries();
                        final CharSequence[] entryValues = multiSelectListPreference.getEntryValues();
                        final List<String> translatedEntries = new ArrayList<>();
                        for (int i = 0; i < entryValues.length; i++) {
                            if (prefSetValue.contains(entryValues[i].toString())) {
                                translatedEntries.add(entries[i].toString());
                            }
                        }
                        summary = TextUtils.join(", ", translatedEntries);
                    }
                } else {
                    summary = prefs.getString(key, preference.getSummary() != null ? preference.getSummary().toString() : "");
                }

                preference.setSummary(summary);
            }

            AbstractPreferenceFragment.this.onSharedPreferenceChanged(preference);
        }
    }
}