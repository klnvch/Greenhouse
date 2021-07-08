package com.klnvch.greenhousecommon.ui.states;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.ModuleState;

import java.util.List;

public class ModuleTimeFragment extends ItemStateFragment implements ModuleStateInterface {
    private static final long THREE_HOURS_IN_SECONDS = 60 * 60 * 3;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            long mainModuleTime = (latest.getMainModuleTime() - THREE_HOURS_IN_SECONDS) * 1000;
            int waterModuleSuccessCount = latest.getWaterModuleSuccessCount();
            int waterModuleFailCount = latest.getWaterModuleFailCount();
            long waterModuleLastAccess = (latest.getWaterModuleLastAccess() - THREE_HOURS_IN_SECONDS) * 1000;
            long climateModuleLastAccess = (latest.getClimateModuleLastAccess() - THREE_HOURS_IN_SECONDS) * 1000;
            String timeStr = formatTime(time);
            String mainModuleTimeStr = formatTime(mainModuleTime);
            String waterModuleLastAccessStr = formatTime(waterModuleLastAccess);
            String climateModuleLastAccessStr = formatTime(climateModuleLastAccess);
            setMessage("Phone time: " + timeStr + "\n"
                    + "Main module time: " + mainModuleTimeStr + "\n"
                    + "Water module last access: " + waterModuleLastAccessStr + " (" + waterModuleSuccessCount + "/" + waterModuleFailCount + ")\n"
                    + "Climate module last access: " + climateModuleLastAccessStr);
            setNormal();
        }
    }

    @Override
    protected String getDescription() {
        return "Phone Time \\ Water module time\nModule time \\ Climate module time";
    }
}
