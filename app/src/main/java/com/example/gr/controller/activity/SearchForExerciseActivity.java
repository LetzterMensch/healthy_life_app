package com.example.gr.controller.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.R;
import com.example.gr.databinding.ActivitySearchForExerciseBinding;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.model.Exercise;
import com.example.gr.model.Workout;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.view.adapter.ExerciseAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchForExerciseActivity extends BaseActivity {
    private ActivitySearchForExerciseBinding mActivitySearchForExerciseBinding;
    private ActivityUser activityUser;
    private Workout tempWorkout;
    private List<Exercise> mExerciseList;
    ExerciseAdapter exerciseAdapter;
    private Diary mDiary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchForExerciseBinding = ActivitySearchForExerciseBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchForExerciseBinding.getRoot());
        activityUser = new ActivityUser();
        getDiary(DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime()));
        initListener();
        initToolbar();
        getListExerciseFromLocalDatabase("");
    }

    private void getListExerciseFromFirebase(String searchkey) {
    }

    private void getListExerciseFromLocalDatabase(String searchkey) {
        if (searchkey == null || searchkey.isEmpty()) {
            mExerciseList = LocalDatabase.getInstance(this).exerciseDAO().getAllExercise();
        } else {
            mExerciseList = LocalDatabase.getInstance(this).exerciseDAO().findExerciseByName("%" + searchkey + "%");
        }
        displayExerciseList();
    }

    private void displayExerciseList() {
        exerciseAdapter = new ExerciseAdapter(mExerciseList, this::onClickViewDetail, this::quickAddBtn);
        mActivitySearchForExerciseBinding.rcvExerciseList.setLayoutManager(new LinearLayoutManager(this));
        mActivitySearchForExerciseBinding.rcvExerciseList.setAdapter(exerciseAdapter);
    }

    private void quickAddBtn(Workout workout) {
//        workout.setDiaryID(mDiary.getId());
        mDiary.logWorkoutItem(workout);
        Toast.makeText(this, "Đã thêm vào lịch sử tập luyện", Toast.LENGTH_SHORT).show();
    }

    private void onClickViewDetail(Exercise exercise) {
//        Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
        AtomicInteger newCaloBurnt = new AtomicInteger(Math.round( exercise.getMet() * exercise.getDefaultDuration()* activityUser.getWeightKg() / 60));
        tempWorkout = new Workout(exercise, exercise.getDefaultDuration(),activityUser.getWeightKg());
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_add_exercise, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        TextView tvExerciseName = viewDialog.findViewById(R.id.dialog_ex_item_name);
        TextView tvExerciseCaloriesBurnt = viewDialog.findViewById(R.id.dialog_exercise_item_calo);
        TextView tvExerciseDuration = viewDialog.findViewById(R.id.dialog_exercise_item_min);
        EditText tvCount = viewDialog.findViewById(R.id.dialog_tv_count);
        TextView tvSubtractBtn = viewDialog.findViewById(R.id.dialog_tv_subtract);
        TextView tvAddBtn = viewDialog.findViewById(R.id.dialog_tv_add);
        TextView tvCloseBtn = viewDialog.findViewById(R.id.dialog_sheet_close_btn);
        TextView tvAddToDiaryBtn = viewDialog.findViewById(R.id.dialog_tv_add_diary);

        tvExerciseName.setText(exercise.getName());
        tvExerciseCaloriesBurnt.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloBurnt.get())));
        tvExerciseDuration.setText(getString(R.string._duration, exercise.getDefaultDuration()));
        tvCount.setText(String.valueOf(exercise.getDefaultDuration()));
        tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                int newCount = 0;
                if(!text.isEmpty()){
                    newCount = Integer.parseInt(tvCount.getText().toString());
                }
                newCaloBurnt.set(Math.round(exercise.getMet() * newCount * activityUser.getWeightKg()/ 60));
                tvExerciseCaloriesBurnt.setText(getString(R.string.unit_calories_burnt, String.valueOf(newCaloBurnt.get())));
                tvExerciseDuration.setText(getString(R.string._duration, newCount));
                tempWorkout.setDuration(newCount);
                tempWorkout.setCaloriesBurnt(newCaloBurnt.get());
            }
        });
        tvSubtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvCount.getText().toString());
                if (count <= 1) {
                    return;
                }
                int newCount = Integer.parseInt(tvCount.getText().toString()) - 1;
                tvCount.setText(String.valueOf(newCount));
            }
        });

        tvAddBtn.setOnClickListener(v -> {
            int newCount = Integer.parseInt(tvCount.getText().toString()) + 1;
            tvCount.setText(String.valueOf(newCount));
        });

        tvCloseBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddToDiaryBtn.setOnClickListener(v -> {
//            tempWorkout.setDiaryID(mDiary.getId());
            mDiary.logWorkoutItem(tempWorkout);
            Toast.makeText(this, "Đã thêm vào lịch sử tập luyện", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();


    }

    private void getDiary(String date) {
        mDiary = LocalDatabase.getInstance(this).diaryDAO().getDiaryByDate(date);
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(this).diaryDAO().insertDiary(mDiary);
        }
    }

    private void initListener() {
        mActivitySearchForExerciseBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mExerciseList != null) mExerciseList.clear();
                    getListExerciseFromLocalDatabase("");
                }
            }
        });

        mActivitySearchForExerciseBinding.imgSearch.setOnClickListener(view -> searchExercise());

        mActivitySearchForExerciseBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchExercise();
                return true;
            }
            return false;
        });
    }

    private void searchExercise() {
        String searchkey = mActivitySearchForExerciseBinding.edtSearchName.getText().toString().trim();
        if (mExerciseList != null) mExerciseList.clear();
//        getListExerciseFromFirebase(searchkey);
        getListExerciseFromLocalDatabase(searchkey);
        GlobalFunction.hideSoftKeyboard(this);
    }
    private void initToolbar() {
        mActivitySearchForExerciseBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}
