package com.example.gr.controller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gr.ControllerApplication;
import com.example.gr.controller.activity.LogWeightActivity;
import com.example.gr.controller.activity.MainActivity;
import com.example.gr.controller.activity.SearchForFoodActivity;
import com.example.gr.model.WeightLog;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.FragmentDashboardBinding;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.constant.GlobalFunction;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DashboardFragment extends BaseFragment {
    private FragmentDashboardBinding mfragmentDashboardBinding;
    private LineChart lineChart;
    private Diary mDiary;
    private FirebaseUser user;
    private ActivityUser activityUser;

    //    private ContactAdapter mContactAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        activityUser = new ActivityUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mfragmentDashboardBinding.logWeightBtn.setOnClickListener(v->goToLogWeightActivity());
        lineChart = mfragmentDashboardBinding.lineChart;
//        initListener();
        initUi();
        return mfragmentDashboardBinding.getRoot();
    }


    private void getDiary(String date) {


        mDiary = LocalDatabase.getInstance(requireActivity()).diaryDAO().getDiaryByDate(date);
        if (mDiary == null){
            ControllerApplication.getApp().getUserDatabaseReference().child(user.getUid()).child("diary").child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        mDiary = snapshot.getValue(Diary.class);
                        Diary tempDiary = LocalDatabase.getInstance(requireActivity()).diaryDAO().getDiaryByDate(date);
                        if (tempDiary != null) {
                            mDiary.setId(tempDiary.getId());
                            LocalDatabase.getInstance(requireActivity()).diaryDAO().insertDiary(mDiary);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(requireActivity()).diaryDAO().insertDiary(mDiary);
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

            mfragmentDashboardBinding.caloriesCircle.setProgress((int) (mDiary.getIntakeCalories() * 100 / (mDiary.getCaloriesGoal() + mDiary.getBurntCalories())));
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
        List<WeightLog> weightLogList = LocalDatabase.getInstance(requireActivity()).weightLogDAO().getAllWeightLog();
        List<Entry> entries = new ArrayList<>();
        for (WeightLog weightLog : weightLogList) {
            entries.add(new Entry(weightLog.getTimeStamp(), weightLog.getWeight()));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Cân nặng"); // Thẻ cho dữ liệu

        // Tùy chỉnh giao diện cho LineDataSet
        dataSet.setColor(Color.rgb(252, 83, 83)); // Color Accent
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColor(Color.RED);
        // Thay đổi màu của các giá trị
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.BLACK); // Màu của giá trị

        // Loại bỏ các vòng tròn trên các điểm dữ liệu
        dataSet.setDrawCircles(false);

        // Thêm hiệu ứng gradient cho đường biểu đồ
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(239, 154, 154));

        LineData lineData = new LineData(dataSet);


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
        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.setScaleEnabled(true);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Thiết lập giá trị tối thiểu cho trục Y
        leftAxis.setAxisMaximum(150f);
        XAxis xAxis = lineChart.getXAxis();

        if(weightLogList.size() == 1){
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(weightLogList.get(0).getTimeStamp());
        }else if (weightLogList.size() > 1){
            xAxis.setAxisMinimum(weightLogList.get(0).getTimeStamp());
            xAxis.setAxisMaximum(weightLogList.get(weightLogList.size()-1).getTimeStamp());
        }

        xAxis.setGranularity(86400000f); // Thiết lập khoảng cách tối thiểu giữa các giá trị trên trục X

        xAxis.setGranularityEnabled(true);         // Đảm bảo rằng các nhãn không bị chồng chéo

        xAxis.setDrawLimitLinesBehindData(true);


        xAxis.setLabelCount(entries.size(), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateTimeUtils.simpleDateFormatWithoutYear(
                        new Date((long) value));
            }
        });
    }

    private void goToLogWeightActivity(){
        Bundle bundle = new Bundle();
        GlobalFunction.startActivity(getActivity(), LogWeightActivity.class, bundle);
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
        activityUser = new ActivityUser();
        displayChartInfo();
        displayDashboardInfo();
    }
}
