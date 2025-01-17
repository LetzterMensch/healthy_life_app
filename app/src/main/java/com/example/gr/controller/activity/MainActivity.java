package com.example.gr.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.view.adapter.MainViewPagerAdapter;
import com.example.gr.model.data.sample.ActivitySample;
import com.example.gr.model.database.DBAccess;
import com.example.gr.model.database.DBHandler;
import com.example.gr.databinding.ActivityMainBinding;
import com.example.gr.controller.device.DeviceCoordinator;
import com.example.gr.controller.device.DeviceManager;
import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.device.model.DailyTotals;
import com.example.gr.controller.device.model.DeviceService;
import com.example.gr.controller.fragment.BaseFragment;
import com.example.gr.utils.HeartRateUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// Hỗ trợ chuyển tiếp giữa các fragments
public class MainActivity extends BaseActivity {
    private static final long DEBOUNCE_INTERVAL = 500; // 500ms
    private Handler handler = new Handler();
    private Runnable runnable;

    private ActivityMainBinding mActivityMainBinding;
    private DeviceManager deviceManager;
    private List<GBDevice> deviceList;
    private ActivitySample currentHRSample;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private BaseFragment currentFragment;
//    private GBDeviceAdapterv2 mGBDeviceAdapter;

    private HashMap<String,long[]> deviceActivityHashMap = new HashMap<>();
    public HashMap<String,long[]> getDeviceActivityHashMap(){
        return this.deviceActivityHashMap;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (Objects.requireNonNull(action)) {
                case ControllerApplication.ACTION_NAV_DASHBOARD_FRAGMENT:
                    mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                    mActivityMainBinding.viewpager2.setCurrentItem(0);
                    createRefreshTask("get activity data", getApplication()).execute();
                    break;
                case ControllerApplication.ACTION_NAV_DIARY_FRAGMENT:
                    mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_diary).setChecked(true);
                    mActivityMainBinding.viewpager2.setCurrentItem(1);
                    break;
                case ControllerApplication.ACTION_NAV_EXERCISE_FRAGMENT:
                    mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_exercise).setChecked(true);
                    mActivityMainBinding.viewpager2.setCurrentItem(2);
                    createRefreshTask("get activity data", getApplication()).execute();
                    postDebouncedEvent(deviceActivityHashMap);
                    break;
                case ControllerApplication.ACTION_NAV_SLEEP_FRAGMENT:
                    mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_sleep).setChecked(true);
                    mActivityMainBinding.viewpager2.setCurrentItem(3);
                    createRefreshTask("get activity data", getApplication()).execute();
                    break;
                case ControllerApplication.ACTION_NAV_PROFILE_FRAGMENT:
                    mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                    mActivityMainBinding.viewpager2.setCurrentItem(4);
                    break;
                case ControllerApplication.ACTION_QUIT:
                    finish();
                    break;
                case DeviceManager.ACTION_DEVICES_CHANGED:
                case ControllerApplication.ACTION_NEW_DATA:
                    createRefreshTask("get activity data", getApplication()).execute();
                    postDebouncedEvent(deviceActivityHashMap);
                    break;
                case DeviceService.ACTION_REALTIME_SAMPLES:
                    handleRealtimeSample(intent.getSerializableExtra(DeviceService.EXTRA_REALTIME_SAMPLE));
                    break;
