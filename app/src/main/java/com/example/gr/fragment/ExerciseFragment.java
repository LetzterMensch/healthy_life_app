package com.example.gr.fragment;

import static com.example.gr.utils.GB.toast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.ControllerApplication;
import com.example.gr.R;
import com.example.gr.activity.DiscoveryActivityV2;
import com.example.gr.activity.MainActivity;
import com.example.gr.activity.SearchForExerciseActivity;
import com.example.gr.activity.SearchForFoodActivity;
import com.example.gr.activity.WorkoutDetailActivity;
import com.example.gr.adapter.HistoryAdapter;
import com.example.gr.constant.ActivityKind;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.data.ActivitySummaryJsonSummary;
import com.example.gr.data.parser.ActivitySummaryParser;
import com.example.gr.database.DBHandler;
import com.example.gr.database.DBHelper;
import com.example.gr.database.LocalDatabase;
import com.example.gr.database.entities.BaseActivitySummary;
import com.example.gr.database.entities.BaseActivitySummaryDao;
import com.example.gr.database.entities.Device;
import com.example.gr.databinding.FragmentExerciseBinding;
import com.example.gr.device.DeviceCoordinator;
import com.example.gr.device.DeviceManager;
import com.example.gr.device.GBDevice;
import com.example.gr.device.model.RecordedDataTypes;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Diary;
import com.example.gr.model.FoodLog;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.model.Workout;
import com.example.gr.model.WorkoutItem;
import com.example.gr.utils.DateTimeUtils;
import com.example.gr.utils.GB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import de.greenrobot.dao.query.QueryBuilder;

public class ExerciseFragment extends BaseFragment {
    private FragmentExerciseBinding mFragmentExerciseBinding;
    private HistoryAdapter mHistoryAdapter;
    private List<WorkoutItem> workoutList;
    private Button addWearableBtn;
    private TextView tvWearableName;
    private TextView tvWearableStatus;
    private DeviceCoordinator deviceCoordinator;
    private DeviceManager deviceManager;
    private GBDevice device;
    private Button syncBtn;
    private Diary mDiary;
    private ActivityUser activityUser;
    private RecyclerView recyclerView;
    private int currentItemCount;
    private long lastReceiveTime = 0;
    private static final long DEBOUNCE_INTERVAL = 1000; // 500ms
    private Calendar today;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            long currentTime = System.currentTimeMillis();

