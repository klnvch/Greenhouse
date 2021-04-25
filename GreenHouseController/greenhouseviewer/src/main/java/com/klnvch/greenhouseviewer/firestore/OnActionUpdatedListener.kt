package com.klnvch.greenhouseviewer.firestore

import com.klnvch.greenhousecommon.models.Action

interface OnActionUpdatedListener {
    fun onActionUpdated(action: Action)
}
