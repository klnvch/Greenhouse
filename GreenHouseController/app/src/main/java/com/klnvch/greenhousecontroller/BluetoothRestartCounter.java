package com.klnvch.greenhousecontroller;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class BluetoothRestartCounter {
    private static final int REPEAT_TIMEOUT_SECONDS = 300;
    private static BluetoothRestartCounter instance = null;
    private Disposable disposable;
    private BehaviorSubject<Long> counter = BehaviorSubject.create();
    private Action action = null;

    @NonNull
    public static BluetoothRestartCounter getInstance() {
        if (instance == null) {
            instance = new BluetoothRestartCounter();
        }
        return instance;
    }

    public Observable<Long> getCounter() {
        return counter;
    }

    public void start(@NonNull Action action) {
        reset();
        this.action = action;
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(i -> i >= REPEAT_TIMEOUT_SECONDS)
                .subscribe(
                        i -> counter.onNext(REPEAT_TIMEOUT_SECONDS - i),
                        throwable -> Timber.e("Counter error: %s", throwable.getMessage()),
                        action
                );
    }

    public void reset() {
        action = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
