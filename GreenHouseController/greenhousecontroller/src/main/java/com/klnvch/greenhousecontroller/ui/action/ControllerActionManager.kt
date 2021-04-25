package com.klnvch.greenhousecontroller.ui.action

import com.klnvch.greenhousecommon.db.ActionDao
import com.klnvch.greenhousecommon.models.Action
import com.klnvch.greenhousecommon.ui.action.ActionManager
import com.klnvch.greenhousecontroller.Command

class ControllerActionManager(actionDao: ActionDao) : ActionManager(actionDao) {
    override fun init() {

    }

    override fun processCommand(command: Action) {
        Command.sendCommand(command)
    }

    override fun clear() {

    }
}
