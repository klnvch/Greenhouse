package com.klnvch.greenhousecommon.di;

import com.klnvch.greenhousecommon.ui.action.ActionActivity;
import com.klnvch.greenhousecommon.ui.chart.ChartActivity;
import com.klnvch.greenhousecommon.ui.states.StateActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract StateActivity contributeStateActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract ChartActivity contributeChartActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract ActionActivity contributeActionActivity();
}
