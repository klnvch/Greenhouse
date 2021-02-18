package com.klnvch.greenhousecontroller.di;

import android.app.Application;

import com.klnvch.greenhousecommon.di.ActivityModule;
import com.klnvch.greenhousecommon.di.AppModule;
import com.klnvch.greenhousecontroller.ControllerApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
        modules = {AndroidInjectionModule.class, AppModule.class, ServiceModule.class, ActivityModule.class}
)
public interface ControllerAppComponent {
    void inject(ControllerApp app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ControllerAppComponent.Builder application(Application application);

        ControllerAppComponent build();
    }
}
