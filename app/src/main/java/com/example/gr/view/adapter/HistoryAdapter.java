package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.ControllerApplication;
import com.example.gr.utils.constant.ActivityKind;
import com.example.gr.model.database.LocalDatabase;
import com.example.gr.databinding.ItemExerciseHistoryBinding;
import com.example.gr.controller.listener.IOnClickDeleteWorkoutItemListener;
import com.example.gr.controller.listener.IOnClickRecordedWorkoutItemListener;
import com.example.gr.controller.listener.IOnClickWorkoutItemListener;
import com.example.gr.model.Exercise;
import com.example.gr.model.RecordedWorkout;
import com.example.gr.model.Workout;
import com.example.gr.model.WorkoutItem;
import com.example.gr.utils.DateTimeUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    public final IOnClickRecordedWorkoutItemListener iOnClickRecordedWorkoutItemListener;
    public final IOnClickWorkoutItemListener iOnClickWorkoutItemListener;
    public final IOnClickDeleteWorkoutItemListener iOnClickDeleteWorkoutItemListener;
    private final List<WorkoutItem> mWorkoutHistoryList;

    public HistoryAdapter(List<WorkoutItem> workoutHistoryList, IOnClickWorkoutItemListener iOnClickWorkoutItemListener,
                          IOnClickRecordedWorkoutItemListener iOnClickRecordedWorkoutItemListener, IOnClickDeleteWorkoutItemListener iOnClickDeleteWorkoutItemListener) {
        this.iOnClickWorkoutItemListener = iOnClickWorkoutItemListener;
        this.iOnClickRecordedWorkoutItemListener = iOnClickRecordedWorkoutItemListener;
        this.mWorkoutHistoryList = workoutHistoryList;
        this.iOnClickDeleteWorkoutItemListener = iOnClickDeleteWorkoutItemListener;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseHistoryBinding itemExerciseHistoryBinding = ItemExerciseHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryAdapter.HistoryViewHolder(itemExerciseHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if (!mWorkoutHistoryList.isEmpty()) {
            WorkoutItem item = mWorkoutHistoryList.get(position);
            if (item.getType() == 1) {
                Workout workout = (Workout) mWorkoutHistoryList.get(position);
                Exercise exercise = LocalDatabase.getInstance(ControllerApplication.getContext()).exerciseDAO().getExerciseById(workout.getExerciseId());
                long durationInMillis = workout.getDurationInMillis();
                holder.mItemExerciseHistoryBinding.historyDate.setText(workout.getCreatedAt());
                holder.mItemExerciseHistoryBinding.historyDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) durationInMillis, TimeUnit.MILLISECONDS)));
                holder.mItemExerciseHistoryBinding.historyTitle.setText(exercise.getName());
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setVisibility(View.VISIBLE);
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setOnClickListener(v->{
                    deleteHistoryItem(workout);
                    iOnClickDeleteWorkoutItemListener.onClickDeleteWorkout(workout);
                });
                holder.mItemExerciseHistoryBinding.itemExHistoryLayout.setOnClickListener(v -> iOnClickWorkoutItemListener.onClickItemWorkout(workout));

            } else {
                RecordedWorkout recordedWorkout = (RecordedWorkout) mWorkoutHistoryList.get(position);
                if (recordedWorkout == null) {
                    return;
                }
                holder.mItemExerciseHistoryBinding.historyDate.setText(recordedWorkout.getCreatedAt());
                holder.mItemExerciseHistoryBinding.historyDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) recordedWorkout.getDurationInMillis(), TimeUnit.MILLISECONDS)));
                holder.mItemExerciseHistoryBinding.historyTitle.setText(recordedWorkout.getName());
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setVisibility(View.VISIBLE);
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setOnClickListener(v->{
                    deleteHistoryItem(recordedWorkout);
                    iOnClickDeleteWorkoutItemListener.onClickDeleteWorkout(recordedWorkout);
                });
                holder.mItemExerciseHistoryBinding.itemExHistoryLayout.setOnClickListener(v -> iOnClickRecordedWorkoutItemListener.onClickItemHistory(recordedWorkout));
                holder.mItemExerciseHistoryBinding.iconHistory.setImageResource(ActivityKind.getIconId(recordedWorkout.getActivityKind()));
            }
        }
    }
    public void deleteHistoryItem(WorkoutItem workoutItem){
        if(workoutItem.getType() == 1){
            LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().deleteWorkout((Workout) workoutItem);
        }else{
            LocalDatabase.getInstance(ControllerApplication.getContext()).recordedWorkoutDAO().deleteRecordedWorkout((RecordedWorkout) workoutItem);
        }
        mWorkoutHistoryList.remove(workoutItem);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mWorkoutHistoryList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemExerciseHistoryBinding mItemExerciseHistoryBinding;

        public HistoryViewHolder(ItemExerciseHistoryBinding itemExerciseHistoryBinding) {
            super(itemExerciseHistoryBinding.getRoot());
            this.mItemExerciseHistoryBinding = itemExerciseHistoryBinding;
        }
    }
}
