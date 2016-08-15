package com.mgu.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;

public class DefaultTaskScheduler implements TaskScheduler {

    private final ForkJoinPool forkJoinPool;

    public DefaultTaskScheduler() {
        this(ForkJoinPool.commonPool());
    }

    public DefaultTaskScheduler(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    @Override
    public <A, B> Tuple2<A, B> parallel(final Supplier<A> taskA, final Supplier<B> taskB) {
        final ForkJoinTask<B> right = schedule(taskB);
        return new Tuple2<>(taskA.get(), right.join());
    }

    @Override
    public void parallel(final Runnable taskA, final Runnable taskB) {
        final ForkJoinTask<Void> right = schedule(taskB);
        taskA.run();
        right.join();
    }

    @Override
    public <T> ForkJoinTask<T> schedule(final Supplier<T> body) {

        final RecursiveTask<T> task = new RecursiveTask<T>() {
            @Override
            protected T compute() {
                return body.get();
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
    public ForkJoinTask<Void> schedule(final Runnable body) {

        final RecursiveAction task = new RecursiveAction() {
            @Override
            protected void compute() {
                body.run();
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
