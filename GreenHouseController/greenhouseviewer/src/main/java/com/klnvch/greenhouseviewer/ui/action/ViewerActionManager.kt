package com.klnvch.greenhouseviewer.ui.action

import com.klnvch.greenhousecommon.db.ActionDao
import com.klnvch.greenhousecommon.models.Action
import com.klnvch.greenhousecommon.ui.action.ActionManager
import com.klnvch.greenhouseviewer.firestore.ActionListener
import com.klnvch.greenhouseviewer.firestore.ActionWriter
import com.klnvch.greenhouseviewer.firestore.OnActionUpdatedListener
import io.reactivex.schedulers.Schedulers

class ViewerActionManager(actionDao: ActionDao) : ActionManager(actionDao),
    OnActionUpdatedListener {
    private val actionListener: ActionListener = ActionListener()

    override fun init() {
        actionListener.startListening(this)
    }

    override fun processCommand(command: Action) {
        clear()
        ActionWriter.write(command)
        init()
    }

    override fun clear() {
        actionListener.stopListening()
    }

    override fun onActionUpdated(action: Action) {
        actionDao.insert(action).subscribeOn(Schedulers.io()).onErrorComplete().subscribe()
    }
}
