package com.klnvch.greenhousecontroller.bluetooth;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public final class BluetoothRestartCounter {
    private static final int REPEAT_TIMEOUT_SECONDS = 300;
    private static BluetoothRestartCounter instance = null;
    private final BehaviorSubject<Long> counter = BehaviorSubject.create();
    private Disposable disposable;
    private boolean completeNow = false;

    private BluetoothRestartCounter() {
        counter.onNext(0L);
    }

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
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeWhile(i -> i < REPEAT_TIMEOUT_SECONDS && !completeNow)
                .subscribe(
                        i -> counter.onNext(REPEAT_TIMEOUT_SECONDS - i),
                        throwable -> Timber.e("Counter error: %s", throwable.getMessage()),
                        action
                );
    }

    public void completeNow() {
        completeNow = true;
        counter.onNext(0L);
    }

    public void reset() {
        completeNow = false;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
