package com.klnvch.greenhousecommon.ui.states;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.klnvch.greenhousecommon.db.AppDatabase;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StateViewModel extends ViewModel {
    private static final String DEVICE_ID = "test";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<ViewState> viewState = new MutableLiveData<>();

    @Inject
    public StateViewModel(@NonNull AppDatabase db) {
        //AppDatabase db = AppDatabase.requireInstance();
        disposable.add(Flowable
                .combineLatest(
                        db.phoneStateDao().getLatestStates(DEVICE_ID),
                        db.moduleStateDao().getLatestStates(DEVICE_ID),
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
