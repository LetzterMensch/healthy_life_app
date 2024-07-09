package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.ControllerApplication;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.controller.listener.IOnClickDeleteFoodLogListener;
import com.example.gr.controller.listener.IOnClickFoodLogItemListener;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.util.List;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.FoodLogViewHolder> {
    public final IOnClickFoodLogItemListener iOnClickFoodLogItemListener;
    private List<FoodLog> mFoodLogsList;
    public final IOnClickDeleteFoodLogListener iOnClickDeleteFoodLogListener;
    private int sum;

    public FoodLogAdapter(List<FoodLog> mFoodLogsList, IOnClickFoodLogItemListener iOnClickFoodLogItemListener, IOnClickDeleteFoodLogListener iOnClickDeleteFoodLogListener) {
        this.mFoodLogsList = mFoodLogsList;
        this.iOnClickFoodLogItemListener = iOnClickFoodLogItemListener;
        this.iOnClickDeleteFoodLogListener = iOnClickDeleteFoodLogListener;
        this.sum = calculateMealTotalCalories(mFoodLogsList);
    }

    @NonNull
    @Override
    public FoodLogAdapter.FoodLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodLogAdapter.FoodLogViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodLogAdapter.FoodLogViewHolder holder, int position) {
        FoodLog foodLog = mFoodLogsList.get(position);
        if (foodLog == null) {
            return;
        }
        Food food = LocalDatabase.getInstance(ControllerApplication.getContext()).foodDAO().getFoodById(foodLog.getFoodId());
        if (food == null) {
            return;
        }
        holder.mItemFoodBinding.foodItemName.setText(food.getName());
        if (food.getServingSize() != 1) {
            holder.mItemFoodBinding.foodItemServingSize.setText(food.getServingSize() + " g");
        } else {
            holder.mItemFoodBinding.foodItemServingSize.setVisibility(View.GONE);
        }
        holder.mItemFoodBinding.foodItemServings.setText(food.getNumberOfServings() + " khẩu phần");
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(foodLog.getTotalCalories()));
        holder.mItemFoodBinding.addBtn.setVisibility(View.GONE);
        holder.mItemFoodBinding.deleteBtn.setVisibility(View.VISIBLE);
        holder.mItemFoodBinding.deleteBtn.setOnClickListener(v -> {
            deleteFoodLog(foodLog, position);
            iOnClickDeleteFoodLogListener.onClickDeleteItemFoodLog(foodLog);
        });
        holder.mItemFoodBinding.layoutItem.setOnClickListener(v -> iOnClickFoodLogItemListener.onClickItemFoodLog(foodLog));
    }

    @Override
    public int getItemCount() {
        return mFoodLogsList.size();
    }

    public int getSum() {
        return sum;
    }

    private void deleteFoodLog(FoodLog foodLog, int position) {
        LocalDatabase.getInstance(ControllerApplication.getContext()).foodLogDAO().deleteFoodLog(foodLog);
        mFoodLogsList.remove(position);
        sum = calculateMealTotalCalories(mFoodLogsList);
        notifyDataSetChanged();
    }

    private int calculateMealTotalCalories(List<FoodLog> foodLogs) {
        int sum = 0;
        if (foodLogs != null) {
            for (FoodLog foodLog : foodLogs) {
                if (foodLog != null) System.out.println(foodLog.getTotalCalories());
                sum += foodLog.getTotalCalories();
            }
        }
        return sum;
    }

    public static class FoodLogViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding mItemFoodBinding;

        public FoodLogViewHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }

    // Method to update the data list
    public void setFoodLogsList(List<FoodLog> newList) {
        this.mFoodLogsList = newList;
//        notifyDataSetChanged();
    }
}
