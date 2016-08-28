package com.mgu.parallel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Provides some convenience methods to use the parallelization API of the {@link ForkJoinTaskScheduler}.
 * This class relies on the {@code ForkJoinPool.commonPool()} for submitted {@code ForkJoinTask}s.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Schedulers {

    private static final TaskScheduler SCHEDULER = new ForkJoinTaskScheduler();

    private static <T> Future<T> task(final Task<T> body) {
        return SCHEDULER.schedule(body);
    }

    public static <A, B> Tuple2<A, B> parallel(final Task<A> taskA, final Task<B> taskB) {
        return SCHEDULER.parallel(taskA, taskB);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> parallel(
            final Task<A> taskA,
            final Task<B> taskB,
            final Task<C> taskC,
            final Task<D> taskD) throws ExecutionException, InterruptedException {
        final Future<A> ta = task(taskA);
        final Future<B> tb = task(taskB);
        final Future<C> tc = task(taskC);
        final Future<D> td = task(taskD);
        return new Tuple4<>(ta.get(), tb.get(), tc.get(), td.get());
    }
}
