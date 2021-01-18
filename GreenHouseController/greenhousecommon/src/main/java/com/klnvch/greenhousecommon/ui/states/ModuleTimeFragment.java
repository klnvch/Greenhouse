package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.ModuleState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModuleTimeFragment extends ItemStateFragment implements ModuleStateInterface {
    private static final String TIME_PATTERN = "dd/mm HH:mm";
    private SimpleDateFormat sdf = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sdf = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        setImage(R.drawable.ic_baseline_access_time_24);
    }

    @Override
    public void update(List<ModuleState> states) {
        if (states == null || states.size() == 0) {
            setDataMissingMessage();
            setAlert();
        } else {
            ModuleState latest = states.get(0);
            long time = latest.getTime();
            String timeStr = sdf.format(new Date(time));
            setMessage(timeStr);
            setNormal();
        }
    }
}
