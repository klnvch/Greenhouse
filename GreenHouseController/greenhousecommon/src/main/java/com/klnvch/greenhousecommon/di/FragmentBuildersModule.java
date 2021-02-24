package com.klnvch.greenhousecommon.di;

import com.klnvch.greenhousecommon.ui.chart.PhoneChartFragment;
import com.klnvch.greenhousecommon.ui.settings.DeviceIdDialog;
import com.klnvch.greenhousecommon.ui.states.BatteryStateFragment;
import com.klnvch.greenhousecommon.ui.states.BluetoothStateFragment;
import com.klnvch.greenhousecommon.ui.states.ModuleOutsideWeatherFragment;
import com.klnvch.greenhousecommon.ui.states.ModuleTimeFragment;
import com.klnvch.greenhousecommon.ui.states.ModuleWaterLevelFragment;
import com.klnvch.greenhousecommon.ui.states.SignalStateFragment;
import com.klnvch.greenhousecommon.ui.states.StateTimeAndDeviceIdFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract StateTimeAndDeviceIdFragment contributeStateTimeAndDeviceIdFragment();

    @ContributesAndroidInjector
    abstract BatteryStateFragment contributeBatteryStateFragment();

    @ContributesAndroidInjector
    abstract BluetoothStateFragment contributeBluetoothStateFragment();

    @ContributesAndroidInjector
    abstract SignalStateFragment contributeSignalStateFragment();

    @ContributesAndroidInjector
    abstract ModuleTimeFragment contributeModuleTimeFragment();

    @ContributesAndroidInjector
    abstract ModuleWaterLevelFragment contributeModuleWaterLevelFragment();

    @ContributesAndroidInjector
    abstract ModuleOutsideWeatherFragment contributeModuleOutsideWeatherFragment();

    @ContributesAndroidInjector
    abstract PhoneChartFragment contributePhoneChartFragment();

    @ContributesAndroidInjector
    abstract DeviceIdDialog contributeDeviceIdDialog();
}
