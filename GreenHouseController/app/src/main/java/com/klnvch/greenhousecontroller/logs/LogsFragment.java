package com.klnvch.greenhousecontroller.logs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.klnvch.greenhousecontroller.R;
import com.klnvch.greenhousecontroller.databinding.FragmentLogsBinding;
import com.klnvch.greenhousecontroller.models.AppDatabase;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LogsFragment extends Fragment {
    private static final String KEY_POSITION = "KEY_POSITION";
    private static final int UPDATE_DELAY_SECONDS = 5;
    private AppDatabase db;
    private LogsAdapter adapter;
    private Disposable disposable;

    public static LogsFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        LogsFragment fragment = new LogsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = AppDatabase.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLogsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_logs, container, false);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LogsAdapter();
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        disposable = db.infoDao().getAll()
                .throttleLatest(UPDATE_DELAY_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::update,
                        e -> Timber.e("Failed to display logs: %s", e.getMessage()));
    }

    @Override
    public void onPause() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        super.onPause();
    }
}