            switch (Objects.requireNonNull(action)) {
                case GBDevice.ACTION_DEVICE_CHANGED:
                    device = intent.getParcelableExtra(GBDevice.EXTRA_DEVICE);
                    if (device.isBusy()) {
                    } else {
                        if (currentTime - lastReceiveTime >= DEBOUNCE_INTERVAL) {
                            lastReceiveTime = currentTime;
                            updateUIAfterShowSnackBar();
                        }
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentExerciseBinding = FragmentExerciseBinding.inflate(inflater, container, false);
        getDiary(DateTimeUtils.simpleDateFormat(Calendar.getInstance().getTime()));
        activityUser = new ActivityUser();
        today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 1);
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(GBDevice.ACTION_DEVICE_CHANGED);
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(mReceiver, filterLocal);
        initUI();
        initListener();

        return mFragmentExerciseBinding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStepsData(HashMap<String, long[]> deviceActivityHashMap) {
        long[] dailyTotals = new long[]{0, 0};
        if (deviceActivityHashMap.containsKey(device.getAddress())) {
            dailyTotals = deviceActivityHashMap.get(device.getAddress());
        }
        int steps = (int) dailyTotals[0];
        int sleep = (int) dailyTotals[1];
        System.out.println("steps : " + steps);
        mDiary.setTotalSteps(steps);
        mDiary.updateDiary();
    }

    private void populateHistoryList() {
        workoutList = new ArrayList<>();
        long now = (long) today.getTimeInMillis();
//        long now = (long) Calendar.getInstance().getTimeInMillis();
        List<Workout> normalWorkoutList = LocalDatabase.getInstance(requireActivity()).workoutDAO().
                findWorkoutByDate(DateTimeUtils.formatDate(new Date(now)));
        List<RecordedWorkout> recordedWorkoutList = LocalDatabase.getInstance(requireActivity()).recordedWorkoutDAO().
                findRecordedWorkoutByDate(DateTimeUtils.formatDate(new Date(now)));
        int listSize = Math.min(normalWorkoutList.size(), recordedWorkoutList.size());
        System.out.println("date time : " + DateTimeUtils.simpleDateFormat(today.getTime()));
        System.out.println("nomal vs recorded : " + recordedWorkoutList.size() + "/" + normalWorkoutList.size());
        while(listSize > 0){
            for (int i = 0; i < listSize; i++) {
                Workout workout = normalWorkoutList.get(i);
                RecordedWorkout recordedWorkout = recordedWorkoutList.get(i);
                if (workout.getTimestamp() > recordedWorkout.getTimeStamp()) {
                    workoutList.add(workout);
                    normalWorkoutList.remove(workout);
                } else {
                    workoutList.add(recordedWorkout);
                    recordedWorkoutList.remove(recordedWorkout);
                }
            }
            listSize = Math.min(normalWorkoutList.size(), recordedWorkoutList.size());
        }

        if (normalWorkoutList.size() > 0) {
            workoutList.addAll(normalWorkoutList);
        } else {
            workoutList.addAll(recordedWorkoutList);
        }
        System.out.println("check list size : " + workoutList.size());
    }

    private void displayHistoryList() {
        if (workoutList == null) {
            workoutList = new ArrayList<>();
        }
        populateHistoryList();
        //Initialize a list that can contains both to load both Workout and RecordedWorkout

        mHistoryAdapter = new HistoryAdapter(workoutList, this::goToWorkoutItemDetailActivity, this::goToWorkoutItemDetailActivity, this::deleteWorkout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mFragmentExerciseBinding.rcvExHistory.setLayoutManager(layoutManager);
        mFragmentExerciseBinding.rcvExHistory.setAdapter(mHistoryAdapter);
        currentItemCount = mHistoryAdapter.getItemCount();
        if (currentItemCount < 3) {
            recyclerView.getLayoutParams().height = 300;
        }else{
            recyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }
    private void deleteWorkout(Workout workout) {
        mDiary.updateDiaryAfterRemove(workout);
        displayHistoryList();
        displayWorkoutInfo();
    }
    private void initListener() {
        mFragmentExerciseBinding.btnExerciseSync.setOnClickListener(v -> {
            fetchWorkoutSummaryData();
        });
        mFragmentExerciseBinding.indoorBtn.setOnClickListener(v -> {
            goToSearchForExerciseActivity();
        });
        addWearableBtn.setOnClickListener(v -> launchDiscoveryActivity());
        syncBtn.setOnClickListener(v -> fetchData());
    }

    private void initUI() {

        addWearableBtn = mFragmentExerciseBinding.addWearableBtn;
        syncBtn = mFragmentExerciseBinding.syncBtn;
        recyclerView = mFragmentExerciseBinding.rcvExHistory;
        tvWearableName = mFragmentExerciseBinding.wearableName;
        tvWearableStatus = mFragmentExerciseBinding.wearableStatus;
        deviceManager = ((ControllerApplication) getActivity().getApplication()).getDeviceManager();
        if (deviceManager.getDevices().size() > 0) {
            device = deviceManager.getDevices().get(0);
        }
        showAddWearableInfo();
        displayWorkoutInfo();
        displayHistoryList();
    }

    private void launchDiscoveryActivity() {
        startActivity(new Intent(this.getActivity(), DiscoveryActivityV2.class));
    }

    private void goToSearchForExerciseActivity() {
        Intent intent = new Intent(requireActivity(), SearchForExerciseActivity.class);
        startActivityForResult(intent, 1);
//        Bundle bundle = new Bundle();
//        GlobalFunction.startActivity(getActivity(), SearchForExerciseActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1) {
            updateUIAfterShowSnackBar();
        }
    }

    private void goToWorkoutItemDetailActivity(RecordedWorkout recordedWorkout) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_RECORDED_WORKOUT_OBJECT, recordedWorkout);
        bundle.putParcelable(Constant.KEY_INTENT_DEVICE_OBJECT, device);
        GlobalFunction.startActivity(getActivity(), WorkoutDetailActivity.class, bundle);
    }

    private void goToWorkoutItemDetailActivity(Workout workout) {

    }

    private void displayWorkoutInfo() {
        if (mDiary != null) {
            mFragmentExerciseBinding.exCalBurnt.setText(mDiary.getBurntCalories() + "cal");
            int minute = (mDiary.getTotalWorkoutDuration() - (mDiary.getTotalWorkoutDuration() / 60) * 60);
            String min = null;
            if (minute < 10) {
                min = "0" + minute;
            } else {
                min = String.valueOf(minute);
            }
            mFragmentExerciseBinding.exCalBurntHr.setText(mDiary.getTotalWorkoutDuration() / 60 + ":" + min + " h");
            mFragmentExerciseBinding.stepsBarIndicator.setProgress(mDiary.getTotalSteps() * 100 / activityUser.getStepsGoal());
            mFragmentExerciseBinding.exSteps.setText(String.valueOf(mDiary.getTotalSteps()));
            mFragmentExerciseBinding.titleGoalSteps.setText("Mục tiêu : " + activityUser.getStepsGoal() + " bước");
        }
    }

