package com.klnvch.greenhousecontroller.di

import com.klnvch.greenhousecommon.db.ActionDao
import com.klnvch.greenhousecommon.di.AppModule
import com.klnvch.greenhousecommon.di.ViewModelModule
import com.klnvch.greenhousecommon.ui.action.ActionManager
import com.klnvch.greenhousecontroller.ui.action.ControllerActionManager
import dagger.Module

@Module(includes = [ViewModelModule::class])
class ControllerAppModule : AppModule() {
    override fun createActionManager(actionDao: ActionDao): ActionManager {
        return ControllerActionManager(actionDao)
    }
}