package com.klnvch.greenhousecontroller.ui.action

import com.klnvch.greenhousecommon.models.Action
import com.klnvch.greenhousecommon.ui.action.ActionManager
import com.klnvch.greenhousecontroller.Command

class ControllerActionManager : ActionManager() {

    override fun processCommand(command: Action) {
        Command.sendCommand(command)
    }
}
