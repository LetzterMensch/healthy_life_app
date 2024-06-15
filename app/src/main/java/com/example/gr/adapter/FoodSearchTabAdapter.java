package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.listener.IOnClickFoodItemListener;
import com.example.gr.model.Food;

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
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodSearchTabAdapter.FoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchTabAdapter.FoodViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        holder.mItemFoodBinding.foodItemName.setText(food.getName());
        holder.mItemFoodBinding.foodItemServings.setText(String.valueOf(food.getServingSize())+"serving");
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(food.getCalories()));
        holder.mItemFoodBinding.foodItemServingSize.setText(String.valueOf(food.getServingSize()) + "g");
        holder.mItemFoodBinding.itemInfo.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(food));
        holder.mItemFoodBinding.addBtn.setOnClickListener(v-> Toast.makeText(v.getContext(), "added to diary",Toast.LENGTH_SHORT).show());
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
