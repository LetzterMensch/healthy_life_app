package com.example.gr;

import static com.example.gr.device.model.DeviceType.MIBAND;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.database.DBHandler;
import com.example.gr.database.DBHelper;
import com.example.gr.database.DBOpenHelper;
import com.example.gr.database.entities.DaoMaster;
import com.example.gr.database.entities.DaoSession;
import com.example.gr.database.entities.Device;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDeviceService;
import com.example.gr.device.model.DeviceService;
import com.example.gr.device.GBDevice;
import com.example.gr.device.GBException;
import com.example.gr.device.model.DeviceType;
import com.example.gr.device.model.Weather;
import com.example.gr.externalevents.BluetoothStateChangeReceiver;
import com.example.gr.externalevents.opentracks.OpenTracksContentObserver;
import com.example.gr.handler.LockHandler;
import com.example.gr.model.ActivityUser;
import com.example.gr.utils.GB;
import com.example.gr.utils.GBPrefs;
import com.example.gr.utils.LimitedQueue;
import com.example.gr.utils.Prefs;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ControllerApplication extends Application {
    private static DeviceService deviceService;
    private OpenTracksContentObserver openTracksObserver;
    private static final String TAG = "Application";
    public static final String DATABASE_NAME = "GR";
    private static SharedPreferences sharedPrefs;
    private static Prefs prefs;
    private static GBPrefs gbPrefs;
    private static ControllerApplication app;
    private DeviceManager deviceManager;
    private static final LimitedQueue<Integer, String> mIDSenderLookup = new LimitedQueue<>(16);

    private static ControllerApplication context;
    private static final Lock dbLock = new ReentrantLock();
    private static LockHandler lockHandler;
    private static final String PREFS_VERSION = "shared_preferences_version";
    private static final int CURRENT_PREFS_VERSION = 28;
    public static final String ACTION_NAV_EXERCISE_FRAGMENT = "om.example.gr.ControllerApplication.action.exercisefragment";
    public static final String ACTION_NAV_DIARY_FRAGMENT = "om.example.gr.ControllerApplication.action.diaryfragment";
    public static final String ACTION_NAV_DASHBOARD_FRAGMENT = "om.example.gr.ControllerApplication.action.dashboardfragment";
    public static final String ACTION_NAV_SLEEP_FRAGMENT = "om.example.gr.ControllerApplication.action.sleepfragment";
    public static final String ACTION_NAV_PROFILE_FRAGMENT = "om.example.gr.ControllerApplication.action.profilefragment";

    public static final String ACTION_NEW_DATA = "com.example.gr.ControllerApplication.action.new_data";

    public static final String ACTION_QUIT = "com.example.gr.ControllerApplication.action.quit";
    private BluetoothStateChangeReceiver bluetoothStateChangeReceiver;
    @Override
    public void onCreate() {
        app = this;
        super.onCreate();

        if (lockHandler != null) {
            // guard against multiple invocations (robolectric)
            return;
        }

        // Initialize the timezones library
        AndroidThreeTen.init(this);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs = new Prefs(sharedPrefs);
        gbPrefs = new GBPrefs(prefs);

        setupDatabase();

        // don't do anything here before we set up logging, otherwise
        // slf4j may be implicitly initialized before we properly configured it.
//        setupLogging(isFileLoggingEnabled());

        if (getPrefsFileVersion() != CURRENT_PREFS_VERSION) {
            migratePrefs(getPrefsFileVersion());
        }

        // Uncomment the line below to force a device key migration, after you updated
        // the devicetype.json file
        migrateDeviceTypes();


        Weather.getInstance().setCacheFile(getCacheDir(), prefs.getBoolean("cache_weather", true));

        deviceManager = new DeviceManager(this);


        deviceService = createDeviceService();

        //This will export the database
//        PeriodicExporter.enablePeriodicExport(context);

        if (isRunningMarshmallowOrLater()) {
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (isRunningOreoOrLater()) {
                bluetoothStateChangeReceiver = new BluetoothStateChangeReceiver();
                registerReceiver(bluetoothStateChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            }
//            try {
//                //the following will ensure the notification manager is kept alive
//                startService(new Intent(this, NotificationCollectorMonitorService.class));
//            } catch (IllegalStateException e) {
//                String message = e.toString();
//                if (message == null) {
//                    message = getString(R.string._unknown_);
//                }
//                GB.notify(NOTIFICATION_ID_ERROR,
//                        new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_HIGH_PRIORITY_ID)
//                                .setSmallIcon(R.drawable.gadgetbridge_img)
//                                .setContentTitle(getString(R.string.error_background_service))
//                                .setContentText(getString(R.string.error_background_service_reason_truncated))
//                                .setStyle(new NotificationCompat.BigTextStyle()
//                                        .bigText(getString(R.string.error_background_service_reason) + "\"" + message + "\""))
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .build(), context);
//            }
        }
    }
    public static boolean isRunningTwelveOrLater() {
        return Build.VERSION.SDK_INT >= 31;  // Build.VERSION_CODES.S, but our target SDK is lower
    }
    private void migratePrefs(int oldVersion) {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // this comes before all other migrations since the new column DeviceTypeName was added as non-null
        if (oldVersion < 25){
            migrateDeviceTypes();
        }

        if (oldVersion == 0) {
            String legacyGender = sharedPrefs.getString("mi_user_gender", null);
            String legacyHeight = sharedPrefs.getString("mi_user_height_cm", null);
            String legacyWeight = sharedPrefs.getString("mi_user_weight_kg", null);
            String legacyYOB = sharedPrefs.getString("mi_user_year_of_birth", null);
            if (legacyGender != null) {
                int gender = "male".equals(legacyGender) ? 1 : "female".equals(legacyGender) ? 0 : 2;
                editor.putString(ActivityUser.PREF_USER_GENDER, Integer.toString(gender));
                editor.remove("mi_user_gender");
            }
            if (legacyHeight != null) {
                editor.putString(ActivityUser.PREF_USER_HEIGHT_CM, legacyHeight);
                editor.remove("mi_user_height_cm");
            }
            if (legacyWeight != null) {
                editor.putString(ActivityUser.PREF_USER_WEIGHT_KG, legacyWeight);
                editor.remove("mi_user_weight_kg");
            }
            if (legacyYOB != null) {
                editor.putString(ActivityUser.PREF_USER_YEAR_OF_BIRTH, legacyYOB);
                editor.remove("mi_user_year_of_birth");
            }
        }
        if (oldVersion < 2) {
            //migrate the integer version of gender introduced in version 1 to a string value, needed for the way Android accesses the shared preferences
            int legacyGender_1 = 2;
            try {
                legacyGender_1 = sharedPrefs.getInt(ActivityUser.PREF_USER_GENDER, 2);
            } catch (Exception e) {
                Log.e(TAG, "Could not access legacy activity gender", e);
            }
            editor.putString(ActivityUser.PREF_USER_GENDER, Integer.toString(legacyGender_1));
        }
        if (oldVersion < 3) {
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                for (Device dbDevice : activeDevices) {
                    SharedPreferences deviceSpecificSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    if (deviceSpecificSharedPrefs != null) {
                        SharedPreferences.Editor deviceSharedPrefsEdit = deviceSpecificSharedPrefs.edit();
                        String preferenceKey = dbDevice.getIdentifier() + "_lastSportsActivityTimeMillis";
                        long lastSportsActivityTimeMillis = sharedPrefs.getLong(preferenceKey, 0);
                        if (lastSportsActivityTimeMillis != 0) {
                            deviceSharedPrefsEdit.putLong("lastSportsActivityTimeMillis", lastSportsActivityTimeMillis);
                            editor.remove(preferenceKey);
                        }
                        preferenceKey = dbDevice.getIdentifier() + "_lastSyncTimeMillis";
                        long lastSyncTimeMillis = sharedPrefs.getLong(preferenceKey, 0);
                        if (lastSyncTimeMillis != 0) {
                            deviceSharedPrefsEdit.putLong("lastSyncTimeMillis", lastSyncTimeMillis);
                            editor.remove(preferenceKey);
                        }

                        String newLanguage = null;
                        Set<String> displayItems = null;

                        DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

                        if (dbDevice.getManufacturer().equals("Huami")) {
                            deviceSharedPrefsEdit.putString("activate_display_on_lift_wrist", prefs.getString("activate_display_on_lift_wrist", "off"));
                            deviceSharedPrefsEdit.putString("display_on_lift_start", prefs.getString("display_on_lift_start", "0:00"));
                            deviceSharedPrefsEdit.putString("display_on_lift_end", prefs.getString("display_on_lift_end", "0:00"));
                        }
                        switch (deviceType) {
                            case MIBAND:
                                deviceSharedPrefsEdit.putBoolean("low_latency_fw_update", prefs.getBoolean("mi_low_latency_fw_update", true));
                                deviceSharedPrefsEdit.putString("device_time_offset_hours", String.valueOf(prefs.getInt("mi_device_time_offset_hours", 0)));
                                break;
//                            case AMAZFITCOR:
//                                displayItems = prefs.getStringSet("cor_display_items", null);
//                                break;
//                            case AMAZFITBIP:
//                                displayItems = prefs.getStringSet("bip_display_items", null);
//                                break;
//                            case MIBAND2:
//                            case MIBAND2_HRX:
//                                displayItems = prefs.getStringSet("mi2_display_items", null);
//                                deviceSharedPrefsEdit.putBoolean("mi2_enable_text_notifications", prefs.getBoolean("mi2_enable_text_notifications", true));
//                                deviceSharedPrefsEdit.putString("mi2_dateformat", prefs.getString("mi2_dateformat", "dateformat_time"));
//                                deviceSharedPrefsEdit.putBoolean("rotate_wrist_to_cycle_info", prefs.getBoolean("mi2_rotate_wrist_to_switch_info", false));
//                                break;
//                            case MIBAND3:
//                                newLanguage = prefs.getString("miband3_language", "auto");
//                                displayItems = prefs.getStringSet("miband3_display_items", null);
//                                deviceSharedPrefsEdit.putBoolean("swipe_unlock", prefs.getBoolean("mi3_band_screen_unlock", false));
//                                deviceSharedPrefsEdit.putString("night_mode", prefs.getString("mi3_night_mode", "off"));
//                                deviceSharedPrefsEdit.putString("night_mode_start", prefs.getString("mi3_night_mode_start", "16:00"));
//                                deviceSharedPrefsEdit.putString("night_mode_end", prefs.getString("mi3_night_mode_end", "7:00"));

                        }
                        if (displayItems != null) {
                            deviceSharedPrefsEdit.putStringSet("display_items", displayItems);
                        }
                        if (newLanguage != null) {
                            deviceSharedPrefsEdit.putString("language", newLanguage);
                        }
                        deviceSharedPrefsEdit.apply();
                    }
                }
                editor.remove("amazfitbip_language");
                editor.remove("bip_display_items");
                editor.remove("cor_display_items");
                editor.remove("disconnect_notification");
                editor.remove("disconnect_notification_start");
                editor.remove("disconnect_notification_end");
                editor.remove("activate_display_on_lift_wrist");
                editor.remove("display_on_lift_start");
                editor.remove("display_on_lift_end");

                editor.remove("mi_low_latency_fw_update");
                editor.remove("mi_device_time_offset_hours");
                editor.remove("mi2_do_not_disturb");
                editor.remove("mi2_do_not_disturb_start");
                editor.remove("mi2_do_not_disturb_end");
                editor.remove("mi2_dateformat");
                editor.remove("mi2_display_items");
                editor.remove("mi2_rotate_wrist_to_switch_info");
                editor.remove("mi2_enable_text_notifications");
                editor.remove("mi3_band_screen_unlock");
                editor.remove("mi3_night_mode");
                editor.remove("mi3_night_mode_start");
                editor.remove("mi3_night_mode_end");
                editor.remove("miband3_language");

            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }
        if (oldVersion < 4) {
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                for (Device dbDevice : activeDevices) {
                    SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

                    if (deviceType == MIBAND) {
                        int deviceTimeOffsetHours = deviceSharedPrefs.getInt("device_time_offset_hours",0);
                        deviceSharedPrefsEdit.putString("device_time_offset_hours", Integer.toString(deviceTimeOffsetHours) );
                    }

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }
        if (oldVersion < 5) {
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                for (Device dbDevice : activeDevices) {
                    SharedPreferences deviceSpecificSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    if (deviceSpecificSharedPrefs != null) {
                        SharedPreferences.Editor deviceSharedPrefsEdit = deviceSpecificSharedPrefs.edit();
                        DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

                        String newWearside = null;
                        String newOrientation = null;
                        String newTimeformat = null;
//                        switch (deviceType) {
//                            case AMAZFITBIP:
//                            case AMAZFITCOR:
//                            case AMAZFITCOR2:
//                            case MIBAND:
//                            case MIBAND2:
//                            case MIBAND2_HRX:
//                            case MIBAND3:
//                            case MIBAND4:
//                                newWearside = prefs.getString("mi_wearside", "left");
//                                break;
//                            case MIBAND5:
//                                newWearside = prefs.getString("mi_wearside", "left");
//                                break;
//                            case HPLUS:
//                                newWearside = prefs.getString("hplus_wrist", "left");
//                                newTimeformat = prefs.getString("hplus_timeformat", "24h");
//                                break;
//                            case ID115:
//                                newWearside = prefs.getString("id115_wrist", "left");
//                                newOrientation = prefs.getString("id115_screen_orientation", "horizontal");
//                                break;
//                            case ZETIME:
//                                newWearside = prefs.getString("zetime_wrist", "left");
//                                newTimeformat = prefs.getInt("zetime_timeformat", 1) == 2 ? "am/pm" : "24h";
//                                break;
//                        }
                        if (newWearside != null) {
                            deviceSharedPrefsEdit.putString("wearlocation", newWearside);
                        }
                        if (newOrientation != null) {
                            deviceSharedPrefsEdit.putString("screen_orientation", newOrientation);
                        }
                        if (newTimeformat != null) {
                            deviceSharedPrefsEdit.putString("timeformat", newTimeformat);
                        }
                        deviceSharedPrefsEdit.apply();
                    }
                }
                editor.remove("hplus_timeformat");
                editor.remove("hplus_wrist");
                editor.remove("id115_wrist");
                editor.remove("id115_screen_orientation");
                editor.remove("mi_wearside");
                editor.remove("zetime_timeformat");
                editor.remove("zetime_wrist");

            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }
        if (oldVersion < 6) {
//            migrateBooleanPrefToPerDevicePref("mi2_enable_button_action", false, "button_action_enable", new ArrayList<>(Collections.singletonList(MIBAND2)));
//            migrateBooleanPrefToPerDevicePref("mi2_button_action_vibrate", false, "button_action_vibrate", new ArrayList<>(Collections.singletonList(MIBAND2)));
//            migrateStringPrefToPerDevicePref("mi_button_press_count", "6", "button_action_press_count", new ArrayList<>(Collections.singletonList(MIBAND2)));
//            migrateStringPrefToPerDevicePref("mi_button_press_count_max_delay", "2000", "button_action_press_max_interval", new ArrayList<>(Collections.singletonList(MIBAND2)));
//            migrateStringPrefToPerDevicePref("mi_button_press_count_match_delay", "0", "button_action_broadcast_delay", new ArrayList<>(Collections.singletonList(MIBAND2)));
//            migrateStringPrefToPerDevicePref("mi_button_press_broadcast", "nodomain.freeyourgadget.gadgetbridge.ButtonPressed", "button_action_broadcast", new ArrayList<>(Collections.singletonList(MIBAND2)));
        }
        if (oldVersion < 7) {
//            migrateStringPrefToPerDevicePref("mi_reserve_alarm_calendar", "0", "reserve_alarms_calendar", new ArrayList<>(Arrays.asList(MIBAND, MIBAND2)));
        }

        if (oldVersion < 8) {
            for (int i = 1; i <= 16; i++) {
                String message = prefs.getString("canned_message_dismisscall_" + i, null);
                if (message != null) {
//                    migrateStringPrefToPerDevicePref("canned_message_dismisscall_" + i, "", "canned_message_dismisscall_" + i, new ArrayList<>(Collections.singletonList(PEBBLE)));
                }
            }
            for (int i = 1; i <= 16; i++) {
                String message = prefs.getString("canned_reply_" + i, null);
                if (message != null) {
//                    migrateStringPrefToPerDevicePref("canned_reply_" + i, "", "canned_reply_" + i, new ArrayList<>(Collections.singletonList(PEBBLE)));
                }
            }
        }
        if (oldVersion < 9) {
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                migrateBooleanPrefToPerDevicePref("transliteration", false, "pref_transliteration_enabled", (ArrayList)activeDevices);
                Log.w(TAG, "migrating transliteration settings");
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock and migrating prefs");
            }
        }
        if (oldVersion < 10) {
            //migrate the string version of pref_galaxy_buds_ambient_volume to int due to transition to SeekBarPreference
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                for (Device dbDevice : activeDevices) {
                    SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

//                    if (deviceType == GALAXY_BUDS) {
//                        GB.log("migrating Galaxy Buds volume", GB.INFO, null);
//                        String volume = deviceSharedPrefs.getString(DeviceSettingsPreferenceConst.PREF_GALAXY_BUDS_AMBIENT_VOLUME, "1");
//                        deviceSharedPrefsEdit.putInt(DeviceSettingsPreferenceConst.PREF_GALAXY_BUDS_AMBIENT_VOLUME, Integer.parseInt(volume));
//                    }
                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }
        if (oldVersion < 11) {
            try (DBHandler db = acquireDB()) {
                DaoSession daoSession = db.getDaoSession();
                List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
                for (Device dbDevice : activeDevices) {
                    SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());
//                    if (deviceType == WATCHXPLUS || deviceType == FITPRO || deviceType == LEFUN) {
//                        deviceSharedPrefsEdit.putBoolean("inactivity_warnings_enable", deviceSharedPrefs.getBoolean("pref_longsit_switch", false));
//                        deviceSharedPrefsEdit.remove("pref_longsit_switch");
//                    }
//                    if (deviceType == WATCHXPLUS || deviceType == FITPRO) {
//                        deviceSharedPrefsEdit.putString("inactivity_warnings_start", deviceSharedPrefs.getString("pref_longsit_start", "06:00"));
//                        deviceSharedPrefsEdit.putString("inactivity_warnings_end", deviceSharedPrefs.getString("pref_longsit_end", "23:00"));
//                        deviceSharedPrefsEdit.remove("pref_longsit_start");
//                        deviceSharedPrefsEdit.remove("pref_longsit_end");
//                    }
//                    if (deviceType == WATCHXPLUS || deviceType == LEFUN) {
//                        deviceSharedPrefsEdit.putString("inactivity_warnings_threshold", deviceSharedPrefs.getString("pref_longsit_period", "60"));
//                        deviceSharedPrefsEdit.remove("pref_longsit_period");
//                    }
//                    if (deviceType == TLW64) {
//                        deviceSharedPrefsEdit.putBoolean("inactivity_warnings_enable_noshed", deviceSharedPrefs.getBoolean("screen_longsit_noshed", false));
//                        deviceSharedPrefsEdit.remove("screen_longsit_noshed");
//                    }
                    if (dbDevice.getManufacturer().equals("Huami")) {
                        editor.putBoolean("inactivity_warnings_dnd", prefs.getBoolean("mi2_inactivity_warnings_dnd", false));
                        editor.putString("inactivity_warnings_dnd_start", prefs.getString("mi2_inactivity_warnings_dnd_start", "12:00"));
                        editor.putString("inactivity_warnings_dnd_end", prefs.getString("mi2_inactivity_warnings_dnd_end", "14:00"));
                        editor.putBoolean("inactivity_warnings_enable", prefs.getBoolean("mi2_inactivity_warnings", false));
                        editor.putInt("inactivity_warnings_threshold", prefs.getInt("mi2_inactivity_warnings_threshold", 60));
                        editor.putString("inactivity_warnings_start", prefs.getString("mi2_inactivity_warnings_start", "06:00"));
                        editor.putString("inactivity_warnings_end", prefs.getString("mi2_inactivity_warnings_end", "22:00"));
                    }
//                    switch (deviceType) {
//                        case LEFUN:
//                            deviceSharedPrefsEdit.putString("language", deviceSharedPrefs.getString("pref_lefun_interface_language", "0"));
//                            deviceSharedPrefsEdit.remove("pref_lefun_interface_language");
//                            break;
//                        case FITPRO:
//                            deviceSharedPrefsEdit.putString("inactivity_warnings_threshold", deviceSharedPrefs.getString("pref_longsit_period", "4"));
//                            deviceSharedPrefsEdit.remove("pref_longsit_period");
//                            break;
//                        case ZETIME:
//                            editor.putString("do_not_disturb", prefs.getString("zetime_do_not_disturb", "off"));
//                            editor.putString("do_not_disturb_start", prefs.getString("zetime_do_not_disturb_start", "22:00"));
//                            editor.putString("do_not_disturb_end", prefs.getString("zetime_do_not_disturb_end", "07:00"));
//                            editor.putBoolean("inactivity_warnings_enable", prefs.getBoolean("zetime_inactivity_warnings", false));
//                            editor.putString("inactivity_warnings_start", prefs.getString("zetime_inactivity_warnings_start", "06:00"));
//                            editor.putString("inactivity_warnings_end", prefs.getString("zetime_inactivity_warnings_end", "22:00"));
//                            editor.putInt("inactivity_warnings_threshold", prefs.getInt("zetime_inactivity_warnings_threshold", 60));
//                            editor.putBoolean("inactivity_warnings_mo", prefs.getBoolean("zetime_prefs_inactivity_repetitions_mo", false));
//                            editor.putBoolean("inactivity_warnings_tu", prefs.getBoolean("zetime_prefs_inactivity_repetitions_tu", false));
//                            editor.putBoolean("inactivity_warnings_we", prefs.getBoolean("zetime_prefs_inactivity_repetitions_we", false));
//                            editor.putBoolean("inactivity_warnings_th", prefs.getBoolean("zetime_prefs_inactivity_repetitions_th", false));
//                            editor.putBoolean("inactivity_warnings_fr", prefs.getBoolean("zetime_prefs_inactivity_repetitions_fr", false));
//                            editor.putBoolean("inactivity_warnings_sa", prefs.getBoolean("zetime_prefs_inactivity_repetitions_sa", false));
//                            editor.putBoolean("inactivity_warnings_su", prefs.getBoolean("zetime_prefs_inactivity_repetitions_su", false));
//                            break;
//                    }
                    deviceSharedPrefsEdit.apply();
                }
                editor.putInt("fitness_goal", prefs.getInt("mi_fitness_goal", 8000));

                editor.remove("zetime_do_not_disturb");
                editor.remove("zetime_do_not_disturb_start");
                editor.remove("zetime_do_not_disturb_end");
                editor.remove("zetime_inactivity_warnings");
                editor.remove("zetime_inactivity_warnings_start");
                editor.remove("zetime_inactivity_warnings_end");
                editor.remove("zetime_inactivity_warnings_threshold");
                editor.remove("zetime_prefs_inactivity_repetitions_mo");
                editor.remove("zetime_prefs_inactivity_repetitions_tu");
                editor.remove("zetime_prefs_inactivity_repetitions_we");
                editor.remove("zetime_prefs_inactivity_repetitions_th");
                editor.remove("zetime_prefs_inactivity_repetitions_fr");
                editor.remove("zetime_prefs_inactivity_repetitions_sa");
                editor.remove("zetime_prefs_inactivity_repetitions_su");
                editor.remove("mi2_inactivity_warnings_dnd");
                editor.remove("mi2_inactivity_warnings_dnd_start");
                editor.remove("mi2_inactivity_warnings_dnd_end");
                editor.remove("mi2_inactivity_warnings");
                editor.remove("mi2_inactivity_warnings_threshold");
                editor.remove("mi2_inactivity_warnings_start");
                editor.remove("mi2_inactivity_warnings_end");
                editor.remove("mi_fitness_goal");
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }
        if (oldVersion < 12) {
            // Convert preferences that were wrongly migrated to int, since Android saves them as Strings internally
            editor.putString("inactivity_warnings_threshold", String.valueOf(prefs.getInt("inactivity_warnings_threshold", 60)));
            editor.putString("fitness_goal", String.valueOf(prefs.getInt("fitness_goal", 8000)));
        }

        if (oldVersion < 13) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    if (dbDevice.getManufacturer().equals("Huami")) {
                        deviceSharedPrefsEdit.putBoolean("inactivity_warnings_enable", prefs.getBoolean("inactivity_warnings_enable", false));
                        deviceSharedPrefsEdit.putString("inactivity_warnings_threshold", prefs.getString("inactivity_warnings_threshold", "60"));
                        deviceSharedPrefsEdit.putString("inactivity_warnings_start", prefs.getString("inactivity_warnings_start", "06:00"));
                        deviceSharedPrefsEdit.putString("inactivity_warnings_end", prefs.getString("inactivity_warnings_end", "22:00"));

                        deviceSharedPrefsEdit.putBoolean("inactivity_warnings_dnd", prefs.getBoolean("inactivity_warnings_dnd", false));
                        deviceSharedPrefsEdit.putString("inactivity_warnings_dnd_start", prefs.getString("inactivity_warnings_dnd_start", "12:00"));
                        deviceSharedPrefsEdit.putString("inactivity_warnings_dnd_end", prefs.getString("inactivity_warnings_dnd_end", "14:00"));

                        deviceSharedPrefsEdit.putBoolean("fitness_goal_notification", prefs.getBoolean("mi2_goal_notification", false));
                    }

                    // Not removing the first 4 preferences since they're still used by some devices (ZeTime)
                    editor.remove("inactivity_warnings_dnd");
                    editor.remove("inactivity_warnings_dnd_start");
                    editor.remove("inactivity_warnings_dnd_end");
                    editor.remove("mi2_goal_notification");

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 14) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    if (MIBAND.equals(dbDevice.getType()) || dbDevice.getManufacturer().equals("Huami")) {
                        deviceSharedPrefsEdit.putBoolean("heartrate_sleep_detection", prefs.getBoolean("mi_hr_sleep_detection", false));
                        deviceSharedPrefsEdit.putString("heartrate_measurement_interval", prefs.getString("heartrate_measurement_interval", "0"));
                    }

                    // Not removing heartrate_measurement_interval since it's still used by some devices (ZeTime)
                    editor.remove("mi_hr_sleep_detection");

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 15) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

//                    if (DeviceType.FITPRO.equals(dbDevice.getType())) {
//                        editor.remove("inactivity_warnings_threshold");
//                        deviceSharedPrefsEdit.apply();
//                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 16) {
            // If transliteration was enabled for a device, migrate it to the per-language setting
            final String defaultLanguagesIfEnabled = "extended_ascii,common_symbols,scandinavian,german,russian,hebrew,greek,ukranian,arabic,persian,latvian,lithuanian,polish,estonian,icelandic,czech,turkish,bengali,korean,hungarian";
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    if (deviceSharedPrefs.getBoolean("pref_transliteration_enabled", false)) {
                        deviceSharedPrefsEdit.putString("pref_transliteration_languages", defaultLanguagesIfEnabled);
                    }

                    deviceSharedPrefsEdit.remove("pref_transliteration_enabled");

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 17) {
            final HashSet<String> calendarBlacklist = (HashSet<String>) prefs.getStringSet(GBPrefs.CALENDAR_BLACKLIST, null);

            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    deviceSharedPrefsEdit.putBoolean("sync_calendar", prefs.getBoolean("enable_calendar_sync", true));

                    if (calendarBlacklist != null) {
                        Prefs.putStringSet(deviceSharedPrefsEdit, GBPrefs.CALENDAR_BLACKLIST, calendarBlacklist);
                    }

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }

            editor.remove(GBPrefs.CALENDAR_BLACKLIST);
        }

        if (oldVersion < 18) {
            // Migrate the default value for Huami find band vibration pattern
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    if (!dbDevice.getManufacturer().equals("Huami")) {
                        continue;
                    }

                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    deviceSharedPrefsEdit.putString("huami_vibration_profile_find_band", "long");
                    deviceSharedPrefsEdit.putString("huami_vibration_count_find_band", "1");

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 19) {
            //remove old ble scanning prefences, now unsupported
            editor.remove("disable_new_ble_scanning");
        }

        if (oldVersion < 20) {
            // Add the new stress tab to all devices
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (final Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());

                    final String chartsTabsValue = deviceSharedPrefs.getString("charts_tabs", null);
                    if (chartsTabsValue == null) {
                        continue;
                    }

                    final String newPrefValue;
                    if (!StringUtils.isBlank(chartsTabsValue)) {
                        newPrefValue = chartsTabsValue + ",stress";
                    } else {
                        newPrefValue = "stress";
                    }

                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    deviceSharedPrefsEdit.putString("charts_tabs", newPrefValue);
                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 21) {
            // Add the new PAI tab to all devices
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (final Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());

                    final String chartsTabsValue = deviceSharedPrefs.getString("charts_tabs", null);
                    if (chartsTabsValue == null) {
                        continue;
                    }

                    final String newPrefValue;
                    if (!StringUtils.isBlank(chartsTabsValue)) {
                        newPrefValue = chartsTabsValue + ",pai";
                    } else {
                        newPrefValue = "pai";
                    }

                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    deviceSharedPrefsEdit.putString("charts_tabs", newPrefValue);
                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 22) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (Device dbDevice : activeDevices) {
                    final DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());
