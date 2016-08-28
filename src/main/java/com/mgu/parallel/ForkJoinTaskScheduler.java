package com.mgu.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * Implementation of {@link TaskScheduler} that uses the Fork-Join framework.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class ForkJoinTaskScheduler implements TaskScheduler {

    private final ForkJoinPool forkJoinPool;

    public ForkJoinTaskScheduler() {
        this(ForkJoinPool.commonPool());
    }

    public ForkJoinTaskScheduler(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    @Override
    public <A, B> Tuple2<A, B> parallel(final Task<A> taskA, final Task<B> taskB) {
        final ForkJoinTask<B> right = schedule(taskB);
        return new Tuple2<>(taskA.compute(), right.join());
    }

    @Override
    public void parallel(final UntypedTask taskA, final UntypedTask taskB) {
        final ForkJoinTask<Void> right = schedule(taskB);
        taskA.compute();
        right.join();
    }

    @Override
    public <T> ForkJoinTask<T> schedule(final Task<T> body) {

        final RecursiveTask<T> task = new RecursiveTask<T>() {
            @Override
            protected T compute() {
                return body.compute();
            }
        };

        if (isForkJoinWorkerThread()) {
            task.fork();
        } else {
            forkJoinPool.execute(task);
        }

        return task;
    }

    @Override
    public ForkJoinTask<Void> schedule(final UntypedTask body) {

        final RecursiveAction task = new RecursiveAction() {
            @Override
            protected void compute() {
                body.compute();
            }
        };

        if (isForkJoinWorkerThread()) {
            task.fork();
        } else {
            forkJoinPool.execute(task);
        }

        return task;
    }

    private boolean isForkJoinWorkerThread() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }
}
