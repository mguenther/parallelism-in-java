package com.mgu.parallel;

import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

public interface TaskScheduler {

    <T>ForkJoinTask<T> schedule(Supplier<T> body);

    <A, B> Tuple2<A, B> parallel(Supplier<A> taskA, Supplier<B> taskB);
}
