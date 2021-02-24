package com.klnvch.greenhousecontroller.di

import com.klnvch.greenhousecommon.di.ActivityModule
import com.klnvch.greenhousecommon.di.FragmentBuildersModule
import com.klnvch.greenhousecontroller.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ControllerActivityModule : ActivityModule() {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
