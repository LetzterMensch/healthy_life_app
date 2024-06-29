package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.ControllerApplication;
import com.example.gr.constant.ActivityKind;
import com.example.gr.database.LocalDatabase;
import com.example.gr.databinding.ItemExerciseHistoryBinding;
import com.example.gr.listener.IOnClickDeleteWorkoutListener;
import com.example.gr.listener.IOnClickRecordedWorkoutItemListener;
import com.example.gr.listener.IOnClickWorkoutItemListener;
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
    public final IOnClickDeleteWorkoutListener iOnClickDeleteWorkoutListener;
    private final List<WorkoutItem> mWorkoutHistoryList;

    public HistoryAdapter(List<WorkoutItem> workoutHistoryList, IOnClickWorkoutItemListener iOnClickWorkoutItemListener,
                          IOnClickRecordedWorkoutItemListener iOnClickRecordedWorkoutItemListener, IOnClickDeleteWorkoutListener iOnClickDeleteWorkoutListener) {
        this.iOnClickWorkoutItemListener = iOnClickWorkoutItemListener;
        this.iOnClickRecordedWorkoutItemListener = iOnClickRecordedWorkoutItemListener;
        this.mWorkoutHistoryList = workoutHistoryList;
        this.iOnClickDeleteWorkoutListener = iOnClickDeleteWorkoutListener;
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
                long durationInMilli = workout.getDuration() * 60 * 100;
                holder.mItemExerciseHistoryBinding.historyDate.setText(workout.getCreatedAt());
                holder.mItemExerciseHistoryBinding.historyDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) durationInMilli, TimeUnit.MILLISECONDS)));
                holder.mItemExerciseHistoryBinding.historyTitle.setText(exercise.getName());
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setVisibility(View.VISIBLE);
                holder.mItemExerciseHistoryBinding.deleteHistoryBtn.setOnClickListener(v->{
                    deleteWorkout(workout);
                    iOnClickDeleteWorkoutListener.onClickDeleteWorkout(workout);
                });
                holder.mItemExerciseHistoryBinding.itemExHistoryLayout.setOnClickListener(v -> iOnClickWorkoutItemListener.onClickItemWorkout(workout));

            } else {
                RecordedWorkout recordedWorkout = (RecordedWorkout) mWorkoutHistoryList.get(position);
                if (recordedWorkout == null) {
                    return;
                }
                holder.mItemExerciseHistoryBinding.historyDate.setText(recordedWorkout.getCreatedAt());
                holder.mItemExerciseHistoryBinding.historyDuration.setText(String.format("%s", DateTimeUtils.formatDurationHoursMinutes((long) recordedWorkout.getDuration(), TimeUnit.MILLISECONDS)));
                holder.mItemExerciseHistoryBinding.historyTitle.setText(recordedWorkout.getName());
                holder.mItemExerciseHistoryBinding.itemExHistoryLayout.setOnClickListener(v -> iOnClickRecordedWorkoutItemListener.onClickItemHistory(recordedWorkout));
                holder.mItemExerciseHistoryBinding.iconHistory.setImageResource(ActivityKind.getIconId(recordedWorkout.getActivityKind()));
            }
        }
    }
    public void deleteWorkout(Workout workout){
        LocalDatabase.getInstance(ControllerApplication.getContext()).workoutDAO().deleteWorkout(workout);
        mWorkoutHistoryList.remove(workout);
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
