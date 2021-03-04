package com.klnvch.greenhousecommon.ui.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.klnvch.greenhousecommon.db.AppDatabase
import com.klnvch.greenhousecommon.db.AppSettings
import com.klnvch.greenhousecommon.models.Action
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ActionViewModel @Inject constructor(val db: AppDatabase, val settings: AppSettings) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val viewState: MutableLiveData<ActionViewState> = MutableLiveData()

    init {
        val deviceId: String = settings.getDeviceId()
        compositeDisposable.add(db.actionDao()
                .getActions(deviceId)
                .map { ActionViewState(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewState::postValue, Timber::e))
    }

    fun getViewState(): LiveData<ActionViewState> {
        return viewState
    }

    fun sendCommand(command: String) {
        val action = Action(settings.getDeviceId(), System.currentTimeMillis(), command, 0)
        compositeDisposable.add(db.actionDao().insert(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, Timber::e))
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
