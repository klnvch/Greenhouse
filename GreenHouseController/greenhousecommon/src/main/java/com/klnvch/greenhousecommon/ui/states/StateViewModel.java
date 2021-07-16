package com.klnvch.greenhousecommon.ui.states;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    @Inject
    public StateViewModel(@NonNull AppDatabase db, @NonNull AppSettings settings) {
        final String deviceId = settings.getDeviceId();
        final long startTime = System.currentTimeMillis() - AppSettings.DAY;
        disposable.add(Flowable
                .combineLatest(
                        db.phoneStateDao().getLatestStates(deviceId, startTime),
                        db.moduleStateDao().getLatestStates(deviceId, startTime),
                        db.phoneStateDao().getLastBatteryNotFullState(deviceId),
                        db.phoneStateDao().getLastBluetoothFailState(deviceId),
                        ViewState::new)
                .subscribeOn(Schedulers.io())
                .subscribe(viewState::postValue, Timber::e));
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
