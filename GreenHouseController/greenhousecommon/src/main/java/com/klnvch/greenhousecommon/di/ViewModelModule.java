package com.klnvch.greenhousecommon.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.klnvch.greenhousecommon.ui.action.ActionViewModel;
import com.klnvch.greenhousecommon.ui.chart.ChartViewModel;
import com.klnvch.greenhousecommon.ui.states.StateViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(StateViewModel.class)
    abstract ViewModel provideStateViewModel(StateViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChartViewModel.class)
    abstract ViewModel provideChartViewModel(ChartViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ActionViewModel.class)
    abstract ViewModel provideActionViewModel(ActionViewModel viewModel);
}
