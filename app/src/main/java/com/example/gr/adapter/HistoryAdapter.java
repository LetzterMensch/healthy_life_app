package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.R;
import com.example.gr.databinding.ItemExerciseHistoryBinding;
import com.example.gr.listener.IOnClickHistoryItemListener;
import com.example.gr.model.BaseWorkout;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
//    public final IOnClickHistoryItemListener iOnClickHistoryItemListener;
    private final List<BaseWorkout> mWorkoutHistoryList;

    public HistoryAdapter(List<BaseWorkout> workoutHistoryList) {
//        this.iOnClickHistoryItemListener = iOnClickHistoryItemListener;
        this.mWorkoutHistoryList = new ArrayList<BaseWorkout>();
        this.mWorkoutHistoryList.add(workoutHistoryList.get(workoutHistoryList.size()-1));
        this.mWorkoutHistoryList.add(workoutHistoryList.get(workoutHistoryList.size()-2));

    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseHistoryBinding itemExerciseHistoryBinding = ItemExerciseHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryAdapter.HistoryViewHolder(itemExerciseHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BaseWorkout baseWorkout = mWorkoutHistoryList.get(position);
        if(baseWorkout == null){
            return;
        }
        holder.mItemExerciseHistoryBinding.historyDate.setText(baseWorkout.getLocalDateTime().toString());
        holder.mItemExerciseHistoryBinding.historyDuration.setText(String.valueOf(baseWorkout.getDuration()));
        holder.mItemExerciseHistoryBinding.historyTitle.setText(R.string.in_door_workout_title);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemExerciseHistoryBinding mItemExerciseHistoryBinding;

        public HistoryViewHolder(ItemExerciseHistoryBinding itemExerciseHistoryBinding) {
            super(itemExerciseHistoryBinding.getRoot());
            this.mItemExerciseHistoryBinding = itemExerciseHistoryBinding;
        }
    }
}
