package com.klnvch.greenhousecommon.ui.states;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.models.ModuleState;
import com.klnvch.greenhousecommon.ui.states.listeners.ModuleStateInterface;

import java.util.List;
import java.util.Locale;

public class ModuleTimeFragment extends ItemStateFragment implements ModuleStateInterface {
    private static final long THREE_HOURS_IN_SECONDS = 60 * 60 * 3;
    private static final String COLOR_RED = "'red'";
    private static final String COLOR_BLACK = "'black'";
    private static final String HTML_MSG = "Phone time: <font color=%s>%s</font>" +
            "<br>" +
            "Main module time: <font color=%s>%s</font>" +
            "<br>" +
            "Water module last access: <font color=%s>%s</font> (%d/%d)" +
            "<br>" +
            "Climate module last access: <font color=%s>%s</font>";

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
            long currentTime = System.currentTimeMillis();
            ModuleState latest = states.get(0);

            long time = latest.getTime();
            long mainTime = (latest.getMainModuleTime() - THREE_HOURS_IN_SECONDS) * 1000;
            int waterSuccessCount = latest.getWaterModuleSuccessCount();
            int waterFailCount = latest.getWaterModuleFailCount();
            long waterLastAccess = (latest.getWaterModuleLastAccess() - THREE_HOURS_IN_SECONDS) * 1000;
            long climateLastAccess = (latest.getClimateModuleLastAccess() - THREE_HOURS_IN_SECONDS) * 1000;

            String timeStr = formatTime(time);
            String timeColor = currentTime - time > ALERT_TIME_DIFFERENCE
                    ? COLOR_RED : COLOR_BLACK;
            String mainTimeStr = formatTime(mainTime);
            String mainTimeColor = currentTime - mainTime > ALERT_TIME_DIFFERENCE
                    ? COLOR_RED : COLOR_BLACK;
            String waterLastAccessStr = formatTime(waterLastAccess);
            String waterLastAccessColor = currentTime - waterLastAccess > ALERT_TIME_DIFFERENCE
                    ? COLOR_RED : COLOR_BLACK;
            String climateLastAccessStr = formatTime(climateLastAccess);
            String climateLastAccessColor = currentTime - climateLastAccess > ALERT_TIME_DIFFERENCE
                    ? COLOR_RED : COLOR_BLACK;

            String htmlText = String.format(Locale.getDefault(), HTML_MSG,
                    timeColor, timeStr,
                    mainTimeColor, mainTimeStr,
                    waterLastAccessColor, waterLastAccessStr, waterSuccessCount, waterFailCount,
                    climateLastAccessColor, climateLastAccessStr);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setMessage(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                setMessage(Html.fromHtml(htmlText));
            }
            setNormal();
        }
    }

    @Override
    protected String getDescription() {
        return "Phone Time \\ Water module time\nModule time \\ Climate module time";
    }
}
