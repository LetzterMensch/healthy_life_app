/*  Copyright (C) 2015-2024 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, Dikay900, José Rebelo, ozkanpakdil, Pavel Elagin, Petr Vaněk, Q-er

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
package com.example.gr.controller.fragment;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gr.controller.activity.MainActivity;
import com.example.gr.controller.activity.charts.ChartsData;
import com.example.gr.controller.activity.charts.ChartsHost;
import com.example.gr.controller.activity.charts.DefaultChartsData;
import com.example.gr.controller.activity.charts.SleepAnalysis;
import com.example.gr.model.ActivityUser;
import com.example.gr.utils.constant.ActivityKind;
import com.example.gr.model.data.sample.ActivitySample;
import com.example.gr.databinding.FragmentSleepBinding;
import com.example.gr.controller.device.DeviceCoordinator;
import com.example.gr.controller.device.DeviceManager;
import com.example.gr.controller.device.model.RecordedDataTypes;
import com.example.gr.utils.GB;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.apache.commons.lang3.tuple.Triple;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.utils.HeartRateUtils;
import com.example.gr.controller.activity.charts.SleepAnalysis.SleepSession;
import com.example.gr.model.database.DBHandler;
import com.example.gr.controller.device.GBDevice;

import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.Prefs;


public class SleepFragment extends AbstractActivityChartFragment<SleepFragment.MyChartsData> implements ChartsHost {
    //    protected static final Logger LOG = LoggerFactory.getLogger(ActivitySleepChartFragment.class);

    public static final String STATE_START_DATE = "stateStartDate";
    public static final String STATE_END_DATE = "stateEndDate";
    private static final int MAX_SCORE = 100;

    private static final double DEEP_SLEEP_WEIGHT = 0.4;
    private static final double REM_SLEEP_WEIGHT = 0.3;
    private static final double LIGHT_SLEEP_WEIGHT = 0.2;
    private static final double WAKE_COUNT_WEIGHT = 0.1;
    private Calendar calendar;
    private int today;
    private DeviceManager mDeviceManager;
    private GBDevice mGBDevice;
    private Date mStartDate;
    private Date mEndDate;
    private SwipeRefreshLayout swipeLayout;
    private LineChart mActivityChart;
    private ActivityUser activityUser;
    private GBDevice device;
    private PieChart mSleepAmountChart;
    private TextView mSleepchartInfo;
    private TextView heartRateAverageLabel;
    private TextView intensityTotalLabel;
    private long totalDeepSleep = 0;
    private long totalLightSleep = 0;
    private long totalREMSleep = 0;
    private long totalActiveTime;
    private long totalSleepSeconds = 0;
    private int wakeUpTimes = 0;
    private int sleepScore = 0;
    private List<Date> goToBedTime = new ArrayList<>();
    private String recordedSleep;
    private FragmentSleepBinding mFragmentSleepBinding;
    Prefs prefs = ControllerApplication.getPrefs();
    private boolean CHARTS_SLEEP_RANGE_24H = prefs.getBoolean("chart_sleep_range_24h", false);
    private boolean SHOW_CHARTS_AVERAGE = prefs.getBoolean("charts_show_average", true);
    private int sleepLinesLimit = prefs.getInt("chart_sleep_lines_limit", 6);

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Objects.requireNonNull(action).equals(GBDevice.ACTION_DEVICE_CHANGED)) {
                GBDevice dev = intent.getParcelableExtra(GBDevice.EXTRA_DEVICE);
                if (dev != null && dev.isInitialized()) {
                    mGBDevice = mDeviceManager.getDevices().get(0);
                    refreshBusyState(dev);
                }
            }
        }
    };

    private void refreshBusyState(GBDevice dev) {
        if (dev.isBusy()) {
            swipeLayout.setRefreshing(true);
        } else {
            boolean wasBusy = swipeLayout.isRefreshing();
            swipeLayout.setRefreshing(false);
            if (wasBusy) {
                LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(new Intent(REFRESH));
            }
        }
        enableSwipeRefresh(true);
    }

    private void displaySleepSummaries() {
        mFragmentSleepBinding.deepSleepHr.setText(DateTimeUtils.formatDurationHoursMinutes(totalDeepSleep, TimeUnit.SECONDS));
        mFragmentSleepBinding.lightSleep.setText(DateTimeUtils.formatDurationHoursMinutes(totalLightSleep, TimeUnit.SECONDS));
        mFragmentSleepBinding.remSleep.setText(DateTimeUtils.formatDurationHoursMinutes(totalREMSleep, TimeUnit.SECONDS));

        int percent_deep_sleep = Math.round((float) (totalDeepSleep * 100) / totalSleepSeconds);
        int percent_light_sleep = Math.round((float) (totalLightSleep * 100) / totalSleepSeconds);
        int percent_rem_sleep = 100 - percent_light_sleep - percent_deep_sleep;

        mFragmentSleepBinding.deepSleepIndicator.setProgress(percent_deep_sleep);
        mFragmentSleepBinding.remSleepIndicator.setProgress(percent_deep_sleep + percent_rem_sleep);

        mFragmentSleepBinding.deepSleepPercentage.setText("Giấc ngủ sâu " + percent_deep_sleep + "%");
        mFragmentSleepBinding.lightSleepPercentage.setText("Giấc nông " + percent_light_sleep + "%");
        mFragmentSleepBinding.remSleepPercentage.setText("Giấc ngủ REM " + percent_rem_sleep + "%");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSleepBinding = FragmentSleepBinding.inflate(inflater, container, false);
        activityUser = new ActivityUser();

        //replace AbstractChartsActivity
        if (savedInstanceState != null) {
            setEndDate(new Date(savedInstanceState.getLong(STATE_END_DATE, System.currentTimeMillis())));
            setStartDate(new Date(savedInstanceState.getLong(STATE_START_DATE, DateTimeUtils.shiftByDays(getEndDate(), -1).getTime())));
        } else {
            setEndDate(new Date());
            setStartDate(DateTimeUtils.shiftByDays(getEndDate(), -1));
        }

        final IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(GBDevice.ACTION_DEVICE_CHANGED);
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(mReceiver, filterLocal);

        mActivityChart = mFragmentSleepBinding.sleepchart;

        this.calendar = Calendar.getInstance();
        this.today = this.calendar.get(Calendar.DAY_OF_MONTH);

        swipeLayout = mFragmentSleepBinding.activitySwipeLayout;
        swipeLayout.setOnRefreshListener(this::fetchRecordedData);

        ImageView mPrevButton = mFragmentSleepBinding.sleepFragmentImgBack;
        mPrevButton.setOnClickListener(v -> getPreviousDay());
        ImageView mNextButton = mFragmentSleepBinding.sleepFragmentImgNext;
        mNextButton.setOnClickListener(v -> getNextDay());

        mDeviceManager = ((ControllerApplication) requireActivity().getApplication()).getDeviceManager();
        if (mDeviceManager.getDevices().size() > 0) {
            if (mDeviceManager.getDevices().get(0).isInitialized() && mDeviceManager.getDevices().get(0).isConnected()) {
                mGBDevice = mDeviceManager.getDevices().get(0);
                enableSwipeRefresh(true);
                setupActivityChart();
                refresh();
            } else {
                fetchRecordedData();
                setupActivityChart();
            }
        }
        return mFragmentSleepBinding.getRoot();
    }

    @Override
    protected MyChartsData refreshInBackground(ChartsHost chartsHost, DBHandler db, GBDevice device) {
        List<? extends ActivitySample> samples;
        if (CHARTS_SLEEP_RANGE_24H) {
            samples = getSamples(db, device);
        } else {
            samples = getSamplesofSleep(db, device);
        }

        MySleepChartsData mySleepChartsData = refreshSleepAmounts(device, samples);

        if (!CHARTS_SLEEP_RANGE_24H) {
            if (mySleepChartsData.sleepSessions.size() > 0) {
                long tstart = mySleepChartsData.sleepSessions.get(0).getSleepStart().getTime() / 1000;
                long tend = mySleepChartsData.sleepSessions.get(mySleepChartsData.sleepSessions.size() - 1).getSleepEnd().getTime() / 1000;

                for (Iterator<ActivitySample> iterator = (Iterator<ActivitySample>) samples.iterator(); iterator.hasNext(); ) {
                    ActivitySample sample = iterator.next();
                    if (sample.getTimestamp() < tstart || sample.getTimestamp() > tend) {
                        iterator.remove();
                    }
                }
            }
        }
        DefaultChartsData<LineData> chartsData = refresh(device, samples);
        Triple<Float, Integer, Integer> hrData = calculateHrData(samples);
        Triple<Float, Float, Float> intensityData = calculateIntensityData(samples);
        return new MyChartsData(mySleepChartsData, chartsData, hrData.getLeft(), hrData.getMiddle(), hrData.getRight(), intensityData.getLeft(), intensityData.getMiddle(), intensityData.getRight());
    }


    private MySleepChartsData refreshSleepAmounts(GBDevice mGBDevice, List<? extends ActivitySample> samples) {
        SleepAnalysis sleepAnalysis = new SleepAnalysis();
        List<SleepSession> sleepSessions = sleepAnalysis.calculateSleepSessions(samples);

        PieData data = new PieData();


        final long lightSleepDuration = calculateLightSleepDuration(sleepSessions);
        final long deepSleepDuration = calculateDeepSleepDuration(sleepSessions);
        final long remSleepDuration = calculateRemSleepDuration(sleepSessions);

        final long totalSeconds = lightSleepDuration + deepSleepDuration + remSleepDuration;

        final List<PieEntry> entries = new ArrayList<>();
        final List<Integer> colors = new ArrayList<>();

        if (!sleepSessions.isEmpty()) {
            entries.add(new PieEntry(lightSleepDuration, requireActivity().getString(R.string.abstract_chart_fragment_kind_light_sleep)));
            entries.add(new PieEntry(deepSleepDuration, requireActivity().getString(R.string.abstract_chart_fragment_kind_deep_sleep)));
            colors.add(getColorFor(ActivityKind.TYPE_LIGHT_SLEEP));
            colors.add(getColorFor(ActivityKind.TYPE_DEEP_SLEEP));

            if (supportsRemSleep(mGBDevice)) {
                entries.add(new PieEntry(remSleepDuration, requireActivity().getString(R.string.abstract_chart_fragment_kind_rem_sleep)));
                colors.add(getColorFor(ActivityKind.TYPE_REM_SLEEP));
            }
        }

        String totalSleep = DateTimeUtils.formatDurationHoursMinutes(totalSeconds, TimeUnit.SECONDS);
        PieDataSet set = new PieDataSet(entries, "");
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DateTimeUtils.formatDurationHoursMinutes((long) value, TimeUnit.SECONDS);
            }
        });
        set.setColors(colors);
        set.setValueTextColor(DESCRIPTION_COLOR);
        set.setValueTextSize(13f);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        data.setDataSet(set);
        totalDeepSleep = deepSleepDuration;
        totalLightSleep = lightSleepDuration;
        totalREMSleep = remSleepDuration;
        totalSleepSeconds = totalSeconds;
        //setup sleep data statistics
        //setupLegend(pieChart);
        return new MySleepChartsData(totalSleep, data, sleepSessions);
    }

    private long calculateLightSleepDuration(List<SleepSession> sleepSessions) {
        long result = 0;
        for (SleepSession sleepSession : sleepSessions) {
            result += sleepSession.getLightSleepDuration();
        }
        return result;
    }

    private long calculateDeepSleepDuration(List<SleepSession> sleepSessions) {
        long result = 0;
        for (SleepSession sleepSession : sleepSessions) {
            result += sleepSession.getDeepSleepDuration();
        }
        return result;
    }

    private long calculateRemSleepDuration(List<SleepSession> sleepSessions) {
        long result = 0;
        for (SleepSession sleepSession : sleepSessions) {
            result += sleepSession.getRemSleepDuration();
        }
        return result;
    }

    @Override
    protected void updateChartsnUIThread(MyChartsData mcd) {
        MySleepChartsData pieData = mcd.getPieData();
        mFragmentSleepBinding.totalSleepTime.setText(pieData.getTotalSleep());
//        mSleepAmountChart.setData(pieData.getPieData());
        mActivityChart.setData(null); // workaround for https://github.com/PhilJay/MPAndroidChart/issues/2317
        mActivityChart.getXAxis().setValueFormatter(mcd.getChartsData().getXValueFormatter());
        mActivityChart.setData(mcd.getChartsData().getData());
        recordedSleep = buildYouSleptText(pieData);
//        mSleepchartInfo.setMovementMethod(new ScrollingMovementMethod());
//        int heartRateMin = mcd.getHeartRateAxisMin();
//        int heartRateMax = mcd.getHeartRateAxisMax();
//        float intensityTotal = mcd.getIntensityTotal();
        displaySleepSummaries();
        displaySleepTips();
    }

    private Triple<Float, Integer, Integer> calculateHrData(List<? extends ActivitySample> samples) {
        if (samples.toArray().length < 1) {
            return Triple.of(0f, 0, 0);
        }

        List<Integer> heartRateValues = new ArrayList<>();
        HeartRateUtils heartRateUtilsInstance = HeartRateUtils.getInstance();
        for (ActivitySample sample : samples) {
            if (sample.getKind() == ActivityKind.TYPE_LIGHT_SLEEP || sample.getKind() == ActivityKind.TYPE_DEEP_SLEEP) {
                int heartRate = sample.getHeartRate();
                if (heartRateUtilsInstance.isValidHeartRateValue(heartRate)) {
                    heartRateValues.add(heartRate);
                }
            }
        }
        if (heartRateValues.toArray().length < 1) {
            return Triple.of(0f, 0, 0);
        }

        int min = Collections.min(heartRateValues);
        int max = Collections.max(heartRateValues);
        int count = heartRateValues.toArray().length;
        float sum = calculateSumOfInts(heartRateValues);
        float average = sum / count;
        return Triple.of(average, min, max);
    }

    private float calculateIntensitySum(List<Float> samples) {
        float result = 0;
        for (Float sample : samples) {
            result += sample;
        }
        return result;
    }

    private float calculateSumOfInts(List<Integer> samples) {
        float result = 0;
        for (Integer sample : samples) {
            result += sample;
        }
        return result;
    }

    private Triple<Float, Float, Float> calculateIntensityData(List<? extends ActivitySample> samples) {
        if (samples.toArray().length < 1) {
            return Triple.of(0f, 0f, 0f);
        }

        List<Float> allIntensities = new ArrayList<>();

        for (ActivitySample s : samples) {
            if (s.getKind() == ActivityKind.TYPE_LIGHT_SLEEP || s.getKind() == ActivityKind.TYPE_DEEP_SLEEP) {
                float intensity = s.getIntensity();
                allIntensities.add(intensity);
            }
        }
        if (allIntensities.toArray().length < 1) {
            return Triple.of(0f, 0f, 0f);
        }

        Float min = Collections.min(allIntensities);
        Float max = Collections.max(allIntensities);
        Float sum = calculateIntensitySum(allIntensities);

        return Triple.of(sum, min, max);
    }

    private int calculateSleepScore(int deepSleepMinutes, int remSleepMinutes, int lightSleepMinutes, int wakeUpTimes) {
        int totalSleepMinutes = deepSleepMinutes + remSleepMinutes + lightSleepMinutes;

        // Tính tỷ lệ giấc ngủ
        double deepSleepRatio = (double) deepSleepMinutes / totalSleepMinutes;
        double remSleepRatio = (double) remSleepMinutes / totalSleepMinutes;
        double lightSleepRatio = (double) lightSleepMinutes / totalSleepMinutes;
        double wakeUpTimesRatio = (double) wakeUpTimes / totalSleepMinutes; // Số lần thức dậy mỗi giờ

        // Tính điểm số cho từng yếu tố
        double deepSleepScore = deepSleepRatio * DEEP_SLEEP_WEIGHT * MAX_SCORE;
        double remSleepScore = remSleepRatio * REM_SLEEP_WEIGHT * MAX_SCORE;
        double lightSleepScore = lightSleepRatio * LIGHT_SLEEP_WEIGHT * MAX_SCORE;
        double wakeUpTimesScore = wakeUpTimes * WAKE_COUNT_WEIGHT * MAX_SCORE;

        // Tính tổng điểm số giấc ngủ
        int sleepScore = (int) Math.round (deepSleepScore + remSleepScore + lightSleepScore - wakeUpTimesScore);
        // Hệ số điều chỉnh cho điểm ngủ
        // Lấy correctionFactor = 3.58 làm hệ số cho một giấc ngủ đạt 100 điểm như sau :
        // Tổng thời gian ngủ: 8 tiếng (480 phút)
        // Deep sleep: 120 phút (25%)
        // REM sleep: 150 phút (31.25%)
        // Light sleep: 208 phút (43.33%)
        // Số lần thức giấc: 1 lần
        //===> Thay đổi hệ số theo tổng thời gian ngủ  :
        //  dưới 3 tiếng = 0.2*h
        //  3-5 tiếng = 0.5 *h
        //  5-7 tiếng =	0.8 * h
        //  7-9 tiếng = 1.0 * h
        // trên 9 tiếng = 0.8 * h
        double totalSleepHour = (double) totalSleepMinutes /60;
        double correctionFactor = 3.58;
        if(totalSleepHour < 3){
            sleepScore = (int) Math.round(sleepScore * 0.2 * correctionFactor);
        }else if( totalSleepHour >=3 && totalSleepHour <5){
            sleepScore = (int) Math.round(sleepScore * 0.5 * correctionFactor);

        }else if(totalSleepHour >=5 && totalSleepHour <7){
            sleepScore = (int) Math.round(sleepScore * 0.8 * correctionFactor);

        } else if (totalSleepHour >= 7 && totalSleepHour < 9.5) {
            sleepScore = (int) Math.round(sleepScore * correctionFactor);

        }else if (totalSleepHour >= 9.5){
            sleepScore = (int) Math.round(sleepScore * 0.8 * correctionFactor);

        }
        // Đảm bảo điểm số không vượt quá giới hạn tối đa
        return Math.min(sleepScore, MAX_SCORE);
    }

    private void displaySleepTips() {
        mFragmentSleepBinding.sleepScore.setText(calculateSleepScore((int) (totalDeepSleep / 60), (int) (totalREMSleep / 60), (int) (totalLightSleep / 60), wakeUpTimes) + " Điểm");
        mFragmentSleepBinding.sleepTooMuch.setVisibility(View.GONE);
        mFragmentSleepBinding.notEnoughSleep.setVisibility(View.GONE);
        mFragmentSleepBinding.lateSleep.setVisibility(View.GONE);
        mFragmentSleepBinding.wakeUpManyTimes.setVisibility(View.GONE);
        mFragmentSleepBinding.notEnoughDeepSleep.setVisibility(View.GONE);
        //sleep too late
        Calendar mCalendar = Calendar.getInstance();
        int timeWentToBed;
        for (Date dateTime : goToBedTime) {
            mCalendar.setTime(dateTime);
            timeWentToBed = mCalendar.get(Calendar.HOUR_OF_DAY);
            if (timeWentToBed >= 22 || timeWentToBed <= 5) {
                mFragmentSleepBinding.lateSleepLabel.setText(getString(R.string.late_sleep_label, DateTimeUtils.timeToString(dateTime)));
                mFragmentSleepBinding.lateSleep.setVisibility(View.VISIBLE);
                break;
            }

        }
        //wake up too many times
        System.out.println("wake up times : "+wakeUpTimes);
        if (wakeUpTimes >= 2) {
            mFragmentSleepBinding.wakeUpManyTimesLabel.setText(getString(R.string.wake_up_many_times_label, String.valueOf(wakeUpTimes)));
            mFragmentSleepBinding.wakeUpManyTimesFact.setText(recordedSleep);
            mFragmentSleepBinding.wakeUpManyTimes.setVisibility(View.VISIBLE);
        }
        //sleep too much or too little
        if (totalSleepSeconds < (activityUser.getSleepDurationGoal() - 2) * 3600L) {// not enough sleep
            mFragmentSleepBinding.notEnoughSleepLabel.setText(getString(R.string.not_enough_sleep_label, DateTimeUtils.formatDurationHoursMinutes(totalSleepSeconds, TimeUnit.SECONDS)));
            mFragmentSleepBinding.notEnoughSleep.setVisibility(View.VISIBLE);
        } else if (totalSleepSeconds > 9.5 * 3600L) {//sleep too much
            mFragmentSleepBinding.sleepTooMuch.setVisibility(View.VISIBLE);
            return;
        }
        //not enough deep sleep
        if (totalDeepSleep < 3700) {
            mFragmentSleepBinding.notEnoughDeepSleep.setVisibility(View.VISIBLE);
        }

    }

    private String buildYouSleptText(MySleepChartsData pieData) {
        final StringBuilder result = new StringBuilder();
        long lastWakeupTime = 0;
        goToBedTime.clear();
        if (pieData.getSleepSessions().isEmpty()) {
            result.append(requireActivity().getString(R.string.you_did_not_sleep));
        } else {
            for (SleepSession sleepSession : pieData.getSleepSessions()) {
                if (result.length() > 0) {
                    result.append('\n');
                    if (sleepSession.getSleepStart().getTime() - lastWakeupTime < 60 * 60 * 2) {
                        wakeUpTimes++;
                    }
                }
                goToBedTime.add(sleepSession.getSleepStart());
                result.append(requireActivity().getString(
                        R.string.you_slept,
                        DateTimeUtils.timeToString(sleepSession.getSleepStart()),
                        DateTimeUtils.timeToString(sleepSession.getSleepEnd())));
                lastWakeupTime = sleepSession.getSleepEnd().getTime();

            }
        }
        return result.toString();
    }

    @Override
    public String getTitle() {
        return getString(R.string.sleepchart_your_sleep);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ChartsHost.REFRESH)) {
            // TODO: use LimitLines to visualize smart alarms?
            refresh();
        } else {
            super.onReceive(context, intent);
        }
    }

//    private void setupSleepAmountChart() {
//        mSleepAmountChart.setBackgroundColor(BACKGROUND_COLOR);
//        mSleepAmountChart.getDescription().setTextColor(DESCRIPTION_COLOR);
//        mSleepAmountChart.setEntryLabelColor(DESCRIPTION_COLOR);
//        mSleepAmountChart.getDescription().setText("");
////        mSleepAmountChart.getDescription().setNoDataTextDescription("");
//        mSleepAmountChart.setNoDataText("");
//        mSleepAmountChart.getLegend().setEnabled(false);
//    }

    private void setupActivityChart() {
        mActivityChart.setBackgroundColor(BACKGROUND_COLOR);
        mActivityChart.getDescription().setTextColor(DESCRIPTION_COLOR);
        configureBarLineChartDefaults(mActivityChart);

        XAxis x = mActivityChart.getXAxis();
        x.setDrawLabels(true);
        x.setDrawGridLines(false);
        x.setEnabled(true);
        x.setTextColor(CHART_TEXT_COLOR);
        x.setDrawLimitLinesBehindData(true);

        YAxis y = mActivityChart.getAxisLeft();
        y.setDrawGridLines(false);
        y.setDrawLabels(false);
        // TODO: make fixed max value optional
        y.setAxisMaximum(1f);
        y.setAxisMinimum(0);
        y.setDrawTopYLabelEntry(false);
        y.setTextColor(CHART_TEXT_COLOR);

        y.setLabelCount(5);
        y.setEnabled(true);

        YAxis yAxisRight = mActivityChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setEnabled(true);
        yAxisRight.setDrawLabels(true);
        yAxisRight.setDrawTopYLabelEntry(true);
        yAxisRight.setTextColor(CHART_TEXT_COLOR);
    }

    @Override
    protected void setupLegend(Chart<?> chart) {
        List<LegendEntry> legendEntries = new ArrayList<>(3);
        LegendEntry lightSleepEntry = new LegendEntry();
        lightSleepEntry.label = akLightSleep.label;
        lightSleepEntry.formColor = akLightSleep.color;
        legendEntries.add(lightSleepEntry);

        LegendEntry deepSleepEntry = new LegendEntry();
        deepSleepEntry.label = akDeepSleep.label;
        deepSleepEntry.formColor = akDeepSleep.color;
        legendEntries.add(deepSleepEntry);

        LegendEntry remSleepEntry = new LegendEntry();
        remSleepEntry.label = akRemSleep.label;
        remSleepEntry.formColor = akRemSleep.color;
        legendEntries.add(remSleepEntry);

        LegendEntry hrEntry = new LegendEntry();
        hrEntry.label = HEARTRATE_LABEL;
        hrEntry.formColor = HEARTRATE_COLOR;
        legendEntries.add(hrEntry);

        LegendEntry activityEntry = new LegendEntry();
        activityEntry.label = akActivity.label;
        activityEntry.formColor = akActivity.color;
        legendEntries.add(activityEntry);
        chart.getLegend().setCustom(legendEntries);
        chart.getLegend().setTextColor(LEGEND_TEXT_COLOR);
    }

    @Override
    protected List<? extends ActivitySample> getSamples(DBHandler db, GBDevice device, int tsFrom, int tsTo) {
// temporary fix for totally wrong sleep amounts
//        return super.getSleepSamples(db, device, tsFrom, tsTo);
        return super.getAllSamples(db, device, tsFrom, tsTo);
    }

    @Override
    protected void renderCharts() {
        mActivityChart.animateX(ANIM_TIME, Easing.EaseInOutQuart);
//        mSleepAmountChart.invalidate();
    }

    private static class MySleepChartsData extends ChartsData {
        private String totalSleep;
        private final PieData pieData;
        private final List<SleepSession> sleepSessions;

        public MySleepChartsData(String totalSleep, PieData pieData, List<SleepSession> sleepSessions) {
            this.totalSleep = totalSleep;
            this.pieData = pieData;
            this.sleepSessions = sleepSessions;
        }

        public PieData getPieData() {
            return pieData;
        }

        public CharSequence getTotalSleep() {
            return totalSleep;
        }

        public List<SleepSession> getSleepSessions() {
            return sleepSessions;
        }
    }

    protected static class MyChartsData extends ChartsData {
        private final DefaultChartsData<LineData> chartsData;
        private final MySleepChartsData pieData;
        private final float heartRateAverage;
        private int heartRateAxisMax;
        private int heartRateAxisMin;
        private float intensityAxisMax;
        private float intensityAxisMin;
        private float intensityTotal;

        public MyChartsData(MySleepChartsData pieData, DefaultChartsData<LineData> chartsData, float heartRateAverage, int heartRateAxisMin, int heartRateAxisMax, float intensityTotal, float intensityAxisMin, float intensityAxisMax) {
            this.pieData = pieData;
            this.chartsData = chartsData;
            this.heartRateAverage = heartRateAverage;
            this.heartRateAxisMax = heartRateAxisMax;
            this.heartRateAxisMin = heartRateAxisMin;
            this.intensityTotal = intensityTotal;
            this.intensityAxisMin = intensityAxisMin;
            this.intensityAxisMax = intensityAxisMax;
        }

        public MySleepChartsData getPieData() {
            return pieData;
        }

        public DefaultChartsData<LineData> getChartsData() {
            return chartsData;
        }

        public float getHeartRateAverage() {
            return heartRateAverage;
        }

        public int getHeartRateAxisMax() {
            return heartRateAxisMax;
        }

        public int getHeartRateAxisMin() {
            return heartRateAxisMin;
        }

        public float getIntensityAxisMax() {
            return intensityAxisMax;
        }

        public float getIntensityAxisMin() {
            return intensityAxisMin;
        }

        public float getIntensityTotal() {
            return intensityTotal;
        }
    }

    @Override
    public GBDevice getDevice() {
        return mGBDevice;
    }

    @Override
    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    @Override
    public Date getStartDate() {
        return mStartDate;
    }

    @Override
    public Date getEndDate() {
        return mEndDate;
    }

    @Override
    public void enableSwipeRefresh(boolean enable) {
        swipeLayout.setEnabled(enable && allowRefresh());
    }

    private void getPreviousDay() {
        handleButtonClicked(DATE_PREV_DAY);

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date to = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date from = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        mFragmentSleepBinding.date.setText(DateTimeUtils.formatDateRange(from,to));
//        fetchRecordedData();
    }

    private void getNextDay() {

        if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return;
        }
        handleButtonClicked(DATE_NEXT_DAY);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date to = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date from = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        mFragmentSleepBinding.date.setText(DateTimeUtils.formatDateRange(from,to));
//        fetchRecordedData();
    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.requireActivity(), (datePicker, i, i1, i2) -> {
            String selectedDate = i + "-" + (i1 + 1) + "-" + i2; //yyyy-MM-dd
            if (i2 == (today + 1)) {
                mFragmentSleepBinding.date.setText("Ngày mai");
            } else if (i2 == (today - 1)) {
                mFragmentSleepBinding.date.setText("Hôm qua");
            } else if (i2 == today) {
                mFragmentSleepBinding.date.setText("Hôm nay");
            } else {
                mFragmentSleepBinding.date.setText(selectedDate);
            }
            this.calendar.set(i, i1, i2);
        }, year, month, day);
        datePickerDialog.show();
    }

    protected int getRecordedDataType() {
        return RecordedDataTypes.TYPE_ACTIVITY;
    }

    protected boolean allowRefresh() {
        final DeviceCoordinator coordinator = getDevice().getDeviceCoordinator();
        return coordinator.allowFetchActivityData(getDevice()) && supportsRefresh();
    }

    protected boolean supportsRefresh() {
        final DeviceCoordinator coordinator = getDevice().getDeviceCoordinator();
        return coordinator.supportsActivityDataFetching();
    }

    private void fetchRecordedData() {
        if (getDevice() != null) {
            if (getDevice().isInitialized()) {
                ControllerApplication.deviceService(getDevice()).onFetchRecordedData(getRecordedDataType());
            }
        } else {
            swipeLayout.setRefreshing(false);
            if (mDeviceManager.getDevices().size() > 0) {
                mGBDevice = mDeviceManager.getDevices().get(0);
                if (mGBDevice.isInitialized()) {
                    enableSwipeRefresh(true);
                    setupActivityChart();
                    refresh();
                }
                return;
            }
            GB.toast(this.getContext(), getString(R.string.device_not_connected), Toast.LENGTH_SHORT, GB.ERROR);
        }
    }

    private void handleButtonClicked(final String action) {
        LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(new Intent(action));
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(STATE_END_DATE, getEndDate().getTime());
        outState.putLong(STATE_START_DATE, getStartDate().getTime());
    }

    @Override
    public void onViewStateRestored(final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            setEndDate(new Date(savedInstanceState.getLong(STATE_END_DATE, System.currentTimeMillis())));
            setStartDate(new Date(savedInstanceState.getLong(STATE_START_DATE, DateTimeUtils.shiftByDays(getEndDate(), -1).getTime())));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onMadeVisibleInActivityInternal();
    }

    @Override
    protected void updateUIAfterShowSnackBar() {
        System.out.println("Done updating !");
    }

    @Override
    public void onStop() {
        super.onStop();
        onMadeInvisibleInActivity();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mReceiver);

    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Giấc ngủ");
        }
    }
}
