package com.klnvch.greenhouseviewer.di

import com.klnvch.greenhousecommon.di.AppModule
import com.klnvch.greenhousecommon.di.ViewModelModule
import com.klnvch.greenhousecommon.ui.action.ActionManager
import com.klnvch.greenhouseviewer.ui.action.ViewerActionManager
import dagger.Module

@Module(includes = [ViewModelModule::class])
class ViewerAppModule : AppModule() {
    override fun createActionManager(): ActionManager {
        return ViewerActionManager()
    }
}