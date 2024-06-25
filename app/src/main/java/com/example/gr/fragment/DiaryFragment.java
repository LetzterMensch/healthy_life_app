package com.example.gr.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.ControllerApplication;
import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.activity.MainActivity;
import com.example.gr.activity.SearchForFoodActivity;
import com.example.gr.adapter.FoodLogAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.FragmentDiaryBinding;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;
import com.example.gr.utils.DateTimeUtils;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false);
        calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_MONTH);
        initUi();
        initListener();
        return mfragmentDiaryBinding.getRoot();
    }

    private void initListener() {
        Calendar.getInstance();
        mfragmentDiaryBinding.date.setOnClickListener(v -> getDiaryOnDatePicker());
        mfragmentDiaryBinding.imgNext.setOnClickListener(v -> getNextDayDiary());
        mfragmentDiaryBinding.imgBack.setOnClickListener(v -> getPreviousDayDiary());
    }

    private void initUi() {
//        List<Food> foodList = new ArrayList<>();
//        foodList.add(new Food("French fires", 1, 118));
//        foodList.add(new Food("Hamburger", 1, 118));
//        foodList.add(new Food("Brisket", 1, 118));
//        foodList.add(new Food("Buffalo chicken", 1, 118));
//        LocalDatabase.getInstance(this.getActivity()).foodDAO().insertAll(foodList);
//        DataImporter.importFoodFromJson(ControllerApplication.getContext(),LocalDatabase.getInstance(this.getActivity()));
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
        if (calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == today) {
            mfragmentDiaryBinding.date.setText("Hôm nay");
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mfragmentDiaryBinding.date.setText(dateFormat.format(calendar.getTime()));
        }
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    private void getNextDayDiary() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == today) {
            mfragmentDiaryBinding.date.setText("Hôm nay");
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mfragmentDiaryBinding.date.setText(dateFormat.format(calendar.getTime()));
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
            String selectedDate = i + "-" + (i1 + 1) + "-" + i2; //yyyy-MM-dd
            if (i2 == (today + 1)) {
                mfragmentDiaryBinding.date.setText("Ngày mai");
            } else if (i2 == (today - 1)) {
                mfragmentDiaryBinding.date.setText("Hôm qua");
            } else if (i2 == today) {
                mfragmentDiaryBinding.date.setText("Hôm nay");
            } else {
                mfragmentDiaryBinding.date.setText(selectedDate);
            }
            calendar.set(i, i1, i2);
        }, year, month, day);
        datePickerDialog.show();
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }

    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }

    private void getDiary(String date) {
        mDiary = LocalDatabase.getInstance(this.requireActivity()).diaryDAO().getDiaryByDate(date);
        if (mDiary == null) {
            mDiary = new Diary(date);
            LocalDatabase.getInstance(this.requireActivity()).diaryDAO().insertDiary(mDiary);
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
        displayFoodLogs(mDate);
    }

    //TODO
    // write a reload data function every time user selects a different date.
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mfragmentDiaryBinding.date.setText("Hôm nay");
        calendar = Calendar.getInstance();
        mDate = DateTimeUtils.simpleDateFormat(calendar.getTime());
        displayFoodLogs(mDate);
    }
}
