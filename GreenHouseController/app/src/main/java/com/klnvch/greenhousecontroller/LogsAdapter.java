package com.klnvch.greenhousecontroller;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klnvch.greenhousecontroller.databinding.ItemLogBinding;
import com.klnvch.greenhousecontroller.models.FireStoreData;

import java.util.ArrayList;
import java.util.List;

class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {
    private List<FireStoreData> items = new ArrayList<>();

    LogsAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLogBinding binding = ItemLogBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        FireStoreData log = items.get(position);
        holder.binding.setData(log);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        FireStoreData log = items.get(position);
        return log.getId().hashCode();
    }

    void update(List<FireStoreData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private ItemLogBinding binding;

        private LogViewHolder(ItemLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
