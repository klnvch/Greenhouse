package com.klnvch.greenhousecommon.ui.chart;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.klnvch.greenhousecommon.db.AppDatabase;
import com.klnvch.greenhousecommon.db.AppSettings;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ChartViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ChartViewState> viewState = new MutableLiveData<>();
    private final AppDatabase db;
    private final AppSettings settings;
    private static final long HOUR = 60 * 60 * 1000;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 30 * WEEK;

    @Inject
    public ChartViewModel(@NonNull AppDatabase db, @NonNull AppSettings settings) {
        this.db = db;
        this.settings = settings;
        load(WEEK);
    }

    private void load(long interval) {
        final String deviceId = settings.getDeviceId();
        final long startTime = System.currentTimeMillis() - interval;

        disposable.clear();
        disposable.add(Single
                .zip(db.phoneStateDao().getStatesAscending(deviceId, startTime),
                        db.moduleStateDao().getStatesAscending(deviceId, startTime),
                        ChartViewState::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewState::postValue, Timber::e));
    }

    public LiveData<ChartViewState> getViewState() {
        return viewState;
    }

    public void setTimeInterval(int which) {
        switch (which) {
            case 0:
                load(HOUR);
                break;
            case 1:
                load(DAY);
                break;
            case 2:
                load(WEEK);
                break;
            case 3:
                load(MONTH);
                break;
        }
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