//                    if (deviceType == MIBAND2) {
//                        final String name = dbDevice.getName();
//                        if ("Mi Band HRX".equalsIgnoreCase(name) || "Mi Band 2i".equalsIgnoreCase(name)) {
//                            dbDevice.setTypeName(DeviceType.MIBAND2_HRX.name());
//                            daoSession.getDeviceDao().update(dbDevice);
//                        }
//                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 26) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (final Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());

                    final String chartsTabsValue = deviceSharedPrefs.getString("charts_tabs", null);
                    if (chartsTabsValue == null) {
                        continue;
                    }

                    final String newPrefValue;
                    if (!StringUtils.isBlank(chartsTabsValue)) {
                        newPrefValue = chartsTabsValue + ",spo2";
                    } else {
                        newPrefValue = "spo2";
                    }

                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    deviceSharedPrefsEdit.putString("charts_tabs", newPrefValue);
                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 27) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (final Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();

                    for (final Map.Entry<String, ?> entry : deviceSharedPrefs.getAll().entrySet()) {
                        final String key = entry.getKey();
                        if (key.startsWith("huami_2021_known_config_")) {
                            deviceSharedPrefsEdit.putString(
                                    key.replace("huami_2021_known_config_", "") + "_is_known",
                                    entry.getValue().toString()
                            );
                        } else if (key.endsWith("_huami_2021_possible_values")) {
                            deviceSharedPrefsEdit.putString(
                                    key.replace("_huami_2021_possible_values", "") + "_possible_values",
                                    entry.getValue().toString()
                            );
                        }
                    }

                    deviceSharedPrefsEdit.apply();
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        if (oldVersion < 28) {
            try (DBHandler db = acquireDB()) {
                final DaoSession daoSession = db.getDaoSession();
                final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

                for (final Device dbDevice : activeDevices) {
                    final SharedPreferences deviceSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                    final SharedPreferences.Editor deviceSharedPrefsEdit = deviceSharedPrefs.edit();
                    boolean shouldApply = false;

                    if (!"UNKNOWN".equals(deviceSharedPrefs.getString("events_forwarding_fellsleep_action_selection", "UNKNOWN"))) {
                        shouldApply = true;
                        deviceSharedPrefsEdit.putStringSet(
                                "events_forwarding_fellsleep_action_selections",
                                Collections.singleton(deviceSharedPrefs.getString("events_forwarding_fellsleep_action_selection", "UNKNOWN"))
                        );
                    }
                    if (!"UNKNOWN".equals(deviceSharedPrefs.getString("events_forwarding_wokeup_action_selection", "UNKNOWN"))) {
                        shouldApply = true;
                        deviceSharedPrefsEdit.putStringSet(
                                "events_forwarding_wokeup_action_selections",
                                Collections.singleton(deviceSharedPrefs.getString("events_forwarding_wokeup_action_selection", "UNKNOWN"))
                        );
                    }
                    if (!"UNKNOWN".equals(deviceSharedPrefs.getString("events_forwarding_startnonwear_action_selection", "UNKNOWN"))) {
                        shouldApply = true;
                        deviceSharedPrefsEdit.putStringSet(
                                "events_forwarding_startnonwear_action_selections",
                                Collections.singleton(deviceSharedPrefs.getString("events_forwarding_startnonwear_action_selection", "UNKNOWN"))
                        );
                    }

                    if (shouldApply) {
                        deviceSharedPrefsEdit.apply();
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "error acquiring DB lock");
            }
        }

        editor.putString(PREFS_VERSION, Integer.toString(CURRENT_PREFS_VERSION));
        editor.apply();
    }
    private void migrateStringPrefToPerDevicePref(String globalPref, String globalPrefDefault, String perDevicePref, ArrayList<DeviceType> deviceTypes) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String globalPrefValue = prefs.getString(globalPref, globalPrefDefault);
        try (DBHandler db = acquireDB()) {
            DaoSession daoSession = db.getDaoSession();
            List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
            for (Device dbDevice : activeDevices) {
                SharedPreferences deviceSpecificSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                if (deviceSpecificSharedPrefs != null) {
                    SharedPreferences.Editor deviceSharedPrefsEdit = deviceSpecificSharedPrefs.edit();
                    DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

                    if (deviceTypes.contains(deviceType)) {
                        Log.i(TAG, "migrating global string preference " + globalPref + " for " + deviceType.name() + " " + dbDevice.getIdentifier() );
                        deviceSharedPrefsEdit.putString(perDevicePref, globalPrefValue);
                    }
                    deviceSharedPrefsEdit.apply();
                }
            }
            editor.remove(globalPref);
            editor.apply();
        } catch (Exception e) {
            Log.w(TAG, "error acquiring DB lock");
        }
    }

    private void migrateBooleanPrefToPerDevicePref(String globalPref, Boolean globalPrefDefault, String perDevicePref, ArrayList<DeviceType> deviceTypes) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        boolean globalPrefValue = prefs.getBoolean(globalPref, globalPrefDefault);
        try (DBHandler db = acquireDB()) {
            DaoSession daoSession = db.getDaoSession();
            List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);
            for (Device dbDevice : activeDevices) {
                SharedPreferences deviceSpecificSharedPrefs = ControllerApplication.getDeviceSpecificSharedPrefs(dbDevice.getIdentifier());
                if (deviceSpecificSharedPrefs != null) {
                    SharedPreferences.Editor deviceSharedPrefsEdit = deviceSpecificSharedPrefs.edit();
                    DeviceType deviceType = DeviceType.fromName(dbDevice.getTypeName());

                    if (deviceTypes.contains(deviceType)) {
                        Log.i(TAG, "migrating global boolean preference " + globalPref + " for " + deviceType.name() + " " + dbDevice.getIdentifier() );
                        deviceSharedPrefsEdit.putBoolean(perDevicePref, globalPrefValue);
                    }
                    deviceSharedPrefsEdit.apply();
                }
            }
            editor.remove(globalPref);
            editor.apply();
        } catch (Exception e) {
            Log.w(TAG, "error acquiring DB lock");
        }
    }
    public static LimitedQueue<Integer, String> getIDSenderLookup() {
        return mIDSenderLookup;
    }

    private void migrateDeviceTypes() {
        try (DBHandler db = acquireDB()) {
            final InputStream inputStream = getAssets().open("migrations/devicetype.json");
            final byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            final JSONObject deviceMapping = new JSONObject(new String(buffer));
            final JSONObject deviceIdNameMapping = deviceMapping.getJSONObject("by-id");

            final DaoSession daoSession = db.getDaoSession();
            final List<Device> activeDevices = DBHelper.getActiveDevices(daoSession);

            for (Device dbDevice : activeDevices) {
                String deviceTypeName = dbDevice.getTypeName();
                if(deviceTypeName.isEmpty() || deviceTypeName.equals("UNKNOWN")){
                    deviceTypeName = deviceIdNameMapping.optString(
                            String.valueOf(dbDevice.getType()),
                            "UNKNOWN"
                    );
                    dbDevice.setTypeName(deviceTypeName);
                    daoSession.getDeviceDao().update(dbDevice);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "error acquiring DB lock");
        }
    }
    protected DeviceService createDeviceService() {
        return new GBDeviceService(this);
    }
    private int getPrefsFileVersion() {
        try {
            return Integer.parseInt(sharedPrefs.getString(PREFS_VERSION, "0")); //0 is legacy
        } catch (Exception e) {
            //in version 1 this was an int
            return 1;
        }
    }
    public static void quit() {
        GB.log("Quitting Gadgetbridge...", GB.INFO, null);
        Intent quitIntent = new Intent(ControllerApplication.ACTION_QUIT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(quitIntent);
        ControllerApplication.deviceService().quit();
        System.exit(0);
    }
    public static GBPrefs getGBPrefs() {
        return gbPrefs;
    }
    public static boolean isRunningOreoOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
    public static boolean isRunningNougatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
    public static boolean isRunningMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    public static void releaseDB() {
        dbLock.unlock();
    }

    public ControllerApplication() {
        context = this;
    }
    public static DeviceService deviceService() {
        return deviceService;
    }
    public static DeviceService deviceService(GBDevice device) {
        return deviceService.forDevice(device);
    }
    public static void deleteDeviceSpecificSharedPrefs(String deviceIdentifier) {
        if (deviceIdentifier == null || deviceIdentifier.isEmpty()) {
            return;
        }
        context.getSharedPreferences("devicesettings_" + deviceIdentifier, Context.MODE_PRIVATE).edit().clear().apply();
    }
    public static DBHandler acquireDB() throws GBException {
        try {
            if (dbLock.tryLock(30, TimeUnit.SECONDS)) {
                return lockHandler;
            }
        } catch (InterruptedException ex) {
            Log.i(TAG, "Interrupted while waiting for DB lock");
        }
        throw new GBException("Unable to access the database.");
    }

    public void setupDatabase() {
        DaoMaster.OpenHelper helper;
//        deleteActivityDatabase(context);
        helper = new DBOpenHelper(this, DATABASE_NAME, null);

        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        if (lockHandler == null) {
            lockHandler = new LockHandler();
        }
        lockHandler.init(daoMaster, helper);
    }
    public static synchronized boolean deleteActivityDatabase(Context context) {
        // TODO: flush, close, reopen db
        if (lockHandler != null) {
            lockHandler.closeDb();
        }
        boolean result = deleteOldActivityDatabase(context);
        result &= getContext().deleteDatabase(DATABASE_NAME);
        return result;
    }
    public static synchronized boolean deleteOldActivityDatabase(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        boolean result = true;
        if (dbHelper.existsDB("ActivityDatabase")) {
            result = getContext().deleteDatabase("ActivityDatabase");
        }
        return result;
    }
    public static ControllerApplication getApp() {
        return app;
    }

    public static SharedPreferences getDeviceSpecificSharedPrefs(String deviceIdentifier) {
        if (deviceIdentifier == null || deviceIdentifier.isEmpty()) {
            return null;
        }
        return context.getSharedPreferences("devicesettings_" + deviceIdentifier, Context.MODE_PRIVATE);
    }

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }
    public static Prefs getPrefs() {
        return prefs;
    }

    public static Context getContext() {
        return context;
    }

    public void setOpenTracksObserver(OpenTracksContentObserver openTracksObserver) {
        this.openTracksObserver = openTracksObserver;
    }
    public static boolean minimizeNotification() {
        return prefs.getBoolean("minimize_priority", false);
    }
    public OpenTracksContentObserver getOpenTracksObserver() {
        return openTracksObserver;
    }
}
