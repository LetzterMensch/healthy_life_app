package com.example.gr;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gr.model.Exercise;
import com.example.gr.model.Food;
import com.example.gr.model.database.DBHandler;
import com.example.gr.model.database.DBHelper;
import com.example.gr.model.database.DBOpenHelper;
import com.example.gr.utils.DataImporter;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.model.database.entities.DaoMaster;
import com.example.gr.model.database.entities.DaoSession;
import com.example.gr.model.database.entities.Device;
import com.example.gr.controller.device.DeviceManager;
import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.device.GBDeviceService;
import com.example.gr.controller.device.GBException;
import com.example.gr.controller.device.model.DeviceService;
import com.example.gr.controller.device.model.Weather;
import com.example.gr.controller.externalevents.BluetoothStateChangeReceiver;
import com.example.gr.controller.externalevents.opentracks.OpenTracksContentObserver;
import com.example.gr.controller.handler.LockHandler;
import com.example.gr.utils.GB;
import com.example.gr.utils.GBPrefs;
import com.example.gr.utils.LimitedQueue;
import com.example.gr.utils.Prefs;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
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
    public static final String ACTION_NAV_EXERCISE_FRAGMENT = "com.example.gr.ControllerApplication.action.exercisefragment";
    public static final String ACTION_NAV_DIARY_FRAGMENT = "com.example.gr.ControllerApplication.action.diaryfragment";
    public static final String ACTION_NAV_DASHBOARD_FRAGMENT = "com.example.gr.ControllerApplication.action.dashboardfragment";
    public static final String ACTION_NAV_SLEEP_FRAGMENT = "com.example.gr.ControllerApplication.action.sleepfragment";
    public static final String ACTION_NAV_PROFILE_FRAGMENT = "com.example.gr.ControllerApplication.action.profilefragment";

    public static final String ACTION_NEW_DATA = "com.example.gr.ControllerApplication.action.new_data";

    public static final String ACTION_QUIT = "com.example.gr.ControllerApplication.action.quit";
    private BluetoothStateChangeReceiver bluetoothStateChangeReceiver;
    private FirebaseDatabase mFirebaseDatabase;


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
        //Set up firebase database
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //Set up local database
        setupDatabase();


        // Uncomment the line below to force a device key migration, after you updated
        // the devicetype.json file
        migrateDeviceTypes();


        Weather.getInstance().setCacheFile(getCacheDir(), prefs.getBoolean("cache_weather", true));

        deviceManager = new DeviceManager(this);


        deviceService = createDeviceService();


        if (isRunningMarshmallowOrLater()) {
//            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (isRunningOreoOrLater()) {
                bluetoothStateChangeReceiver = new BluetoothStateChangeReceiver();
                registerReceiver(bluetoothStateChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            }
        }
    }
    public static ControllerApplication get(Context context) {
        return (ControllerApplication) context.getApplicationContext();
    }
    public DatabaseReference getFoodDatabaseReference() {
        return mFirebaseDatabase.getReference("/shared/food");
    }
    public DatabaseReference getExerciseDatabaseReference() {
        return mFirebaseDatabase.getReference("/shared/exercise");
    }

    public static boolean isRunningTwelveOrLater() {
        return Build.VERSION.SDK_INT >= 31;  // Build.VERSION_CODES.S, but our target SDK is lower
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
                if (deviceTypeName.isEmpty() || deviceTypeName.equals("UNKNOWN")) {
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

    public static void quit() {
        GB.log("Quitting Suc Khoe BK...", GB.INFO, null);
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
        // Import dữ liệu
        //        deleteActivityDatabase(context);
//        LocalDatabase.getInstance(context).clearAllTables();
        DataImporter.importFoodFromJson(getApplicationContext(), LocalDatabase.getInstance(this.getApplicationContext()));
        DataImporter.importExerciseFromJson(getApplicationContext(), LocalDatabase.getInstance(this.getApplicationContext()));
//
//        List<Food> foodList = LocalDatabase.getInstance(this).foodDAO().getAllFood();
//        DatabaseReference foodReference = getFoodDatabaseReference();
//        for (Food food: foodList
//             ) {
//            foodReference.push().setValue(food);
//        }
//
//        List<Exercise> exerciseList = LocalDatabase.getInstance(this).exerciseDAO().getAllExercise();
//        DatabaseReference exerciseReference = getExerciseDatabaseReference();
//        for (Exercise ex: exerciseList
//             ) {
//            exerciseReference.push().setValue(ex);
//        }
        DaoMaster.OpenHelper helper;
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
        LocalDatabase.getInstance(context).clearAllTables();
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

    public static int getBackgroundColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.background, typedValue, true);
        return typedValue.data;
    }

    public static int getSecondaryTextColor(Context context) {
        return context.getResources().getColor(R.color.secondarytext);
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
