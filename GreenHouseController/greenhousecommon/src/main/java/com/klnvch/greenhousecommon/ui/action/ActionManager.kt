package com.klnvch.greenhousecommon.ui.action

import com.klnvch.greenhousecommon.models.Action

abstract class ActionManager {
    abstract fun processCommand(command: Action)
}
