package com.example.gr.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr.databinding.ItemExerciseBinding;
import com.example.gr.databinding.ItemWeightLogBinding;
import com.example.gr.model.WeightLog;
import com.example.gr.utils.DateTimeUtils;

import java.util.List;

public class WeightLogAdapter extends RecyclerView.Adapter<WeightLogAdapter.WeightLogViewHolder> {

    private List<WeightLog> mWeightLogList;

    public WeightLogAdapter(List<WeightLog> weightLogList) {
        this.mWeightLogList = weightLogList;
    }

    @NonNull
    @Override
    public WeightLogAdapter.WeightLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeightLogBinding itemWeightLogBinding = ItemWeightLogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WeightLogAdapter.WeightLogViewHolder(itemWeightLogBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightLogAdapter.WeightLogViewHolder holder, int position) {
        WeightLog weightLog = mWeightLogList.get(position);
        if (weightLog == null) {
            return;
        }
        holder.mItemWeightLogBinding.itemDate.setText(
                DateTimeUtils.simpleDateFormatWithWeekDays(
                        DateTimeUtils.parseTimestampMillis(weightLog.getTimeStamp())));
        holder.mItemWeightLogBinding.itemWeight.setText(weightLog.getWeight()+" kg");
    }

    @Override
    public int getItemCount() {
        return mWeightLogList.size();
    }
    public void setWeightLogList(List<WeightLog> weightLogList){
        if(this.mWeightLogList != null && !this.mWeightLogList.isEmpty()){
            this.mWeightLogList.clear();
            this.mWeightLogList.addAll(weightLogList);
        }else{
            this.mWeightLogList = weightLogList;
        }
        notifyDataSetChanged();
    }

    public static class WeightLogViewHolder extends RecyclerView.ViewHolder {
        private final ItemWeightLogBinding mItemWeightLogBinding;

        public WeightLogViewHolder(ItemWeightLogBinding itemWeightLogBinding) {
            super(itemWeightLogBinding.getRoot());
            this.mItemWeightLogBinding = itemWeightLogBinding;
        }
    }
}
