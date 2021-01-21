package com.klnvch.greenhousecommon.ui.chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.models.PhoneState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ChartActivity extends AppCompatActivity {
    private static final String DEVICE_ID = "test";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private LineChart chart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chart = findViewById(R.id.chart_view);

        disposable.add(AppDatabase.getInstance(this)
                .phoneStateDao()
                .getStatesAscending(DEVICE_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError));
    }

    private void onSuccess(List<PhoneState> states) {
        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);
        chart.setViewPortOffsets(0f, 0f, 0f, 0f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hou// r
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                long millis = TimeUnit.MINUTES.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(-10f);
        leftAxis.setAxisMaximum(150f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        List<Entry> batteryValues = getBatteryValues(states);
        LineDataSet batterySet = createDataSet(batteryValues, "Battery level", Color.RED);

        List<Entry> networkValues = getNetworkValues(states);
        LineDataSet networkSet = createDataSet(networkValues, "Network strength", Color.BLUE);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(batterySet);
        dataSets.add(networkSet);

        LineData data = new LineData(dataSets);
        chart.setData(data);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
    }

    private List<Entry> getBatteryValues(List<PhoneState> states) {
        List<Entry> values = new ArrayList<>();
        for (PhoneState state : states) {
            long time = TimeUnit.MILLISECONDS.toMinutes(state.getTime());
            values.add(new Entry(time, state.getBatteryLevel()));
        }
        return values;
    }

    private List<Entry> getNetworkValues(List<PhoneState> states) {
        List<Entry> values = new ArrayList<>();
        for (PhoneState state : states) {
            long time = TimeUnit.MILLISECONDS.toMinutes(state.getTime());
            values.add(new Entry(time, -state.getNetworkStrength()));
        }
        return values;
    }

    private LineDataSet createDataSet(List<Entry> values, String label, int color) {
        LineDataSet set = new LineDataSet(values, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setValueTextColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(4);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawCircleHole(false);
        return set;
    }

    private void onError(Throwable e) {
        Timber.e(e);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
