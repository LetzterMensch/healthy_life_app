package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.listener.IOnClickFoodItemListener;
import com.example.gr.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    public final IOnClickFoodItemListener iOnClickFoodItemListener;
    private final List<Food> mListFoods;


    public FoodAdapter(List<Food> mListFoods, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListFoods = mListFoods;
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
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        holder.mItemFoodBinding.foodItemName.setText(food.getName());
        holder.mItemFoodBinding.foodItemServings.setText(String.valueOf(food.getServingSize()));
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(food.getCalories()));
        holder.mItemFoodBinding.addBtn.setVisibility(View.GONE);
        holder.mItemFoodBinding.layoutItem.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(food));
    }

    @Override
    public int getItemCount() {
        return mListFoods.size();
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding mItemFoodBinding;

        public FoodViewHolder(ItemFoodBinding itemFoodBinding) {
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }
}
