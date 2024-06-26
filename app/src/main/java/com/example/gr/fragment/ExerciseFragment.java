package com.example.gr.fragment;

import static com.example.gr.device.model.DeviceService.ACTION_CONNECT;
import static com.example.gr.utils.GB.toast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.activity.DiscoveryActivityV2;
import com.example.gr.activity.MainActivity;
import com.example.gr.activity.SearchForExerciseActivity;
import com.example.gr.adapter.HistoryAdapter;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.FragmentExerciseBinding;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDevice;
import com.example.gr.device.model.RecordedDataTypes;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.GB;
import com.example.gr.model.BaseWorkout;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExerciseFragment extends BaseFragment {
    private FragmentExerciseBinding mFragmentExerciseBinding;
    private HistoryAdapter mHistoryAdapter;
    private List<BaseWorkout> workoutList;
    private Button addWearableBtn;
    private TextView tvWearableName;
    private TextView tvWearableStatus;
    private DeviceCoordinator deviceCoordinator;
    private DeviceManager deviceManager;
    private GBDevice device;
    private Button syncBtn;
    private Diary mDiary;
    private ActivityUser activityUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentExerciseBinding = FragmentExerciseBinding.inflate(inflater, container, false);
        getDiary(DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime()));
        activityUser = new ActivityUser();
        initUI();
        return mFragmentExerciseBinding.getRoot();
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStepsData(HashMap<String,long[]> deviceActivityHashMap){
        long[] dailyTotals = new long[]{0, 0};
        if (deviceActivityHashMap.containsKey(device.getAddress())) {
            dailyTotals = deviceActivityHashMap.get(device.getAddress());
        }
        int steps = (int) dailyTotals[0];
        int sleep = (int) dailyTotals[1];
        System.out.println("steps : " + steps);
        mDiary.setTotalSteps(steps);
        mDiary.updateDiary();
        displayWorkoutInfo();
        EventBus.getDefault().unregister(this);
    }
    private void initUI() {
        displayWorkoutInfo();
        workoutList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            workoutList.add(new BaseWorkout(12, LocalDateTime.now()));
            workoutList.add(new BaseWorkout(13, LocalDateTime.now()));
            workoutList.add(new BaseWorkout(14, LocalDateTime.now()));
        }
        mHistoryAdapter = new HistoryAdapter(workoutList);
        mFragmentExerciseBinding.rcvExHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentExerciseBinding.rcvExHistory.setAdapter(mHistoryAdapter);
        addWearableBtn = mFragmentExerciseBinding.addWearableBtn;
        syncBtn = mFragmentExerciseBinding.syncBtn;
        mFragmentExerciseBinding.btnGpsStart.setOnClickListener(v->{
            goToSearchForExerciseActivity();
        });
        mFragmentExerciseBinding.indoorBtn.setOnClickListener(v->{
            goToSearchForExerciseActivity();
        });
        tvWearableName = mFragmentExerciseBinding.wearableName;
        tvWearableStatus = mFragmentExerciseBinding.wearableStatus;
        deviceManager = ((ControllerApplication) getActivity().getApplication()).getDeviceManager();
        if (deviceManager.getDevices().size() > 0) {
                device = deviceManager.getDevices().get(0);
        }

        showAddWearableInfo();
        addWearableBtn.setOnClickListener(v -> launchDiscoveryActivity());
        syncBtn.setOnClickListener(v -> fetchData());
    }

    private void launchDiscoveryActivity() {
        startActivity(new Intent(this.getActivity(), DiscoveryActivityV2.class));
    }
    private void goToSearchForExerciseActivity(){
        Bundle bundle = new Bundle();
        GlobalFunction.startActivity(getActivity(),SearchForExerciseActivity.class,bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    void createDynamicShortcut(GBDevice device) {
        Context context = this.getContext();
        Intent intent = new Intent(context, MainActivity.class)
                .setAction(ACTION_CONNECT)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra("device", device.getAddress());

        ShortcutManager shortcutManager = (ShortcutManager) context.getApplicationContext().getSystemService(Context.SHORTCUT_SERVICE);

        DeviceCoordinator coordinator = device.getDeviceCoordinator();

        shortcutManager.pushDynamicShortcut(new ShortcutInfo.Builder(context, device.getAddress())
                .setLongLived(false)
                .setShortLabel(device.getAliasOrName())
                .setIntent(intent)
                .setIcon(Icon.createWithResource(context, coordinator.getDefaultIconResource()))
                .build()
        );
    }
    private void displayWorkoutInfo() {
        if (mDiary != null) {
            mFragmentExerciseBinding.exCalBurnt.setText(mDiary.getBurntCalories() + "cal");
            int minute = (mDiary.getTotalWorkoutDuration() - (mDiary.getTotalWorkoutDuration() / 60) * 60);
            String min = null;
            if (minute < 10) {
                min = "0" + minute;
            }else {
                min = String.valueOf(minute);
            }
            mFragmentExerciseBinding.exCalBurntHr.setText(mDiary.getTotalWorkoutDuration() / 60 + ":" + min+" h");
            mFragmentExerciseBinding.stepsBarIndicator.setProgress(mDiary.getTotalSteps()*100/activityUser.getStepsGoal());
            mFragmentExerciseBinding.exSteps.setText(String.valueOf(mDiary.getTotalSteps()));
            mFragmentExerciseBinding.titleGoalSteps.setText("Mục tiêu : " + activityUser.getStepsGoal() + " bước");
        }
    }
    private void getDiary(String date) {
        mDiary = LocalDatabase.getInstance(this.requireActivity()).diaryDAO().getDiaryByDate(date);
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(this.requireActivity()).diaryDAO().insertDiary(mDiary);
        }
        System.out.println(mDiary.getDate());
    }

    private void fetchData() {
        if (device.isInitialized() && device.isConnected()) {
            showTransientSnackbar(R.string.controlcenter_snackbar_need_longpress);
        } else {
            showTransientSnackbar(R.string.controlcenter_snackbar_connecting);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                createDynamicShortcut(device);
//            }
            ControllerApplication.deviceService(device).connect();
            return;
        }
        showTransientSnackbar(R.string.busy_task_fetch_activity_data);
        ControllerApplication.deviceService(device).onFetchRecordedData(RecordedDataTypes.TYPE_SYNC);
    }

    private void showAddWearableInfo() {
        if (ControllerApplication.getPrefs().getBoolean("display_add_wearable_btn", true)) {
            addWearableBtn.setVisibility(View.VISIBLE);
            syncBtn.setVisibility(View.INVISIBLE);
            tvWearableName.setText(R.string.no_device_found);
            tvWearableStatus.setVisibility(View.GONE);
        } else {
            if (deviceManager.getDevices().size() < 1) {
                addWearableBtn.setVisibility(View.VISIBLE);
                syncBtn.setVisibility(View.GONE);
                tvWearableName.setText(R.string.no_device_found);
                tvWearableStatus.setVisibility(View.GONE);
            } else {
                addWearableBtn.setVisibility(View.GONE);
                tvWearableName.setText(device.getName());
                syncBtn.setVisibility(View.VISIBLE);
                tvWearableStatus.setVisibility(View.VISIBLE);
                if (device.isConnected() && device.isInitialized()) {
                    tvWearableStatus.setText(R.string.connected);
                } else if (device.isConnecting()) {
                    tvWearableStatus.setText(R.string.connecting);
                } else {
                    tvWearableStatus.setText(R.string.not_connected);
                }
            }
        }
    }

    private void checkAndRequestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            LOG.error("No permission to access background location!");
            toast(getActivity(), getString(R.string.error_no_location_access), Toast.LENGTH_SHORT, GB.ERROR);
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        showAddWearableInfo();
    }
    @Override
    protected void updateUIAfterShowSnackBar() {
        displayWorkoutInfo();
        showAddWearableInfo();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Thể dục");
        }
    }
}