    private void getDiary(String date) {
        mDiary = LocalDatabase.getInstance(this.requireActivity()).diaryDAO().getDiaryByDate(date);
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(this.requireActivity()).diaryDAO().insertDiary(mDiary);
        }
        System.out.println(mDiary.getDate());
    }

    private void createNewRecordedData() {
        //get BaseActivitySummaries
        try (DBHandler handler = ControllerApplication.acquireDB()) {
            BaseActivitySummaryDao summaryDao = handler.getDaoSession().getBaseActivitySummaryDao();
            Device dbDevice = DBHelper.findDevice(device, handler.getDaoSession());
            QueryBuilder<BaseActivitySummary> qb = summaryDao.queryBuilder();
            qb.where(
                    BaseActivitySummaryDao.Properties.DeviceId.eq(
                            dbDevice.getId())).orderDesc(BaseActivitySummaryDao.Properties.StartTime);
            List<BaseActivitySummary> allSummaries = new ArrayList<>();
            allSummaries.add(new BaseActivitySummary());
            allSummaries.addAll(qb.build().list());
            RecordedWorkout recordedWorkout = new RecordedWorkout();
            for (BaseActivitySummary sportitem : allSummaries) {
                if (sportitem.getStartTime() == null) continue; //first item is empty, for dashboard
                // Not parsing existed record
                if (LocalDatabase.getInstance(requireActivity()).recordedWorkoutDAO()
                        .getRecordedWorkoutByTimeStamp(sportitem.getStartTime().getTime()) != null) {
                    continue;
                }
                recordedWorkout.setName(ActivityKind.asString(sportitem.getActivityKind(), getContext()));
                recordedWorkout.setBaseActivitySummaryId(sportitem.getId());
                recordedWorkout.setActivityKind(sportitem.getActivityKind());
                recordedWorkout.setCreatedAt(DateTimeUtils.formatDate(new Date((long) sportitem.getStartTime().getTime())));
                recordedWorkout.setTimeStamp(sportitem.getStartTime().getTime());
                recordedWorkout.setEndTime(sportitem.getEndTime().getTime());
                recordedWorkout.setDuration(sportitem.getEndTime().getTime() - sportitem.getStartTime().getTime());
                System.out.println(recordedWorkout.getName());
                final ActivitySummaryParser summaryParser = device.getDeviceCoordinator().getActivitySummaryParser(device);
                final ActivitySummaryJsonSummary activitySummaryJsonSummary = new ActivitySummaryJsonSummary(summaryParser, sportitem);
                JSONObject summarySubdata = activitySummaryJsonSummary.getSummaryData();
                JSONObject data = activitySummaryJsonSummary.getSummaryGroupedList(); //get list, grouped by groups

                if (summarySubdata != null) {
                    try {
                        System.out.println(recordedWorkout.getName());
                        Iterator<String> keys = data.keys();
                        System.out.println("keys : ");
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray innerList = (JSONArray) data.get(key);
                            for (int i = 0; i < innerList.length(); i++) {
                                JSONObject innerData = innerList.getJSONObject(i);
                                String unit = innerData.getString("unit");
                                String name = innerData.getString("name");
                                if (name.equals("averageHR")) {
                                    double avgHR = innerData.getDouble("value");
                                    System.out.println("avgHR "+avgHR);
                                    recordedWorkout.setAvgHeartRate((int) avgHR);
                                }
                                if(name.equals("distanceMeters")){
                                    double distance = innerData.getDouble("value");
                                    System.out.println("distance :  "+distance);
                                    recordedWorkout.setDistance((double) (int) distance /1000);
                                }
                                if (name.equals("maxHR")) {
                                    double maxHR = innerData.getDouble("value");
                                    recordedWorkout.setMaxHeartRate((int) maxHR);
                                }
                                if (name.equals("minHR")) {
                                    double minHR = innerData.getDouble("value");
                                    recordedWorkout.setMinHeartRate((int) minHR);
                                }
                                if (name.equals("caloriesBurnt")) {
                                    double caloriesBurnt = innerData.getDouble("value");
                                    recordedWorkout.setCaloriesBurnt((int) caloriesBurnt);
                                }
                            }
                        }
                        recordedWorkout.saveRecordedWorkout(recordedWorkout);
                        displayHistoryList();
//                        System.out.println("Save record sucessfully");
                    } catch (JSONException e) {
                        GB.toast("JSON error.", Toast.LENGTH_SHORT, GB.ERROR, e);
                    }
                }
            }
        } catch (Exception e) {
            GB.toast("Error loading activity summaries.", Toast.LENGTH_SHORT, GB.ERROR, e);
        }
    }

    private String getStringResourceByName(String aString) {
        String packageName = requireActivity().getPackageName();
        int resId = requireActivity().getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            //LOG.warn("SportsActivity " + "Missing string in strings:" + aString);
            return aString;
        } else {
            return requireActivity().getString(resId);
        }
    }

    //Reset last fetching timestamp to 00:00:00 to start a new day.
    // If it was set to yesterday then reset it.
    // Else set timestamp = lastTimeStamp + 1000 (this is done in FetchSportsSummaryOperation's processBufferedData () )
    private void resetLastFetchTimeStamp() {
        //debug -> get data from 26/6
//        today.add(Calendar.DAY_OF_YEAR, -1);

//        today.set(Calendar.DATE, -1);
        if (today.getTime().getDate() != 26) {
            today.set(Calendar.DAY_OF_MONTH,26);
            today.set(Calendar.MONTH,5);
        }
        long timestamp = today.getTimeInMillis();
        SharedPreferences sharedPreferences = ControllerApplication.getDeviceSpecificSharedPrefs(device.getAddress());
        SharedPreferences.Editor editor = ControllerApplication.getDeviceSpecificSharedPrefs(device.getAddress()).edit();
        long lastTimeStamp = sharedPreferences.getLong("lastSportsActivityTimeMillis", 0);
        System.out.println("Last timestamp : " + lastTimeStamp);
        System.out.println("reset time stamp : " + timestamp);
        // it should be like this
        if (lastTimeStamp <= timestamp || today.getTimeInMillis() == 0) {
            editor.remove("lastSportsActivityTimeMillis");
            editor.putLong("lastSportsActivityTimeMillis", timestamp);
            editor.apply();
        }
        //For debug purpose only
//        editor.remove("lastSportsActivityTimeMillis");
//        editor.putLong("lastSportsActivityTimeMillis", timestamp);
//        editor.apply();
    }

    private void fetchWorkoutSummaryData() {
        if (device.isInitialized() && !device.isBusy()) {
            System.out.println("inside fetching summary data");
            resetLastFetchTimeStamp();
            showTransientSnackbar(R.string.busy_task_fetch_activity_data);
            ControllerApplication.deviceService(device).onFetchRecordedData(RecordedDataTypes.TYPE_GPS_TRACKS);
            createNewRecordedData();
        } else {
            showTransientSnackbar(R.string.controlcenter_snackbar_not_connected);
        }
    }

    private void fetchData() {
        if (device.isInitialized() && device.isConnected()) {
            showTransientSnackbar(R.string.busy_task_fetch_activity_data);
            ControllerApplication.deviceService(device).onFetchRecordedData(RecordedDataTypes.TYPE_ACTIVITY);
        } else {
            showTransientSnackbar(R.string.controlcenter_snackbar_connecting);
            ControllerApplication.deviceService(device).connect();
        }
    }

    private void showAddWearableInfo() {
        if (ControllerApplication.getPrefs().getBoolean("display_add_wearable_btn", true)) {
            addWearableBtn.setVisibility(View.VISIBLE);
            syncBtn.setVisibility(View.INVISIBLE);
            tvWearableName.setText(R.string.no_device_found);
            tvWearableStatus.setVisibility(View.GONE);
        } else {
            if (deviceManager.getDevices().size() < 1) {
                addWearableBtn.setVisibility(View.VISIBLE);
                syncBtn.setVisibility(View.GONE);
                tvWearableName.setText(R.string.no_device_found);
                tvWearableStatus.setVisibility(View.GONE);
            } else {
                addWearableBtn.setVisibility(View.GONE);
                tvWearableName.setText(device.getName());
                syncBtn.setVisibility(View.VISIBLE);
                tvWearableStatus.setVisibility(View.VISIBLE);
                if (device.isConnected() && device.isInitialized()) {
                    tvWearableStatus.setText(R.string.connected);
                } else if (device.isConnecting()) {
                    tvWearableStatus.setText(R.string.connecting);
                } else if(!device.isConnected()){
                    tvWearableStatus.setText(R.string.not_connected);
                }
            }
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
    protected void updateUIAfterShowSnackBar() {
        displayWorkoutInfo();
        displayHistoryList();
        showAddWearableInfo();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Thể dục");
        }
    }
}
