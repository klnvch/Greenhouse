package com.klnvch.greenhousecommon.ui.action

import com.klnvch.greenhousecommon.db.ActionDao
import com.klnvch.greenhousecommon.models.Action

abstract class ActionManager(val actionDao: ActionDao) {
    abstract fun init()
    abstract fun processCommand(command: Action)
    abstract fun clear()
}
