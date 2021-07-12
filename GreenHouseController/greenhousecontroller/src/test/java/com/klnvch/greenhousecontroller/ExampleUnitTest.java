package com.klnvch.greenhousecontroller;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        BehaviorSubject<Integer> subject = BehaviorSubject.create();

        TestScheduler scheduler = new TestScheduler();
        TestObserver<Integer> subscriber = new TestObserver<>();

        subject.subscribeOn(scheduler)
                .throttleLatest(10, TimeUnit.MINUTES)
                .subscribe(subscriber);

        subscriber.assertNoValues();

        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);

        subscriber.assertNoValues();
        scheduler.advanceTimeBy(20, TimeUnit.MINUTES);
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(3);

        subject.onNext(5);
        subject.onNext(6);
        subject.onNext(7);

        scheduler.advanceTimeBy(20, TimeUnit.MINUTES);
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(7);
    }
}