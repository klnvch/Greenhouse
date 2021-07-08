package com.klnvch.greenhouseviewer.di;

import com.klnvch.greenhouseviewer.ui.states.StartTimeDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule extends com.klnvch.greenhousecommon.di.FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract StartTimeDialog contributeStartTimeDialog();
}
