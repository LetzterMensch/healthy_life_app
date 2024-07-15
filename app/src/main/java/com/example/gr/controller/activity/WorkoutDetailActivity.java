package com.example.gr.controller.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.controller.activity.charts.DefaultChartsData;
import com.example.gr.controller.activity.charts.SampleXLabelFormatter;
import com.example.gr.controller.activity.charts.TimestampTranslation;
import com.example.gr.controller.device.DeviceCoordinator;
import com.example.gr.controller.device.GBDevice;
import com.example.gr.controller.device.GBException;
import com.example.gr.controller.fragment.AbstractActivityChartFragment;
import com.example.gr.databinding.ActivityWorkoutDetailBinding;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.model.data.sample.ActivitySample;
import com.example.gr.model.data.sample.SampleProvider;
import com.example.gr.model.database.DBHandler;
import com.example.gr.model.database.entities.AbstractActivitySample;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.HeartRateUtils;
import com.example.gr.utils.constant.ActivityKind;
import com.example.gr.utils.constant.Constant;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkoutDetailActivity extends BaseActivity  {

    ActivityWorkoutDetailBinding mActivityWorkoutDetailBinding;
    private RecordedWorkout mRecordedWorkout;
    private GBDevice mDevice;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityWorkoutDetailBinding = ActivityWorkoutDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityWorkoutDetailBinding.getRoot());
        mChart = mActivityWorkoutDetailBinding.activityChart;
        initToolbar();
        getDataIntent();
        initUI();
        try {
            initActivityChart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        initListener();
    }
    private void initUI(){
        mActivityWorkoutDetailBinding.workoutName.setText(mRecordedWorkout.getName());
        if(mRecordedWorkout.getDistance() == 0){
            mActivityWorkoutDetailBinding.workoutDistance.setVisibility(View.GONE);
        }else{
            mActivityWorkoutDetailBinding.workoutDistance.setText(mRecordedWorkout.getDistance()+  " km");
        }
        mActivityWorkoutDetailBinding.workoutCalBurnt.setText(getString(R.string.unit_calories_burnt, String.valueOf(mRecordedWorkout.getCaloriesBurnt())));
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
    private void initActivityChart() throws Exception {
        setUpDataForActivityChart();
        XAxis x = mChart.getXAxis();
        x.setDrawLabels(true);
        x.setDrawGridLines(false);
        x.setEnabled(true);
        x.setTextColor(R.color.textColorSecondary);
        x.setDrawLimitLinesBehindData(true);

        YAxis y = mChart.getAxisLeft();
        y.setDrawGridLines(false);
        y.setDrawLabels(false);
        // TODO: make fixed max value optional
        y.setAxisMaximum(1f);
        y.setAxisMinimum(0);
        y.setDrawTopYLabelEntry(false);
        y.setTextColor(R.color.textColorSecondary);

        y.setLabelCount(5);
        y.setEnabled(true);

        YAxis yAxisRight = mChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setEnabled(true);
        yAxisRight.setDrawLabels(true);
        yAxisRight.setDrawTopYLabelEntry(true);
        yAxisRight.setTextColor(R.color.textColorSecondary);

        mChart.getDescription().setEnabled(false);
    }
    private void setUpDataForActivityChart() throws Exception {
        DBHandler db = ControllerApplication.acquireDB();
        List<? extends ActivitySample> samples = getAllSamples(db, mDevice, (int) (mRecordedWorkout.getTimestamp()/1000), (int) (mRecordedWorkout.getEndTime()/1000));
        DefaultChartsData dcd = null;
        try {
            dcd = refresh( samples);
        } catch (Exception e) {
            System.out.println("Unable to get charts data right now:"+ e);
        }
        if (dcd != null) {
            mChart.setData(null); // workaround for https://github.com/PhilJay/MPAndroidChart/issues/2317
            mChart.getXAxis().setValueFormatter(dcd.getXValueFormatter());
            mChart.setData((LineData) dcd.getData());
        }
        db.close();
    }
    protected SampleProvider<? extends AbstractActivitySample> getProvider(DBHandler db, GBDevice device) {
        DeviceCoordinator coordinator = device.getDeviceCoordinator();
        return coordinator.getSampleProvider(device, db.getDaoSession());
    }
    protected List<? extends ActivitySample> getAllSamples(DBHandler db, GBDevice device, int tsFrom, int tsTo) {
        SampleProvider<? extends ActivitySample> provider = getProvider(db, device);
        return provider.getAllActivitySamples(tsFrom, tsTo);
    }
    public DefaultChartsData<LineData> refresh(List<? extends ActivitySample> samples) {

        TimestampTranslation tsTranslation = new TimestampTranslation();
        int AK_ACTIVITY_COLOR = ContextCompat.getColor(this,R.color.yellow);

        AbstractActivityChartFragment.ActivityConfig akActivity = new AbstractActivityChartFragment.ActivityConfig(ActivityKind.TYPE_ACTIVITY, getString(R.string.abstract_chart_fragment_kind_activity), AK_ACTIVITY_COLOR);

        System.out.println(": number of samples:" + samples.size());
        LineData lineData;
        if (samples.size() > 1) {


            int last_type = ActivityKind.TYPE_UNKNOWN;

            int numEntries = samples.size();
            List<Entry> activityEntries = new ArrayList<>(numEntries);
            List<Entry> heartrateEntries = new ArrayList<Entry>(numEntries);
            List<Integer> colors = new ArrayList<>(numEntries); // this is kinda inefficient...
            int lastHrSampleIndex = -1;
            HeartRateUtils heartRateUtilsInstance = HeartRateUtils.getInstance();

            for (int i = 0; i < numEntries; i++) {
                ActivitySample sample = samples.get(i);
                int type = sample.getKind();
                int ts = tsTranslation.shorten(sample.getTimestamp());

                float movement = sample.getIntensity();

                float value = movement;
                switch (type) {
                    default:
                        if (last_type != type) {
                            activityEntries.add(createLineEntry(0, ts - 1));
                        }
                        activityEntries.add(createLineEntry(value, ts));
                }
                if (sample.getKind() != ActivityKind.TYPE_NOT_WORN && heartRateUtilsInstance.isValidHeartRateValue(sample.getHeartRate())) {
                    if (lastHrSampleIndex > -1 && ts - lastHrSampleIndex > 1800*HeartRateUtils.MAX_HR_MEASUREMENTS_GAP_MINUTES) {
                        heartrateEntries.add(createLineEntry(0, lastHrSampleIndex + 1));
                        heartrateEntries.add(createLineEntry(0, ts - 1));
                    }

                    heartrateEntries.add(createLineEntry(sample.getHeartRate(), ts));
                    lastHrSampleIndex = ts;
                }
                last_type = type;
            }


            List<ILineDataSet> lineDataSets = new ArrayList<>();
            LineDataSet activitySet = createDataSet(activityEntries, akActivity.color, "Activity");
            lineDataSets.add(activitySet);

            if (heartrateEntries.size() > 0) {
                LineDataSet heartrateSet = createHeartrateSet(heartrateEntries, "Heart Rate");

                lineDataSets.add(heartrateSet);
            }
            lineData = new LineData(lineDataSets);

        } else {
            lineData = new LineData();
        }

        ValueFormatter xValueFormatter = new SampleXLabelFormatter(tsTranslation);
        return new DefaultChartsData<>(lineData, xValueFormatter);
    }
    protected Entry createLineEntry(float value, int xValue) {
        return new Entry(xValue, value);
    }
    protected LineDataSet createDataSet(List<Entry> values, Integer color, String label) {
        LineDataSet set1 = new LineDataSet(values, label);
        set1.setColor(color);

        set1.setDrawFilled(true);
        set1.setDrawCircles(false);

        set1.setFillColor(color);
        set1.setFillAlpha(255);
        set1.setDrawValues(false);

        set1.setValueTextColor(R.color.textColorPrimary);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set1;
    }
    protected LineDataSet createHeartrateSet(List<Entry> values, String label) {
        int HEARTRATE_COLOR = ContextCompat.getColor(this, R.color.red);

        LineDataSet set1 = new LineDataSet(values, label);
        set1.setLineWidth(2.2f);
        set1.setColor(HEARTRATE_COLOR);
//        set1.setDrawCubic(true);
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setCubicIntensity(0.1f);
        set1.setDrawCircles(false);

        set1.setDrawValues(true);
        set1.setValueTextColor(R.color.textColorPrimary);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
        return set1;
    }
}
