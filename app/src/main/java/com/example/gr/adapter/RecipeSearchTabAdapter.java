package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemSearchRecipeTabBinding;
import com.example.gr.listener.IOnClickFoodItemListener;
import com.example.gr.listener.IOnClickRecipeItemListener;
import com.example.gr.model.Recipe;
import com.example.gr.utils.GlideUtils;

import java.util.List;

public class RecipeSearchTabAdapter extends RecyclerView.Adapter<RecipeSearchTabAdapter.RecipeViewHolder> {
    public final IOnClickRecipeItemListener iOnClickRecipeItemListener;
    private final List<Recipe> mRecipeList;


    public RecipeSearchTabAdapter(List<Recipe> recipeList, IOnClickRecipeItemListener iOnClickRecipeItemListener) {
        this.mRecipeList = recipeList;
        this.iOnClickRecipeItemListener = iOnClickRecipeItemListener;
    }

    @NonNull
    @Override
    public RecipeSearchTabAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchRecipeTabBinding itemRecipeBinding = ItemSearchRecipeTabBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeSearchTabAdapter.RecipeViewHolder(itemRecipeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchTabAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        if (recipe == null) {
            return;
        }
        GlideUtils.loadUrl(recipe.getImage(), holder.mItemRecipeBinding.imgFood);

        holder.mItemRecipeBinding.recipeName.setText(recipe.getName());

        holder.mItemRecipeBinding.layoutItem.setOnClickListener(v->iOnClickRecipeItemListener.onClickItemRecipe(recipe));

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ItemSearchRecipeTabBinding mItemRecipeBinding;

        public RecipeViewHolder(ItemSearchRecipeTabBinding itemRecipeBinding) {
            super(itemRecipeBinding.getRoot());
            this.mItemRecipeBinding = itemRecipeBinding;
        }
    }
}

