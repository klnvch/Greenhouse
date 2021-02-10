package com.klnvch.greenhousecommon.ui.states;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.databinding.FragmentItemStateBinding;
import com.klnvch.greenhousecommon.di.Injectable;
import com.klnvch.greenhousecommon.di.ViewModelFactory;

import javax.inject.Inject;

public abstract class ItemStateFragment extends Fragment implements Injectable {
    @Inject
    protected ViewModelFactory viewModelFactory;
    private FragmentItemStateBinding binding;
    private StateViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentItemStateBinding.inflate(inflater, container, true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(StateViewModel.class);
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::onStateChanged);
    }

    private void onStateChanged(ViewState viewState) {
        if (this instanceof PhoneStateInterface) {
            ((PhoneStateInterface) this).update(viewState.getPhoneStates());
        }
        if (this instanceof ModuleStateInterface) {
            ((ModuleStateInterface) this).update(viewState.getModuleStates());
        }
    }

    protected void setImage(@DrawableRes int drawableRes) {
        binding.image.setImageResource(drawableRes);
    }

    protected void setDataMissingMessage() {
        binding.message.setText(R.string.state_message_data_missing);
    }

    protected void setMessage(@Nullable String message) {
        binding.message.setText(message);
        binding.invalidateAll();
    }

    protected void setMessage(@StringRes int message) {
        binding.message.setText(message);
    }

    /**
     * Sets red color for image.
     */
    protected void setAlert() {
        binding.image.setColorFilter(Color.RED);
    }

    /**
     * Sets default color for image.
     */
    protected void setNormal() {
        binding.image.setColorFilter(null);
    }
}
