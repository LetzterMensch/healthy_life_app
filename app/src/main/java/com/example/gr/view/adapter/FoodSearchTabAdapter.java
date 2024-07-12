package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.controller.listener.IOnClickFoodItemListener;
import com.example.gr.controller.listener.IOnClickQuickAddFoodItemListener;
import com.example.gr.model.Diary;
import com.example.gr.model.Food;
import com.example.gr.model.FoodLog;

import java.util.List;

public class FoodSearchTabAdapter extends RecyclerView.Adapter<FoodSearchTabAdapter.FoodViewHolder> {
    public final IOnClickFoodItemListener iOnClickFoodItemListener;
    private final IOnClickQuickAddFoodItemListener iOnClickQuickAddFoodItemListener;
    private List<Food> mListFoods;
    private int meal;
    private final Diary mDiary;

    public FoodSearchTabAdapter(List<Food> mListFoods, int meal, Diary mDiary, IOnClickFoodItemListener iOnClickFoodItemListener, IOnClickQuickAddFoodItemListener iOnClickQuickAddFoodItemListener) {
        this.mListFoods = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
        this.iOnClickQuickAddFoodItemListener = iOnClickQuickAddFoodItemListener;
        this.meal = meal;
        this.mDiary = mDiary;
    }

    @NonNull
    @Override
    public FoodSearchTabAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodSearchTabAdapter.FoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchTabAdapter.FoodViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        FoodLog foodLog = new FoodLog(food,meal,food.getNumberOfServings(),mDiary.getId());
        holder.mItemFoodBinding.foodItemName.setText(food.getName());
        holder.mItemFoodBinding.foodItemServings.setText(food.getNumberOfServings() + " khẩu phần");
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(food.getCalories()));
        if (food.getServingSize() != 1) {
            holder.mItemFoodBinding.foodItemServingSize.setText(food.getServingSize() + " g");
        }else{
            holder.mItemFoodBinding.foodItemServingSize.setVisibility(View.GONE);
        }
        holder.mItemFoodBinding.itemInfo.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(food));
        holder.mItemFoodBinding.addBtn.setOnClickListener(v-> iOnClickQuickAddFoodItemListener.onClickQuickAdd(foodLog));
    }

    @Override
    public int getItemCount() {
        return mListFoods.size();
    }
    public void addFoodList(List<Food> newFoodList) {
        int startPosition = mListFoods.size();
        mListFoods.addAll(newFoodList);
        notifyItemRangeInserted(startPosition, newFoodList.size());
    }
    public void setmListFoods(List<Food> newFoodList){
        this.mListFoods = newFoodList;
        notifyDataSetChanged();
    }
    public void setMeal(int meal){
        this.meal = meal;
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding mItemFoodBinding;

        public FoodViewHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }
}
