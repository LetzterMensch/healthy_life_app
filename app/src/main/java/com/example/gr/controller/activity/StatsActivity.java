package com.example.gr.controller.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.gr.R;
import com.example.gr.databinding.ActivityLogWeightBinding;
import com.example.gr.databinding.ActivityStatsBinding;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.utils.constant.Constant;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatsActivity extends BaseActivity {
    private ActivityStatsBinding activityStatsBinding;
    private Diary diary;
    private PieChart pieChart;
    private ActivityUser activityUser;
    private final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStatsBinding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(activityStatsBinding.getRoot());
        getDataIntent();
        activityUser = new ActivityUser();
        initUI();
//        initListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            diary = (Diary) bundle.get("key_diary");
        }
    }

    private void initUI() {
        pieChart = activityStatsBinding.pieChart;
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry = new PieEntry((float) (diary.getIntakeCarb() * 100) / diary.getIntakeCalories());
        pieEntries.add(pieEntry);
        pieEntry = new PieEntry((float) (diary.getIntakeProtein() * 100) / diary.getIntakeCalories());
        pieEntries.add(pieEntry);
        pieEntry = new PieEntry((float) (diary.getIntakeFat() * 100) / diary.getIntakeCalories());
        pieEntries.add(pieEntry);
        // Initialize pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Tinh bột, Chất đạm, Chất béo");
        final List<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.green));
        colors.add(ContextCompat.getColor(this, R.color.colorPrimary));
        colors.add(ContextCompat.getColor(this, R.color.yellow));
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.white));
        pieChart.setData(new PieData(pieDataSet));
        pieChart.getData().setValueTextSize(20f);
        pieChart.animateXY(1500, 1500);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(diary.getIntakeCalories()+ "KCal");
        pieChart.setCenterTextSize(18);
        // Set text view
        activityStatsBinding.caloriesGoal.setText(getString( R.string.unit_calories_burnt, String.valueOf(diary.getCaloriesGoal())));
        activityStatsBinding.caloriesBurnt.setText(getString( R.string.unit_calories_burnt, String.valueOf(diary.getBurntCalories())));
        activityStatsBinding.caloriesIntake.setText(getString( R.string.unit_calories_burnt, String.valueOf(diary.getIntakeCalories())));
        // Set visibility
        if (diary.getIntakeCalories() < 1000) {
            activityStatsBinding.notEatingEnoughLayout.setVisibility(View.VISIBLE);
            activityStatsBinding.estimatedWeightLayout.setVisibility(View.GONE);
        } else {
            activityStatsBinding.notEatingEnoughLayout.setVisibility(View.GONE);
            activityStatsBinding.estimatedWeightLayout.setVisibility(View.VISIBLE);
        }
        // Evoke canned message
        // Assume : 1 kg = 7700 Kcal
        float newKg;
        newKg = Math.abs((diary.getRemainingCalories() * 7 * 5 / 7700));
        if (diary.getRemainingCalories() > 0) {
            activityStatsBinding.tvEstimatedWeight.setText(getString(R.string.estimated_weight_in_5_weeks, df.format(activityUser.getWeightKg() + newKg)));
        } else {
            activityStatsBinding.tvEstimatedWeight.setText(getString(R.string.estimated_weight_in_5_weeks, df.format(activityUser.getWeightKg() - newKg)));
        }
        //
        activityStatsBinding.btnBackToDiary.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());
    }
}
