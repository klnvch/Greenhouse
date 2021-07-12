package com.klnvch.greenhousecommon.ui.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.klnvch.greenhousecommon.R;
import com.klnvch.greenhousecommon.databinding.FragmentChartBinding;
import com.klnvch.greenhousecommon.di.Injectable;
import com.klnvch.greenhousecommon.di.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChartFragment extends Fragment implements Injectable {
    @Inject
    protected ViewModelFactory viewModelFactory;
    private FragmentChartBinding binding;
    private ChartViewModel viewModel;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ChartViewModel.class);
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::onStateChanged);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.item_time_interval) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.title_time_intervals_selector)
                    .setItems(R.array.time_intervals, (dialog, which) -> viewModel.setTimeInterval(which))
                    .show();
            return true;
        } else if (item.getItemId() == R.id.item_data) {
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void onStateChanged(ChartViewState viewState) {
        binding.chartView.clear();

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
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new CustomValueFormatter());

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

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (ChartLine line : viewState.getData()) {
            LineDataSet set = createDataSet(line.getData(), line.getName(), line.getColor());
            dataSets.add(set);
        }

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
        set.setValueTextColor(color);
        set.setLineWidth(2);
        set.setDrawCircles(true);
        set.setCircleRadius(2);
        set.setDrawValues(false);
        set.setFillAlpha(65);
        set.setFillColor(color);
        set.setHighLightColor(color);
        set.setDrawCircleHole(false);
        set.setCircleColor(color);
        return set;
    }
}
