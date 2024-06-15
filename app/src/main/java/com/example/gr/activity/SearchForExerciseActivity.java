package com.example.gr.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.R;
import com.example.gr.adapter.ExerciseAdapter;
import com.example.gr.adapter.SearchFragmentViewPagerAdapter;
import com.example.gr.databinding.ActivitySearchForExerciseBinding;
import com.example.gr.model.Exercise;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchForExerciseActivity extends BaseActivity{
    private ActivitySearchForExerciseBinding mActivitySearchForExerciseBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivitySearchForExerciseBinding = ActivitySearchForExerciseBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchForExerciseBinding.getRoot());
        initToolbar();
        initUi();
    }
    private void initUi() {
        List<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise("Chạy bộ", 260 /30,30));
        exerciseList.add(new Exercise("Khiêu vũ", 111 /30,30));
        exerciseList.add(new Exercise("Đạp xe", 186 /30,30));
        exerciseList.add(new Exercise("Bóng rổ",728/60,60));
        exerciseList.add(new Exercise("Tennis đơn",728/60,60));
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exerciseList,this::onClickViewDetail);
        mActivitySearchForExerciseBinding.rcvExerciseList.setLayoutManager(new LinearLayoutManager(this));
        mActivitySearchForExerciseBinding.rcvExerciseList.setAdapter(exerciseAdapter);

    }
    private void onClickViewDetail(Exercise exercise){
//        Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
        View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_add_exercise, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);

        TextView tvExerciseName = viewDialog.findViewById(R.id.dialog_ex_item_name);
        TextView tvExerciseCaloriesBurnt = viewDialog.findViewById(R.id.dialog_exercise_item_calo);
        TextView tvExerciseDuration = viewDialog.findViewById(R.id.dialog_exercise_item_min);
        TextView tvCount = viewDialog.findViewById(R.id.dialog_tv_count);
        TextView tvSubstractBtn = viewDialog.findViewById(R.id.dialog_tv_subtract);
        TextView tvAddBtn = viewDialog.findViewById(R.id.dialog_tv_add);
        TextView tvCloseBtn = viewDialog.findViewById(R.id.dialog_sheet_close_btn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvAddToDiaryBtn = viewDialog.findViewById(R.id.dialog_tv_add_diary);

        tvExerciseName.setText(exercise.getName());
        tvExerciseCaloriesBurnt.setText(getString(R.string.unit_calories_burnt,String.valueOf(exercise.getCaloriesBurntPerMin()*exercise.getDefaultDuration())));
        tvExerciseDuration.setText(getString(R.string._duration,exercise.getDefaultDuration()));
        tvCount.setText(String.valueOf(exercise.getDefaultDuration()));

        tvSubstractBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            if (count <= 1) {
                return;
            }
            int newCount = Integer.parseInt(tvCount.getText().toString()) - 1;
            tvCount.setText(String.valueOf(newCount));
            int newCaloBurnt = exercise.getCaloriesBurntPerMin() * newCount;
            tvExerciseCaloriesBurnt.setText(getString(R.string.unit_calories_burnt,String.valueOf(newCaloBurnt)));
            tvExerciseDuration.setText(getString(R.string._duration,newCount));
            exercise.setDuration(newCount);
            exercise.setCaloriesBurntCount(newCaloBurnt);
        });

        tvAddBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            int newCount = Integer.parseInt(tvCount.getText().toString()) + 1;
            tvCount.setText(String.valueOf(newCount));
            int newCaloBurnt = exercise.getCaloriesBurntPerMin() * newCount;
            tvExerciseCaloriesBurnt.setText(getString(R.string.unit_calories_burnt,String.valueOf(newCaloBurnt)));
            tvExerciseDuration.setText(getString(R.string._duration,newCount));
            exercise.setDuration(newCount);
            exercise.setCaloriesBurntCount(newCaloBurnt);
        });

        tvCloseBtn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddToDiaryBtn.setOnClickListener(v -> {
//            FoodDatabase.getInstance(FoodDetailActivity.this).foodDAO().insertFood(mFood);
//            setStatusButtonAddToCart();
//            EventBus.getDefault().post(new ReloadListCartEvent());
            Toast.makeText(this,"added to diary",Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();

        });

        bottomSheetDialog.show();



    }
    private void initToolbar() {
        mActivitySearchForExerciseBinding.imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}
