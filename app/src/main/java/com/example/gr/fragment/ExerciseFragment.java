package com.example.gr.fragment;

import static com.example.gr.utils.GB.toast;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.activity.DiscoveryActivityV2;
import com.example.gr.activity.MainActivity;
import com.example.gr.adapter.HistoryAdapter;
import com.example.gr.databinding.FragmentExerciseBinding;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.device.GBDevice;
import com.example.gr.utils.GB;
import com.example.gr.model.BaseWorkout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExerciseFragment extends BaseFragment{
    private FragmentExerciseBinding mFragmentExerciseBinding;
    private HistoryAdapter mHistoryAdapter;
    private List<BaseWorkout> workoutList;
    private Button addWearableBtn;
    private TextView tvWearableName;
    private DeviceCoordinator deviceCoordinator;
    private GBDevice device;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentExerciseBinding = FragmentExerciseBinding.inflate(inflater, container, false);
        initUI();
        return mFragmentExerciseBinding.getRoot();
    }
    private void initUI(){
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
        tvWearableName = mFragmentExerciseBinding.wearableName;
        showAddWearableInfoIfNecessary();
        mFragmentExerciseBinding.addWearableBtn.setOnClickListener(v->launchDiscoveryActivity());
    }
    private void launchDiscoveryActivity() {
        startActivity(new Intent(this.getActivity(), DiscoveryActivityV2.class));
    }
    private void showAddWearableInfoIfNecessary() {
        int devicesCount = ControllerApplication.getApp().getDeviceManager().getDevices().size();
        if( devicesCount == 0){
            addWearableBtn.setVisibility(View.VISIBLE);
            tvWearableName.setText(R.string.no_device_found);
        }else{
            device = ControllerApplication.getApp().getDeviceManager().getDevices().get(0);
            addWearableBtn.setVisibility(View.INVISIBLE);
            tvWearableName.setText(device.getName());
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
        showAddWearableInfoIfNecessary();
    }
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Thể dục");
        }
    }
}
