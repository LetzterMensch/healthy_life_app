package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.activity.FoodDetailActivity;
import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.databinding.ItemSearchFoodTabBinding;
import com.example.gr.listener.IOnClickFoodItemListener;
import com.example.gr.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodSearchTabAdapter extends RecyclerView.Adapter<FoodSearchTabAdapter.FoodViewHolder> {
    public final IOnClickFoodItemListener iOnClickFoodItemListener;
    private final List<Food> mListFoods;


    public FoodSearchTabAdapter(List<Food> mListFoods, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListFoods = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodSearchTabAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchFoodTabBinding itemSearchFoodTabBinding = ItemSearchFoodTabBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodSearchTabAdapter.FoodViewHolder(itemSearchFoodTabBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchTabAdapter.FoodViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        holder.mItemSearchFoodBinding.foodItemName.setText(food.getName());
        holder.mItemSearchFoodBinding.foodItemServings.setText(String.valueOf(food.getServingSize()));
        holder.mItemSearchFoodBinding.foodItemCalo.setText(String.valueOf(food.getCalories()));

        holder.mItemSearchFoodBinding.itemInfo.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(food));
        holder.mItemSearchFoodBinding.addBtn.setOnClickListener(v-> Toast.makeText(v.getContext(), "added to diary",Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mListFoods.size();
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemSearchFoodTabBinding mItemSearchFoodBinding;

        public FoodViewHolder(ItemSearchFoodTabBinding itemSearchFoodTabBinding) {
            super(itemSearchFoodTabBinding.getRoot());
            this.mItemSearchFoodBinding = itemSearchFoodTabBinding;
        }
    }
}
