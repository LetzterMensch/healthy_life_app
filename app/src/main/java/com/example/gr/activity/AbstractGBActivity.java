/*  Copyright (C) 2017-2024 Andreas Shimokawa, Arjan Schrijver, Carsten
    Pfeiffer, Daniele Gobbetti, Petr Vaněk

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
package com.example.gr.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.controls.Control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.ControllerApplication;
import com.example.gr.device.settings.GBActivity;

import java.util.Locale;



public abstract class AbstractGBActivity extends AppCompatActivity implements GBActivity {
    private boolean isLanguageInvalid = false;

    public static final int NONE = 0;
    public static final int NO_ACTIONBAR = 1;

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action == null) {
//                return;
//            }
//            switch (action) {
//                case ControllerApplication.ACTION_LANGUAGE_CHANGE:
//                    setLanguage(ControllerApplication.getLanguage(), true);
//                    break;
//                case ControllerApplication.ACTION_THEME_CHANGE:
//                    recreate();
//                    break;
//                case ControllerApplication.ACTION_QUIT:
//                    finish();
//                    break;
//            }
//        }
//    };

//    public void setLanguage(Locale language, boolean invalidateLanguage) {
//        if (invalidateLanguage) {
//            isLanguageInvalid = true;
//        }
//        AndroidUtils.setLanguage(this, language);
//    }
//
//    public static void init(GBActivity activity) {
//        init(activity, NONE);
//    }

//    public static void init(GBActivity activity, int flags) {
//        //set action bar and themes
//        if (ControllerApplication.areDynamicColorsEnabled()) {
//            if (ControllerApplication.isDarkThemeEnabled()) {
//                if ((flags & NO_ACTIONBAR) != 0) {
//                    if (ControllerApplication.isAmoledBlackEnabled())
//                        activity.setTheme(R.style.GadgetbridgeThemeDynamicDarkAmoled_NoActionBar);
//                    else
//                        activity.setTheme(R.style.GadgetbridgeThemeDynamicDark_NoActionBar);
//                } else {
//                    if (ControllerApplication.isAmoledBlackEnabled())
//                        activity.setTheme(R.style.GadgetbridgeThemeDynamicDarkAmoled);
//                    else
//                        activity.setTheme(R.style.GadgetbridgeThemeDynamicDark);
//                }
//            } else {
//                if ((flags & NO_ACTIONBAR) != 0) {
//                    activity.setTheme(R.style.GadgetbridgeThemeDynamicLight_NoActionBar);
//                } else {
//                    activity.setTheme(R.style.GadgetbridgeThemeDynamicLight);
//                }
//            }
//        } else if (ControllerApplication.isDarkThemeEnabled()) {
//            if ((flags & NO_ACTIONBAR) != 0) {
//                if (ControllerApplication.isAmoledBlackEnabled())
//                    activity.setTheme(R.style.GadgetbridgeThemeBlack_NoActionBar);
//                else
//                    activity.setTheme(R.style.GadgetbridgeThemeDark_NoActionBar);
//            } else {
//                if (ControllerApplication.isAmoledBlackEnabled())
//                    activity.setTheme(R.style.GadgetbridgeThemeBlack);
//                else
//                    activity.setTheme(R.style.GadgetbridgeThemeDark);
//            }
//        } else {
//            if ((flags & NO_ACTIONBAR) != 0) {
//                activity.setTheme(R.style.GadgetbridgeTheme_NoActionBar);
//            } else {
//                activity.setTheme(R.style.GadgetbridgeTheme);
//            }
//        }
//        activity.setLanguage(ControllerApplication.getLanguage(), false);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        IntentFilter filterLocal = new IntentFilter();
//        filterLocal.addAction(ControllerApplication.ACTION_QUIT);
//        filterLocal.addAction(ControllerApplication.ACTION_LANGUAGE_CHANGE);
//        filterLocal.addAction(ControllerApplication.ACTION_THEME_CHANGE);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filterLocal);

//        init(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLanguageInvalid) {
            isLanguageInvalid = false;
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
