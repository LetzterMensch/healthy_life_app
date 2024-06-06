package com.example.gr.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.activity.MainActivity;
import com.example.gr.activity.SearchForFoodActivity;
import com.example.gr.adapter.FoodAdapter;
import com.example.gr.constant.Constant;
import com.example.gr.constant.GlobalFunction;
import com.example.gr.databinding.FragmentDiaryBinding;
import com.example.gr.model.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.concurrent.CancellationException;

public class DiaryFragment extends BaseFragment {
    private FragmentDiaryBinding mfragmentDiaryBinding;
    private Calendar calendar;
    private int today;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mfragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false);
        this.calendar = Calendar.getInstance();
        this.today = this.calendar.get(Calendar.DAY_OF_MONTH);
        initUi();
        initListener();

        return mfragmentDiaryBinding.getRoot();
    }

    private void initListener() {
        Calendar.getInstance();
        mfragmentDiaryBinding.date.setOnClickListener(v -> openDatePicker());
        mfragmentDiaryBinding.imgNext.setOnClickListener(v -> getNextDay());
        mfragmentDiaryBinding.imgBack.setOnClickListener(v -> getPreviousDay());
    }

    private void initUi() {
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food("French fires", 1.0f, 118));
        foodList.add(new Food("Hamburger", 1.0f, 118));
        foodList.add(new Food("Brisket", 1.0f, 118));
        foodList.add(new Food("Buffalo chicken", 1.0f, 118));
        FoodAdapter mFoodAdapter = new FoodAdapter(foodList, this::goToFoodDetail);
        mfragmentDiaryBinding.layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(4);
            }
        });
        mfragmentDiaryBinding.rcvBreakfastFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvBreakfastFood.setAdapter(mFoodAdapter);
        mfragmentDiaryBinding.btnBreakfastAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(0);
            }
        });

        mfragmentDiaryBinding.rcvLunchFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvLunchFood.setAdapter(mFoodAdapter);
        mfragmentDiaryBinding.btnLunchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(1);
            }
        });

        mfragmentDiaryBinding.rcvDinnerFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvDinnerFood.setAdapter(mFoodAdapter);
        mfragmentDiaryBinding.btnDinnerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(2);
            }
        });

        mfragmentDiaryBinding.rcvSnackFood.setLayoutManager(new LinearLayoutManager(getActivity()));
        mfragmentDiaryBinding.rcvSnackFood.setAdapter(mFoodAdapter);
        mfragmentDiaryBinding.btnSnackAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSearchActivity(3);
            }
        });
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

    private void goToSearchActivity(int meal) {
        Bundle bundle = new Bundle();
        bundle.putInt("meal",meal);
        GlobalFunction.startActivity(getActivity(), SearchForFoodActivity.class, bundle);
    }

    private void getPreviousDay() {
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if(calendar.get(Calendar.DAY_OF_MONTH) == today){
            mfragmentDiaryBinding.date.setText("Hôm nay");
        }else if (calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mfragmentDiaryBinding.date.setText(dateFormat.format(calendar.getTime()));
        }
    }

    private void getNextDay() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (calendar.get(Calendar.DAY_OF_MONTH) == today - 1) {
            mfragmentDiaryBinding.date.setText("Hôm qua");
        } else if(calendar.get(Calendar.DAY_OF_MONTH) == today){
            mfragmentDiaryBinding.date.setText("Hôm nay");
        }else if (calendar.get(Calendar.DAY_OF_MONTH) == today + 1) {
            mfragmentDiaryBinding.date.setText("Ngày mai");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mfragmentDiaryBinding.date.setText(dateFormat.format(calendar.getTime()));
        }
    }

    private void openDatePicker() {
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
            this.calendar.set(i, i1, i2);
        }, year, month, day);
        datePickerDialog.show();
    }
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }
    private void reloadDiaryFragment() {
        this.calendar = Calendar.getInstance();
        mfragmentDiaryBinding.date.setText("Hôm nay");
        //TODO
        // reload data of the current date.
    }

    private void reloadDate() {

    }

    //TODO
    // write a reload data function every time user selects a different date.
    @Override
    public void onPause() {
        super.onPause();
        reloadDiaryFragment();
    }
}
