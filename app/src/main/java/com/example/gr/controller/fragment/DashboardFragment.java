package com.example.gr.controller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gr.controller.activity.MainActivity;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.FragmentDashboardBinding;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.utils.DateTimeUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DashboardFragment extends BaseFragment {
    private FragmentDashboardBinding mfragmentDashboardBinding;
    private LineChart lineChart;
    private Diary mDiary;
    private ActivityUser activityUser;

    //    private ContactAdapter mContactAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        activityUser = new ActivityUser();
//        initListener();
        initUi();
        return mfragmentDashboardBinding.getRoot();
    }


    private void getDiary(String date) {
        mDiary = LocalDatabase.getInstance(this.requireActivity()).diaryDAO().getDiaryByDate(date);
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(this.requireActivity()).diaryDAO().insertDiary(mDiary);
        }
    }

    private void displayDashboardInfo() {
        getDiary(DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime()));
        if (mDiary != null) {
            mfragmentDashboardBinding.caloriesBurnt.setText(String.valueOf(mDiary.getBurntCalories()));
            mfragmentDashboardBinding.caloriesInput.setText(String.valueOf(mDiary.getIntakeCalories()));
            if (mDiary.getRemainingCalories() == 0) {
                mfragmentDashboardBinding.caloriesRemain.setText(String.valueOf(mDiary.getCaloriesGoal()));
            } else {
                mfragmentDashboardBinding.caloriesRemain.setText(String.valueOf(mDiary.getRemainingCalories()));
            }

            mfragmentDashboardBinding.caloriesCircle.setProgress((int) (mDiary.getIntakeCalories() * 100 / mDiary.getCaloriesGoal()));
            mfragmentDashboardBinding.carbIndicator.setProgress((int) (mDiary.getIntakeCarb() * 100 / mDiary.getCarbGoal()));
            mfragmentDashboardBinding.proteinIndicator.setProgress((int) (mDiary.getIntakeProtein() * 100 / mDiary.getProteinGoal()));
            mfragmentDashboardBinding.fatIndicator.setProgress((int) (mDiary.getIntakeFat() * 100 / mDiary.getFatGoal()));

            mfragmentDashboardBinding.dashboardCarb.setText(mDiary.getIntakeCarb() + "/" + mDiary.getCarbGoal());
            mfragmentDashboardBinding.dashboardProtein.setText(mDiary.getIntakeProtein() + "/" + mDiary.getProteinGoal());
            mfragmentDashboardBinding.dashboardFat.setText(mDiary.getIntakeFat() + "/" + mDiary.getFatGoal());

            mfragmentDashboardBinding.dashboardCalBurnt.setText(mDiary.getBurntCalories() + "cal");
            mfragmentDashboardBinding.calBurntDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) mDiary.getTotalWorkoutDuration(), TimeUnit.MILLISECONDS)));
            mfragmentDashboardBinding.stepsBarIndicator.setProgress(mDiary.getTotalSteps()*100/activityUser.getStepsGoal());
            mfragmentDashboardBinding.dashboardSteps.setText(String.valueOf(mDiary.getTotalSteps()));
            mfragmentDashboardBinding.titleGoalSteps.setText("Mục tiêu : " + activityUser.getStepsGoal() + " bước");

        }
    }

    private void displayChartInfo() {
        lineChart = mfragmentDashboardBinding.lineChart;
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 81f));
        entries.add(new Entry(1f, 80.5f));
        entries.add(new Entry(2f, 80.7f));
        entries.add(new Entry(3f, 80f));
        entries.add(new Entry(4f, 79.5f));
        entries.add(new Entry(5f, 79.7f));
        LineDataSet dataSet = new LineDataSet(entries, "Cân nặng"); // Thẻ cho dữ liệu

        // Tùy chỉnh giao diện cho LineDataSet
        dataSet.setColor(Color.rgb(252, 83, 83)); // Color Accent
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(Color.RED);

        LineData lineData = new LineData(dataSet);

        // Thay đổi màu của các giá trị
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK); // Màu của giá trị

        // Loại bỏ các vòng tròn trên các điểm dữ liệu
        dataSet.setDrawCircles(false);

        // Thêm hiệu ứng gradient cho đường biểu đồ
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(239, 154, 154));

        lineChart.setData(lineData);
        lineChart.invalidate(); // Làm mới biểu đồ để hiển thị dữ liệu

        // Tùy chỉnh giao diện cho LineChart
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.setFocusable(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setScaleEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Thiết lập giá trị tối thiểu cho trục Y
        leftAxis.setAxisMaximum(150f);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f); // Thiết lập khoảng cách tối thiểu giữa các giá trị trên trục X
    }

    private void initUi() {
        displayDashboardInfo();
        displayChartInfo();
    }

    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Dashboard");
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onStart() {
        super.onStart();
        displayChartInfo();
        displayDashboardInfo();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
        displayChartInfo();
        displayDashboardInfo();
    }
}