//                case ACTION_REQUEST_PERMISSIONS:
//                    checkAndRequestPermissions();
//                    break;
//                case ACTION_REQUEST_LOCATION_PERMISSIONS:
//                    checkAndRequestLocationPermissions();
//                    break;
            }
        }
    };
    public void postDebouncedEvent(final Object event) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(event);
            }
        };

        handler.postDelayed(runnable, DEBOUNCE_INTERVAL);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ControllerApplication.ACTION_NAV_DASHBOARD_FRAGMENT);
        intentFilter.addAction(ControllerApplication.ACTION_NAV_DIARY_FRAGMENT);
        intentFilter.addAction(ControllerApplication.ACTION_NAV_EXERCISE_FRAGMENT);
        intentFilter.addAction(ControllerApplication.ACTION_NAV_SLEEP_FRAGMENT);
        intentFilter.addAction(ControllerApplication.ACTION_NAV_PROFILE_FRAGMENT);
        intentFilter.addAction(ControllerApplication.ACTION_QUIT);
        intentFilter.addAction(ControllerApplication.ACTION_NEW_DATA);
        intentFilter.addAction(DeviceManager.ACTION_DEVICES_CHANGED);
        intentFilter.addAction(DeviceService.ACTION_REALTIME_SAMPLES);
        deviceManager = ((ControllerApplication) getApplication()).getDeviceManager();
        deviceList = deviceManager.getDevices();
        if(deviceList.size() > 0){
            if(deviceList.get(0).isInitialized()){
                createRefreshTask("get activity data", getApplication()).execute();
            }
        }
        mActivityMainBinding.viewpager2.setUserInputEnabled(false);
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mActivityMainBinding.viewpager2.setAdapter(mainViewPagerAdapter);
        mActivityMainBinding.viewpager2.setOffscreenPageLimit(1);
        mActivityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
                        break;

                    case 1:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_diary).setChecked(true);
                        break;

                    case 2:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_exercise).setChecked(true);
                        break;

                    case 3:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_sleep).setChecked(true);
                        break;

                    case 4:
                        mActivityMainBinding.navView.getMenu().findItem(R.id.navigation_profile).setChecked(true);
                        break;
                }
            }
        });

        mActivityMainBinding.navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.navigation_dashboard){
                mActivityMainBinding.viewpager2.setCurrentItem(0);
            }else if ( id == R.id.navigation_diary){
                mActivityMainBinding.viewpager2.setCurrentItem(1);
            }else if (id == R.id.navigation_exercise){
                mActivityMainBinding.viewpager2.setCurrentItem(2);
            }else if (id == R.id.navigation_sleep){
                mActivityMainBinding.viewpager2.setCurrentItem(3);
            }else if (id == R.id.navigation_profile){
                mActivityMainBinding.viewpager2.setCurrentItem(4);
            }
            return true;
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);


    }
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    public class RefreshTask extends DBAccess {
        public RefreshTask(String task, Context context) {
            super(task, context);
        }

        @Override
        protected void doInBackground(DBHandler db) {
            for (GBDevice gbDevice : deviceList) {
                final DeviceCoordinator coordinator = gbDevice.getDeviceCoordinator();
                if (coordinator.supportsActivityTracking()) {
                    long[] stepsAndSleepData = getSteps(gbDevice, db);
                    deviceActivityHashMap.put(gbDevice.getAddress(), stepsAndSleepData);
                }
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            updateData();
        }

    }

    private void updateData(){

    }

    private void handleRealtimeSample(Serializable extra) {
        if (extra instanceof ActivitySample) {
            ActivitySample sample = (ActivitySample) extra;
            setCurrentHRSample(sample);
        }
    }
    private void setCurrentHRSample(ActivitySample sample) {
        if (HeartRateUtils.getInstance().isValidHeartRateValue(sample.getHeartRate())) {
            currentHRSample = sample;
//            refreshPairedDevices();
        }
    }
    protected RefreshTask createRefreshTask(String task, Context context) {
        return new RefreshTask(task, context);
    }
    private long[] getSteps(GBDevice device, DBHandler db) {
        Calendar day = GregorianCalendar.getInstance();

        DailyTotals ds = new DailyTotals();
        return ds.getDailyTotalsForDevice(device, day, db);
    }
//    @Override
//    public void onBackPressed() {
//        super.getOnBackPressedDispatcher().onBackPressed();
//        showConfirmExitApp();
//    }

    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finish())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }
    public void setToolBar(boolean isHome, String title) {
        if (isHome) {
            mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.GONE);
            return;
        }
        mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityMainBinding.toolbar.tvTitle.setText(title);
    }
}