package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemExerciseBinding;
import com.example.gr.listener.IOnClickExerciseItemListener;
import com.example.gr.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    private final List<Exercise> mExerciseList;
    public final IOnClickExerciseItemListener iOnClickExerciseItemListener;

    public ExerciseAdapter(List<Exercise> mExerciseList, IOnClickExerciseItemListener iOnClickExerciseItemListener) {
        this.mExerciseList = mExerciseList;
        this.iOnClickExerciseItemListener = iOnClickExerciseItemListener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseBinding itemExerciseBinding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ExerciseViewHolder(itemExerciseBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        Exercise exercise = mExerciseList.get(position);
        if(exercise == null) {
            return;
        }
        holder.mItemExerciseBinding.exerciseItemCalo.setText(String.valueOf((int)exercise.getCaloriesBurntPerMin()*exercise.getDefaultDuration())+"KCal  -  ");
        holder.mItemExerciseBinding.exerciseItemName.setText(exercise.getName());
        holder.mItemExerciseBinding.exerciseItemMin.setText(String.valueOf(exercise.getDefaultDuration())+" phút");
        holder.mItemExerciseBinding.itemExerciseLayout.setOnClickListener(v->iOnClickExerciseItemListener.onClickItemExercise(exercise));
    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private final ItemExerciseBinding mItemExerciseBinding;
        public ExerciseViewHolder(ItemExerciseBinding itemExerciseBinding) {
            super(itemExerciseBinding.getRoot());
            this.mItemExerciseBinding = itemExerciseBinding;
        }
    }
}
