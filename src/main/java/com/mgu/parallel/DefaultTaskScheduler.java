package com.mgu.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;

public class DefaultTaskScheduler extends AbstractTaskScheduler {

    private final ForkJoinPool forkJoinPool;

    public DefaultTaskScheduler() {
        this(ForkJoinPool.commonPool());
    }

    public DefaultTaskScheduler(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
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

    private boolean isForkJoinWorkerThread() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }
}
