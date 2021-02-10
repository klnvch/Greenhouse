package com.klnvch.greenhousecommon.di;

import com.klnvch.greenhousecommon.ui.states.StateActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract StateActivity contributeStateActivity();
}
