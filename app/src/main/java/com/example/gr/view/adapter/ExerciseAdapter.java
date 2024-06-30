package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemExerciseBinding;
import com.example.gr.controller.listener.IOnClickExerciseItemListener;
import com.example.gr.controller.listener.IOnClickQuickAddWorkoutItemListener;
import com.example.gr.model.ActivityUser;
import com.example.gr.model.Exercise;
import com.example.gr.model.Workout;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    private final List<Exercise> mExerciseList;
    public final IOnClickExerciseItemListener iOnClickExerciseItemListener;
    public final IOnClickQuickAddWorkoutItemListener iOnClickQuickAddWorkoutItemListener;
    private final ActivityUser activityUser;

    public ExerciseAdapter(List<Exercise> mExerciseList, IOnClickExerciseItemListener iOnClickExerciseItemListener, IOnClickQuickAddWorkoutItemListener iOnClickQuickAddWorkoutItemListener) {
        this.mExerciseList = mExerciseList;
        this.iOnClickExerciseItemListener = iOnClickExerciseItemListener;
        this.activityUser = new ActivityUser();
        this.iOnClickQuickAddWorkoutItemListener = iOnClickQuickAddWorkoutItemListener;
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
        Workout workout = new Workout(exercise, exercise.getDefaultDuration(),activityUser.getWeightKg());
        holder.mItemExerciseBinding.exerciseItemCalo.setText(String.valueOf((int)exercise.getMet()*exercise.getDefaultDuration()* activityUser.getWeightKg()/60)+"KCal  -  ");
        holder.mItemExerciseBinding.exerciseItemName.setText(exercise.getName());
        holder.mItemExerciseBinding.exerciseItemMin.setText(String.valueOf(exercise.getDefaultDuration())+" phút");
        holder.mItemExerciseBinding.itemExerciseLayout.setOnClickListener(v->iOnClickExerciseItemListener.onClickItemExercise(exercise));
        holder.mItemExerciseBinding.addBtn.setOnClickListener(v->iOnClickQuickAddWorkoutItemListener.onClickQuickAdd(workout));
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
