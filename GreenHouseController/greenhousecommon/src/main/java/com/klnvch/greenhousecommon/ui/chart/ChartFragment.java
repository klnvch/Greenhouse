package com.klnvch.greenhousecommon.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.klnvch.greenhousecommon.databinding.FragmentChartBinding;
import com.klnvch.greenhousecommon.di.Injectable;
import com.klnvch.greenhousecommon.di.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ChartFragment extends Fragment implements Injectable {
    @Inject
    protected ViewModelFactory viewModelFactory;
    private FragmentChartBinding binding;
    private ChartViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ChartViewModel.class);
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::onStateChanged);
    }

    private void onStateChanged(ChartViewState viewState) {
        // no description text
        binding.chartView.getDescription().setEnabled(false);

        // enable touch gestures
        binding.chartView.setTouchEnabled(true);

        binding.chartView.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        binding.chartView.setDragEnabled(true);
        binding.chartView.setScaleEnabled(true);
        binding.chartView.setDrawGridBackground(false);
        binding.chartView.setHighlightPerDragEnabled(true);

        // set an alternative background color
        binding.chartView.setBackgroundColor(Color.WHITE);
        binding.chartView.setViewPortOffsets(0f, 0f, 0f, 0f);

        XAxis xAxis = binding.chartView.getXAxis();
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

        YAxis leftAxis = binding.chartView.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(-10f);
        leftAxis.setAxisMaximum(150f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = binding.chartView.getAxisRight();
        rightAxis.setEnabled(false);

        LineDataSet batterySet = createDataSet(viewState.getBatteryLevel(), "Battery level", Color.RED);
        LineDataSet networkSet = createDataSet(viewState.getNetworkStrength(), "Network strength", Color.BLUE);
        LineDataSet temperatureSet = createDataSet(viewState.getTemperature(), "Temperature", Color.GREEN);
        LineDataSet humiditySet = createDataSet(viewState.getHumidity(), "Humidity", Color.GRAY);
        LineDataSet lightSet = createDataSet(viewState.getLightLevel(), "Light", Color.MAGENTA);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(batterySet);
        dataSets.add(networkSet);
        dataSets.add(temperatureSet);
        dataSets.add(humiditySet);
        dataSets.add(lightSet);

        LineData data = new LineData(dataSets);
        binding.chartView.setData(data);

        Legend legend = binding.chartView.getLegend();
        legend.setEnabled(true);
    }

    private LineDataSet createDataSet(List<Pair<Long, Float>> map, String label, int color) {
        List<Entry> values = new ArrayList<>();
        for (Pair<Long, Float> entry : map) {
            values.add(new Entry(entry.first, entry.second));
        }

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
}
