package com.example.gr.controller.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.databinding.ActivityLogWeightBinding;
import com.example.gr.model.WeightLog;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.view.adapter.WeightLogAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogWeightActivity extends BaseActivity {
    private ActivityLogWeightBinding mActivityLogWeightBinding;
    private WeightLog newWeightLog;
    private List<WeightLog> weightLogList;
    private WeightLogAdapter mWeightLogAdapter;
    private FirebaseAuth mAuth;
    private float newWeight;
    private FirebaseUser firebaseUser;
    private Calendar calendar;
    private LineChart lineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLogWeightBinding = ActivityLogWeightBinding.inflate(getLayoutInflater());
        setContentView(mActivityLogWeightBinding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        weightLogList = new ArrayList<>();
        initUI();
        initListener();
    }

    private void initUI() {
        weightLogList = LocalDatabase.getInstance(this).weightLogDAO().getAllWeightLog();
        mWeightLogAdapter = new WeightLogAdapter(weightLogList);
        mActivityLogWeightBinding.rcvWeightLog.setLayoutManager(new LinearLayoutManager(this));
        mActivityLogWeightBinding.rcvWeightLog.setAdapter(mWeightLogAdapter);
        lineChart = mActivityLogWeightBinding.weightLineChart;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        displaySelectedDateWeightLog();
        displayChartInfo();
    }

    private void initListener() {
        mActivityLogWeightBinding.imgSave.setOnClickListener(v -> logNewWeight());
        mActivityLogWeightBinding.tvDate.setOnClickListener(v -> getWeightOnDatePicker());
        mActivityLogWeightBinding.imgBack.setOnClickListener(v -> finish());
    }

    private void logNewWeight() {
        String editText = mActivityLogWeightBinding.tvWeight.getText().toString();
        if (editText.isEmpty()) {
            newWeight = 0;
        } else {
            newWeight = Float.parseFloat(editText);
        }
//        mActivityLogWeightBinding.editWeight.setText(newWeight + " kg");
        newWeightLog = LocalDatabase.getInstance(this).weightLogDAO().findWeightLogByTimeStamp(calendar.getTimeInMillis());
        if(newWeightLog != null){
            newWeightLog.setWeight(newWeight);
            newWeightLog.updateWeightLog();
        }else{
            newWeightLog = new WeightLog(firebaseUser.getUid(), newWeight, calendar.getTimeInMillis());
            newWeightLog.saveWeightLog();
        }
        weightLogList = LocalDatabase.getInstance(this).weightLogDAO().getAllWeightLog();
        displayChartInfo();
        displaySelectedDateWeightLog();
        mWeightLogAdapter.setWeightLogList(weightLogList);
    }

    private void displaySelectedDateWeightLog() {
        newWeightLog = LocalDatabase.getInstance(this).weightLogDAO().findWeightLogByTimeStamp(calendar.getTimeInMillis());
        if (newWeightLog != null) {
            mActivityLogWeightBinding.tvDate.setText(DateTimeUtils.simpleDateFormatWithWeekDays(DateTimeUtils.parseTimestampMillis(newWeightLog.getTimeStamp())));
            mActivityLogWeightBinding.tvWeight.setText(String.valueOf(newWeightLog.getWeight()));
        }else{
            mActivityLogWeightBinding.tvDate.setText(DateTimeUtils.simpleDateFormatWithWeekDays(calendar.getTime()));
        }
    }

    private void displayChartInfo() {
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
        dataSet.setDrawCircles(true);

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
        // Cập nhật đồ thị
//        lineChart.invalidate();
    }

    private void getWeightOnDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            calendar.set(i, i1, i2);
            calendar.set(Calendar.HOUR, 10);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND,0);
            mActivityLogWeightBinding.tvDate.setText(DateTimeUtils.formatDate(calendar.getTime()));
        }, year, month, day);
        c.set(Calendar.HOUR,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
//        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
//        displayFoodLogs(mDate);
        displaySelectedDateWeightLog();
    }
}
