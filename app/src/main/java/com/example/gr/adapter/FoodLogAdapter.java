package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.listener.IOnClickFoodItemListener;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.util.List;

public class FoodLogAdapter extends RecyclerView.Adapter<FoodLogAdapter.FoodViewHolder>{
    public final IOnClickFoodItemListener iOnClickFoodItemListener;
    private final List<FoodLog> mFoodLogsList;


    public FoodLogAdapter(List<FoodLog> mFoodLogsList, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mFoodLogsList = mFoodLogsList;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodLog foodLog = mFoodLogsList.get(position);
        if (foodLog == null) {
            return;
        }
        holder.mItemFoodBinding.foodItemName.setText(foodLog.getFood().getName());
        holder.mItemFoodBinding.foodItemServings.setText(String.valueOf(foodLog.getFood().getServingSize()));
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(foodLog.getFood().getCalories()));
        holder.mItemFoodBinding.addBtn.setVisibility(View.GONE);
        holder.mItemFoodBinding.layoutItem.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(foodLog.getFood()));
    }

    @Override
    public int getItemCount() {
        return mFoodLogsList.size();
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding mItemFoodBinding;

        public FoodViewHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }
    // Method to update the data list
    public void setFoodLogsList(List<FoodLog> newList) {
        mFoodLogsList.clear();
        mFoodLogsList.addAll(newList);
//        notifyDataSetChanged();
    }
}
