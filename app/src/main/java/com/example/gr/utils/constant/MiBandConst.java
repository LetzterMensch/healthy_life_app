/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, Christian
    Fischer, Damien Gaignon, Daniele Gobbetti, José Rebelo, Michal Novotny,
    Szymon Tomasz Stefanek

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
package com.example.gr.utils.constant;


import com.example.gr.utils.Prefs;
import com.example.gr.utils.Version;

public final class MiBandConst {
    public static final String PREF_MIBAND_ADDRESS = "development_miaddr";  // FIXME: should be prefixed mi_
    public static final String PREF_MIBAND_ALARMS = "mi_alarms";
    public static final String PREF_MIBAND_DONT_ACK_TRANSFER = "mi_dont_ack_transfer";
    public static final String PREF_MIBAND_DEVICE_TIME_OFFSET_HOURS = "device_time_offset_hours";
	public static final String PREF_MI2_DATEFORMAT = "mi2_dateformat";
	public static final String PREF_MI2_DISPLAY_ITEM_CLOCK = "clock";
	public static final String PREF_MI2_DISPLAY_ITEM_STEPS = "steps";
	public static final String PREF_MI2_DISPLAY_ITEM_DISTANCE = "distance";
	public static final String PREF_MI2_DISPLAY_ITEM_CALORIES = "calories";
	public static final String PREF_MI2_DISPLAY_ITEM_HEART_RATE = "heart_rate";
	public static final String PREF_MI2_DISPLAY_ITEM_BATTERY = "battery";
    public static final String PREF_MI2_ROTATE_WRIST_TO_SWITCH_INFO = "rotate_wrist_to_cycle_info";
	public static final String PREF_MI2_ENABLE_TEXT_NOTIFICATIONS = "mi2_enable_text_notifications";
    public static final String PREF_MIBAND_SETUP_BT_PAIRING = "mi_setup_bt_pairing";

    public static final String PREF_SWIPE_UNLOCK = "swipe_unlock";
    public static final String PREF_NIGHT_MODE = "night_mode";
    public static final String PREF_NIGHT_MODE_START = "night_mode_start";
    public static final String PREF_NIGHT_MODE_END = "night_mode_end";
    public static final String PREF_NIGHT_MODE_OFF = "off";
    public static final String PREF_NIGHT_MODE_SUNSET = "sunset";
    public static final String PREF_NIGHT_MODE_SCHEDULED = "scheduled";

    public static final String ORIGIN_INCOMING_CALL = "incoming_call";
    public static final String ORIGIN_ALARM_CLOCK = "alarm_clock";
    public static final String MI_GENERAL_NAME_PREFIX = "MI";
    public static final String MI_1 = "1";
    public static final String MI_1A = "1A";
    public static final String MI_1S = "1S";
    public static final String MI_AMAZFIT = "Amazfit";
    public static final String MI_PRO = "2";

    public static final Version MI2_FW_VERSION_MIN_TEXT_NOTIFICATIONS = new Version("1.0.1.28");
    public static final Version MI2_FW_VERSION_INTERMEDIATE_UPGRADE_53 = new Version("1.0.0.53");

    public enum DistanceUnit {
        METRIC,
        IMPERIAL
    }

    public static int getNotificationPrefIntValue(String pref, String origin, Prefs prefs, int defaultValue) {
        String key = getNotificationPrefKey(pref, origin);
        return prefs.getInt(key, defaultValue);
    }

    public static String getNotificationPrefStringValue(String pref, String origin, Prefs prefs, String defaultValue) {
        String key = getNotificationPrefKey(pref, origin);
        return prefs.getString(key, defaultValue);
    }

    public static String getNotificationPrefKey(String pref, String origin) {
        return pref + '_' + origin;
    }

    public static final String VIBRATION_PROFILE = "mi_vibration_profile";
    public static final String VIBRATION_COUNT = "mi_vibration_count";
    public static final String VIBRATION_DURATION = "mi_vibration_duration";
    public static final String VIBRATION_PAUSE = "mi_vibration_pause";
    public static final String FLASH_COUNT = "mi_flash_count";
    public static final String FLASH_DURATION = "mi_flash_duration";
    public static final String FLASH_PAUSE = "mi_flash_pause";
    public static final String FLASH_COLOUR = "mi_flash_colour";
    public static final String FLASH_ORIGINAL_COLOUR = "mi_flash_original_colour";

    public static final String DEFAULT_VALUE_VIBRATION_PROFILE = "short";
    public static final int DEFAULT_VALUE_VIBRATION_COUNT = 3;
    public static final int DEFAULT_VALUE_VIBRATION_DURATION = 500; // ms
    public static final int DEFAULT_VALUE_VIBRATION_PAUSE = 500; // ms
    public static final int DEFAULT_VALUE_FLASH_COUNT = 10; // ms
    public static final int DEFAULT_VALUE_FLASH_DURATION = 500; // ms
    public static final int DEFAULT_VALUE_FLASH_PAUSE = 500; // ms
    public static final int DEFAULT_VALUE_FLASH_COLOUR = 1; // TODO: colour!
    public static final int DEFAULT_VALUE_FLASH_ORIGINAL_COLOUR = 1; // TODO: colour!
}
