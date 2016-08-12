package com.mgu.parallel;

import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

abstract public class AbstractTaskScheduler implements TaskScheduler {

    @Override
    public <A, B> Tuple2<A, B> parallel(final Supplier<A> taskA, final Supplier<B> taskB) {
        final ForkJoinTask<B> right = schedule(taskB);
        return new Tuple2<A, B>(taskA.get(), right.join());
    }
}
