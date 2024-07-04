package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.controller.listener.IOnClickDeleteFoodListener;
import com.example.gr.controller.listener.IOnClickFoodItemListener;
import com.example.gr.controller.listener.IOnClickQuickAddSubFoodListener;
import com.example.gr.databinding.ItemFoodBinding;
import com.example.gr.model.Food;

import java.util.List;

public class SubFoodAdapter extends RecyclerView.Adapter<SubFoodAdapter.SubFoodViewHolder> {
    private List<Food> mSubFoodList;
    private final IOnClickFoodItemListener iOnClickFoodItemListener;
    private final IOnClickQuickAddSubFoodListener iOnClickQuickAddSubFoodListener;
    private final IOnClickDeleteFoodListener iOnClickDeleteFoodListener;
    private final boolean isSearching;
    public SubFoodAdapter(List<Food> subFoodList, IOnClickFoodItemListener iOnClickFoodItemListener, IOnClickQuickAddSubFoodListener iOnClickQuickAddSubFoodListener, IOnClickDeleteFoodListener iOnClickDeleteFoodListener, boolean isSearching){
        this.mSubFoodList = subFoodList;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
        this.iOnClickQuickAddSubFoodListener = iOnClickQuickAddSubFoodListener;
        this.iOnClickDeleteFoodListener = iOnClickDeleteFoodListener;
        this.isSearching = isSearching;
    }

    @NonNull
    @Override
    public SubFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodBinding itemFoodBinding = ItemFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SubFoodViewHolder(itemFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubFoodViewHolder holder, int position) {
        Food food = mSubFoodList.get(position);
        if(food == null){
            return;
        }
        holder.mItemFoodBinding.foodItemName.setText(food.getName());
        holder.mItemFoodBinding.foodItemCalo.setText(String.valueOf(food.getCalories()));
        if(food.getServingSize() > 1f){
            holder.mItemFoodBinding.foodItemServingSize.setText(String.valueOf(food.getServingSize())+" g");
        }else{
            holder.mItemFoodBinding.foodItemServingSize.setText(food.getServingSize()  + " ");
        }
        holder.mItemFoodBinding.foodItemServings.setText(String.valueOf(food.getNumberOfServings()));
        holder.mItemFoodBinding.layoutItem.setOnClickListener(v->iOnClickFoodItemListener.onClickItemFood(food));
        holder.mItemFoodBinding.addBtn.setOnClickListener(v-> iOnClickQuickAddSubFoodListener.onClickQuickAdd(food));
        holder.mItemFoodBinding.deleteBtn.setOnClickListener(v->iOnClickDeleteFoodListener.onClickDeleteFoodItem(food));
        if (!isSearching){
            holder.mItemFoodBinding.addBtn.setVisibility(View.GONE);
            holder.mItemFoodBinding.deleteBtn.setVisibility(View.VISIBLE);
        }else{
            holder.mItemFoodBinding.addBtn.setVisibility(View.VISIBLE);
            holder.mItemFoodBinding.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mSubFoodList.size();
    }
    public void updateSubFoodList(List<Food> newList){
        this.mSubFoodList = newList;
        notifyDataSetChanged();
    }
    public static class SubFoodViewHolder extends RecyclerView.ViewHolder{
        private final ItemFoodBinding mItemFoodBinding;
        public  SubFoodViewHolder(ItemFoodBinding itemFoodBinding){
            super(itemFoodBinding.getRoot());
            this.mItemFoodBinding = itemFoodBinding;
        }
    }
}
