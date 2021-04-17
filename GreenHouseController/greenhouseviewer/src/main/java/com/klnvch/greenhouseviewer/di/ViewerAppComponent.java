package com.klnvch.greenhouseviewer.di;

import android.app.Application;

import com.klnvch.greenhouseviewer.ViewerApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
        modules = {AndroidInjectionModule.class, ViewerAppModule.class, ViewerActivityModule.class}
)
public interface ViewerAppComponent {
    void inject(ViewerApp app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ViewerAppComponent.Builder application(Application application);

        ViewerAppComponent build();
    }
}
