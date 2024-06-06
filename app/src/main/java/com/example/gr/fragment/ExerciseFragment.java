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
import com.example.gr.adapter.HistoryAdapter;
import com.example.gr.databinding.FragmentExerciseBinding;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDevice;
import com.example.gr.device.model.RecordedDataTypes;
import com.example.gr.utils.GB;
import com.example.gr.model.BaseWorkout;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentExerciseBinding = FragmentExerciseBinding.inflate(inflater, container, false);
        initUI();
        return mFragmentExerciseBinding.getRoot();
    }

    private void initUI() {
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
        showAddWearableInfo();
    }
    @Override
    protected void updateUIAfterShowSnackBar() {
        showAddWearableInfo();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Thể dục");
        }
    }
}
