package com.mgu.parallel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

public class Schedulers {

    public static final DefaultTaskScheduler SCHEDULER = new DefaultTaskScheduler();

    private static <T> ForkJoinTask<T> task(final Supplier<T> body) {
        return SCHEDULER.schedule(body);
    }

    public static <A, B> Tuple2<A, B> parallel(final Supplier<A> taskA, final Supplier<B> taskB) {
        return SCHEDULER.parallel(taskA, taskB);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> parallel(
            final Supplier<A> taskA,
            final Supplier<B> taskB,
            final Supplier<C> taskC,
            final Supplier<D> taskD) throws ExecutionException, InterruptedException {
        final ForkJoinTask<A> ta = task(taskA);
        final ForkJoinTask<B> tb = task(taskB);
        final ForkJoinTask<C> tc = task(taskC);
        final ForkJoinTask<D> td = task(taskD);
        return new Tuple4<>(ta.join(), tb.join(), tc.join(), td.get());
    }
}
