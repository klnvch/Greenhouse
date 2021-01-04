package com.klnvch.greenhousecontroller.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecontroller.AppSettings;
import com.klnvch.greenhousecontroller.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateTimeAndDeviceIdFragment extends ItemStateFragment {
    private static final String TIME_PATTERN = "dd/mm HH:mm";
    private static Disposable disposable;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.image.setImageResource(R.drawable.ic_baseline_router_24);

        disposable = Observable.interval(1, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (getContext() != null) {
                        AppSettings settings = AppSettings.getInstance(getContext());
                        String deviceId = settings.getDeviceId();
                        String time = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault()).format(new Date());
                        String msg = time + "\t" + deviceId;
                        binding.message.setText(msg);
                    }
                }, Timber::e);
    }

    @Override
    public void onDestroyView() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroyView();
    }
}
