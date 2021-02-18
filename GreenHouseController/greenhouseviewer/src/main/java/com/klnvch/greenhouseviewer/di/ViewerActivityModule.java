package com.klnvch.greenhouseviewer.di;

import com.klnvch.greenhousecommon.di.ActivityModule;
import com.klnvch.greenhousecommon.di.FragmentBuildersModule;
import com.klnvch.greenhouseviewer.ui.StateViewerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ViewerActivityModule extends ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract StateViewerActivity contributeStateViewerActivity();
}
