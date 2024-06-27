package com.example.gr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.R;
import com.example.gr.databinding.ItemExerciseHistoryBinding;
import com.example.gr.listener.IOnClickHistoryItemListener;
import com.example.gr.model.RecordedWorkout;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    public final IOnClickHistoryItemListener iOnClickHistoryItemListener;
    private final List<RecordedWorkout> mWorkoutHistoryList;

    public HistoryAdapter(List<RecordedWorkout> workoutHistoryList, IOnClickHistoryItemListener iOnClickHistoryItemListener) {
        this.iOnClickHistoryItemListener = iOnClickHistoryItemListener;
        this.mWorkoutHistoryList = workoutHistoryList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseHistoryBinding itemExerciseHistoryBinding = ItemExerciseHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryAdapter.HistoryViewHolder(itemExerciseHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        RecordedWorkout recordedWorkout = mWorkoutHistoryList.get(position);
        if(recordedWorkout == null){
            return;
        }
        holder.mItemExerciseHistoryBinding.historyDate.setText(recordedWorkout.getStartTime());
        holder.mItemExerciseHistoryBinding.historyDuration.setText(String.valueOf(recordedWorkout.getDuration()));
        if(recordedWorkout.getName() != null){
            holder.mItemExerciseHistoryBinding.historyTitle.setText(recordedWorkout.getName());
        }else{
            holder.mItemExerciseHistoryBinding.historyTitle.setText(recordedWorkout.getExercise().getName());
        }
        holder.mItemExerciseHistoryBinding.itemExHistoryLayout.setOnClickListener(v->iOnClickHistoryItemListener.onClickItemHistory(recordedWorkout));
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
