package com.klnvch.greenhousecommon.di;

import android.app.Application;

import com.klnvch.greenhousecommon.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
        modules = {AndroidInjectionModule.class, AppModule.class}
)
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
