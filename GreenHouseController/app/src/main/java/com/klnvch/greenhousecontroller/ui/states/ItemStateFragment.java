package com.klnvch.greenhousecontroller.ui.states;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.klnvch.greenhousecontroller.databinding.FragmentItemStateBinding;

public abstract class ItemStateFragment extends Fragment {
    protected FragmentItemStateBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentItemStateBinding.inflate(inflater, container, true);
        return binding.getRoot();
    }
}
