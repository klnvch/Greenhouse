package com.klnvch.greenhousecommon.ui.states;

import android.content.Context;
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

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.databinding.FragmentItemStateBinding;

public abstract class ItemStateFragment extends Fragment {
    private FragmentItemStateBinding binding;
    private StateHolderInterface stateHolder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof StateHolderInterface) {
            stateHolder = ((StateHolderInterface) context);
            if (this instanceof PhoneStateInterface) {
                stateHolder.addInterface((PhoneStateInterface) this);
            }
            if (this instanceof ModuleStateInterface) {
                stateHolder.addInterface((ModuleStateInterface) this);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentItemStateBinding.inflate(inflater, container, true);
        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        if (stateHolder != null) {
            if (this instanceof PhoneStateInterface) {
                stateHolder.removeInterface((PhoneStateInterface) this);
            }
            if (this instanceof ModuleStateInterface) {
                stateHolder.removeInterface((ModuleStateInterface) this);
            }
            stateHolder = null;
        }
        super.onDetach();
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
