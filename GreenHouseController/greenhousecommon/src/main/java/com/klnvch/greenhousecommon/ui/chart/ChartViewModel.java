package com.klnvch.greenhousecommon.ui.chart;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ChartViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ChartViewState> viewState = new MutableLiveData<>();

    @Inject
    public ChartViewModel(@NonNull AppDatabase db, @NonNull AppSettings settings) {
        final String deviceId = settings.getDeviceId();
        disposable.add(db.phoneStateDao()
                .getStatesAscending(deviceId)
                .map(ChartViewState::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewState::postValue, Timber::e));
    }

    public LiveData<ChartViewState> getViewState() {
        return viewState;
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
