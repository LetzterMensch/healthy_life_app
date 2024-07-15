package com.example.gr.controller.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.ControllerApplication;
import com.example.gr.controller.activity.FoodDetailActivity;
import com.example.gr.controller.activity.MainActivity;
import com.example.gr.controller.activity.SearchForFoodActivity;
import com.example.gr.controller.activity.StatsActivity;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.model.WeightLog;
import com.example.gr.model.Workout;
import com.example.gr.view.adapter.FoodLogAdapter;
import com.example.gr.utils.constant.Constant;
import com.example.gr.utils.constant.GlobalFunction;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.FragmentDiaryBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.FoodLog;
import com.example.gr.utils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DiaryFragment extends BaseFragment {
    private FragmentDiaryBinding mfragmentDiaryBinding;
    private Calendar calendar;
    private String mDate;
    private int today;
    private FoodLogAdapter mFoodLogAdapter;
    private List<FoodLog> breakfastLogsList;
    private List<FoodLog> lunchLogsList;
    private List<FoodLog> dinnerLogsList;
    private List<FoodLog> snackLogsList;
    private Diary mDiary;
    private FirebaseUser user;
    public static final String UPDATE_DATE = "update_date";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false);
        calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_MONTH);
        user = FirebaseAuth.getInstance().getCurrentUser();
        initUi();
        initListener();
        return mfragmentDiaryBinding.getRoot();
    }

    private void initListener() {
        Calendar.getInstance();
        mfragmentDiaryBinding.date.setOnClickListener(v -> getDiaryOnDatePicker());
        mfragmentDiaryBinding.imgNext.setOnClickListener(v -> getNextDayDiary());
        mfragmentDiaryBinding.imgBack.setOnClickListener(v -> getPreviousDayDiary());
        mfragmentDiaryBinding.finishDiary.setOnClickListener(v -> {
            createProgressDialog();
            showProgressDialog(true);
            uploadUserDataToFirebase();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog(false);
                    goToStatsActivity(mDiary);
                }
            }, 1500);

        });
    }

    private void initUi() {
        mfragmentDiaryBinding.date.setText("Hôm nay");
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    @Override
    protected void updateUIAfterShowSnackBar() {

    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, "Nhật ký dinh dưỡng");
        }
    }

    private void goToSearchActivity(int meal, Diary diary) {
        Bundle bundle = new Bundle();
        bundle.putInt("key_meal", meal);
        bundle.putSerializable(Constant.KEY_INTENT_DIARY_OBJECT, diary);
        GlobalFunction.startActivity(getActivity(), SearchForFoodActivity.class, bundle);
    }

    private void getPreviousDayDiary() {
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today) {
            mfragmentDiaryBinding.date.setText("Hôm nay");
        } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            mfragmentDiaryBinding.date.setText(DateTimeUtils.formatDate(calendar.getTime()));
        }
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    private void getNextDayDiary() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today) {
            mfragmentDiaryBinding.date.setText("Hôm nay");
        } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            mfragmentDiaryBinding.date.setText(DateTimeUtils.formatDate(calendar.getTime()));
        }
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    private void getDiaryOnDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.requireActivity(), (datePicker, i, i1, i2) -> {
//            String selectedDate = i + "-" + (i1 + 1) + "-" + i2; //yyyy-MM-dd
            calendar.set(i, i1, i2);
            if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && i2 == (today + 1)) {
                mfragmentDiaryBinding.date.setText("Ngày mai");
            } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && i2 == (today - 1)) {
                mfragmentDiaryBinding.date.setText("Hôm qua");
            } else if (Calendar.getInstance().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && i2 == today) {
                mfragmentDiaryBinding.date.setText("Hôm nay");
            } else {
                mfragmentDiaryBinding.date.setText(DateTimeUtils.formatDate(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    private void goToStatsActivity(@NonNull Diary diary) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_diary", diary);
        GlobalFunction.startActivity(getActivity(), StatsActivity.class, bundle);
    }

    private void goToFoodDetail(@NonNull FoodLog foodLog) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_EDIT_FOOD_LOG_OBJECT, foodLog);
        bundle.putInt("key_meal", foodLog.getMeal());
        bundle.putSerializable("key_diary", mDiary);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
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
        breakfastLogsList = mDiary.getBreakfastLogs();
        lunchLogsList = mDiary.getLunchLogs();
        dinnerLogsList = mDiary.getDinnerLogs();
        snackLogsList = mDiary.getSnackLogs();

    }

    private void displayDiaryInfo(String date) {
        getDiary(date);
        mfragmentDiaryBinding.calGoal.setText(String.valueOf(mDiary.getCaloriesGoal()));
        mfragmentDiaryBinding.calIntake.setText(String.valueOf(mDiary.getIntakeCalories()));
        mfragmentDiaryBinding.calBurnt.setText(String.valueOf(mDiary.getBurntCalories()));
        mfragmentDiaryBinding.calRemain.setText(String.valueOf(mDiary.getRemainingCalories()));
        mfragmentDiaryBinding.diaryCarb.setText(String.valueOf(mDiary.getIntakeCarb()) + "/" + String.valueOf(mDiary.getCarbGoal()));
        mfragmentDiaryBinding.diaryProtein.setText(String.valueOf(mDiary.getIntakeProtein()) + "/" + String.valueOf(mDiary.getProteinGoal()));
        mfragmentDiaryBinding.diaryFat.setText(String.valueOf(mDiary.getIntakeFat()) + "/" + String.valueOf(mDiary.getFatGoal()));
        if (mDiary.getCarbGoal() != 0) {
            mfragmentDiaryBinding.carbIndicator.setProgress((int) (mDiary.getIntakeCarb() * 100 / mDiary.getCarbGoal()));
            mfragmentDiaryBinding.proteinIndicator.setProgress((int) (mDiary.getIntakeProtein() * 100 / mDiary.getProteinGoal()));
            mfragmentDiaryBinding.fatIndicator.setProgress((int) (mDiary.getIntakeFat() * 100 / mDiary.getFatGoal()));
        } else {
            mfragmentDiaryBinding.carbIndicator.setProgress(0);
            mfragmentDiaryBinding.proteinIndicator.setProgress(0);
            mfragmentDiaryBinding.fatIndicator.setProgress(0);
        }
    }

    private void displayMealsTotalCalories() {

    }

    private void displayFoodLogs(String date) {
        //TODO
        // reload data of the current date and display it.
        getDiary(date);
        displayDiaryInfo(date);
        mfragmentDiaryBinding.layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(4, mDiary);
            }
        });
        mFoodLogAdapter = new FoodLogAdapter(breakfastLogsList, this::goToFoodDetail, this::deleteFoodLog);
        mfragmentDiaryBinding.calBreakfast.setText(String.valueOf(mFoodLogAdapter.getSum()));
        mfragmentDiaryBinding.rcvBreakfastFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvBreakfastFood.setAdapter(mFoodLogAdapter);
        mfragmentDiaryBinding.btnBreakfastAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(0, mDiary);
            }
        });

        mFoodLogAdapter = new FoodLogAdapter(lunchLogsList, this::goToFoodDetail, this::deleteFoodLog);
        mfragmentDiaryBinding.calLunch.setText(String.valueOf(mFoodLogAdapter.getSum()));
        mfragmentDiaryBinding.rcvLunchFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvLunchFood.setAdapter(mFoodLogAdapter);
        mfragmentDiaryBinding.btnLunchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(1, mDiary);
            }
        });

        mFoodLogAdapter = new FoodLogAdapter(dinnerLogsList, this::goToFoodDetail, this::deleteFoodLog);
        mfragmentDiaryBinding.calDinner.setText(String.valueOf(mFoodLogAdapter.getSum()));
        mfragmentDiaryBinding.rcvDinnerFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvDinnerFood.setAdapter(mFoodLogAdapter);
        mfragmentDiaryBinding.btnDinnerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(2, mDiary);
            }
        });

        mFoodLogAdapter = new FoodLogAdapter(snackLogsList, this::goToFoodDetail, this::deleteFoodLog);
        mfragmentDiaryBinding.calSnack.setText(String.valueOf(mFoodLogAdapter.getSum()));
        mfragmentDiaryBinding.rcvSnackFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvSnackFood.setAdapter(mFoodLogAdapter);
        mfragmentDiaryBinding.btnSnackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(3, mDiary);
            }
        });
    }

    private void deleteFoodLog(FoodLog foodLog) {
        mDiary.updateDiaryAfterRemove(foodLog);
        ControllerApplication.getApp()
                .getUserDatabaseReference()
                .child(user.getUid())
                .child("foodlog")
                .child(String.valueOf(foodLog.getId()))
                .setValue(null);
        displayFoodLogs(mDate);
    }

    // Upload user's
    // diary
    // foodlog
    // workout
    // recordedWorkout
    // weightlog ??
    private void uploadUserDataToFirebase() {
        DatabaseReference userReference = ControllerApplication.getApp().getUserDatabaseReference().child(user.getUid());
        // Upload diary
        userReference.child("diary").child(mDiary.getDate()).setValue(mDiary);
        // Upload foodlog
        List<FoodLog> todayFoodLogList = new ArrayList<>();
        todayFoodLogList.addAll(breakfastLogsList);
        todayFoodLogList.addAll(lunchLogsList);
        todayFoodLogList.addAll(dinnerLogsList);
        todayFoodLogList.addAll(snackLogsList);
        for (FoodLog foodlog : todayFoodLogList
        ) {
            userReference.child("foodlog").child(String.valueOf(foodlog.getId())).setValue(foodlog);
        }
        // Upload workout
        List<Workout> normalWorkoutList = LocalDatabase.getInstance(requireActivity()).workoutDAO().
                findWorkoutByDate(DateTimeUtils.formatDate(new Date(calendar.getTimeInMillis())));
        for (Workout workout : normalWorkoutList) {
            userReference.child("workout").child(String.valueOf(workout.getId())).setValue(workout);
        }
        // Upload recorded workout
        List<RecordedWorkout> recordedWorkoutList = LocalDatabase.getInstance(requireActivity()).recordedWorkoutDAO().
                findRecordedWorkoutByDate(DateTimeUtils.formatDate(new Date(calendar.getTimeInMillis())));
        for (RecordedWorkout recordedWorkout : recordedWorkoutList) {
            userReference.child("recordedworkout").child(String.valueOf(recordedWorkout.getId())).setValue(recordedWorkout);
        }
        // Upload weightLogs
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        WeightLog weightLog = LocalDatabase.getInstance(requireActivity()).weightLogDAO().findWeightLogByTimeStamp(c.getTimeInMillis());
        if (weightLog != null) {
            userReference.child("weightlog").child(String.valueOf(weightLog.getId())).setValue(weightLog);
        }
    }

    //TODO
    // write a reload data function every time user selects a different date.
    @Override
    public void onPause() {
        super.onPause();
        Intent intent = new Intent(UPDATE_DATE);
        intent.putExtra("key_calendar_time", calendar.getTimeInMillis());
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
//        mfragmentDiaryBinding.date.setText("Hôm nay");
//        calendar = Calendar.getInstance();
//        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }
//    @Override
//    public void onDestroy(){
//        mFoodLogAdapter = null;
//        super.onDestroy();
//    }
}
