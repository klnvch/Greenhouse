package com.klnvch.greenhousecontroller.logs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klnvch.greenhousecontroller.databinding.ItemLogInfoBinding;
import com.klnvch.greenhousecontroller.models.FireStoreData;
import com.klnvch.greenhousecontroller.models.Info;

import java.util.ArrayList;
import java.util.List;

class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {
    private List<Info> items = new ArrayList<>();

    LogsAdapter() {
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLogInfoBinding binding = ItemLogInfoBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Info info = items.get(position);
        holder.binding.setInfo(info);
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

    void update(List<Info> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private ItemLogInfoBinding binding;

        private LogViewHolder(ItemLogInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
