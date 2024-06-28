package com.example.gr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gr.constant.Constant;
import com.example.gr.databinding.ActivityFoodDetailBinding;
import com.example.gr.databinding.ActivityWorkoutDetailBinding;
import com.example.gr.device.GBDevice;
import com.example.gr.model.Diary;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.utils.DateTimeUtils;

import java.util.concurrent.TimeUnit;

public class WorkoutDetailActivity extends BaseActivity  {

    ActivityWorkoutDetailBinding mActivityWorkoutDetailBinding;
    private RecordedWorkout mRecordedWorkout;
    private GBDevice mDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityWorkoutDetailBinding = ActivityWorkoutDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityWorkoutDetailBinding.getRoot());
        initToolbar();
        getDataIntent();
        initUI();

//        initListener();
    }
    private void initUI(){
        mActivityWorkoutDetailBinding.workoutName.setText(mRecordedWorkout.getName());
        mActivityWorkoutDetailBinding.workoutDistance.setText(mRecordedWorkout.getDistance()+  "km");
        mActivityWorkoutDetailBinding.workoutCalBurnt.setText(String.valueOf(mRecordedWorkout.getCaloriesBurnt()));
        mActivityWorkoutDetailBinding.workoutDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) mRecordedWorkout.getDuration(), TimeUnit.MILLISECONDS)));
        mActivityWorkoutDetailBinding.workoutAvgHr.setText(String.valueOf(mRecordedWorkout.getAvgHeartRate()));
        mActivityWorkoutDetailBinding.workoutMaxHr.setText(String.valueOf(mRecordedWorkout.getMaxHeartRate()));
        mActivityWorkoutDetailBinding.workoutMinHr.setText(String.valueOf(mRecordedWorkout.getMinHeartRate()));


    }
    private void initToolbar() {
        mActivityWorkoutDetailBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDevice = (GBDevice) bundle.get(Constant.KEY_INTENT_DEVICE_OBJECT);
            mRecordedWorkout = (RecordedWorkout) bundle.get(Constant.KEY_INTENT_RECORDED_WORKOUT_OBJECT);

        }

    }

}
