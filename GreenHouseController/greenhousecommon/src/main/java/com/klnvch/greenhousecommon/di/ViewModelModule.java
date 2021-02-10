package com.klnvch.greenhousecommon.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
    abstract ViewModel provideVideoListViewModel(StateViewModel videoListViewModel);
}
