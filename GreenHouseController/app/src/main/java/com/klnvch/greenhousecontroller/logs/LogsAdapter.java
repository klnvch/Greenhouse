package com.klnvch.greenhousecontroller.logs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.klnvch.greenhousecontroller.databinding.ItemLogInfoBinding;
import com.klnvch.greenhousecontroller.databinding.ItemPhoneDataBinding;
import com.klnvch.greenhousecontroller.models.FireStoreData;
import com.klnvch.greenhousecontroller.models.Info;
import com.klnvch.greenhousecontroller.models.PhoneData;

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
        if (viewType == 1) {
            ItemPhoneDataBinding binding = ItemPhoneDataBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new LogViewHolder(binding);
        } else {
            ItemLogInfoBinding binding = ItemLogInfoBinding
                    .inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new LogViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        FireStoreData info = items.get(position);
        holder.fill(info);
    }

    @Override
    public int getItemViewType(int position) {
        FireStoreData info = items.get(position);
        if (info instanceof Info) {
            return 0;
        } else if (info instanceof PhoneData) {
            return 1;
        } else {
            return 2;
        }
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
        private final ViewDataBinding binding;

        private LogViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void fill(FireStoreData log) {
            if (binding instanceof ItemLogInfoBinding && log instanceof Info) {
                ((ItemLogInfoBinding) binding).setItem((Info) log);
            } else if (binding instanceof ItemPhoneDataBinding && log instanceof PhoneData) {
                ((ItemPhoneDataBinding) binding).setItem((PhoneData) log);
            }
        }
    }
}
