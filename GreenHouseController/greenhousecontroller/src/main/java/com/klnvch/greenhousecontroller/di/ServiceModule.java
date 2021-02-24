package com.klnvch.greenhousecontroller.di;

import com.klnvch.greenhousecontroller.MainService;
import com.klnvch.greenhousecontroller.MyFirebaseMessagingService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract MainService contributeMainService();

    @ContributesAndroidInjector
    abstract MyFirebaseMessagingService contributeMyFirebaseMessagingService();
}
