package com.klnvch.greenhousecontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class LogsFragment extends Fragment {
    private static final String KEY_POSITION = "KEY_POSITION";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_logs, container, false);

        return rootView;
    }

    public static LogsFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        LogsFragment fragment = new LogsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
