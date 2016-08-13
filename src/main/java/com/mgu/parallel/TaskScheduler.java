package com.mgu.parallel;

import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

/**
 * Provides the means to schedule single tasks or multiple tasks in parallel. Implementing classes define the
 * scheduling strategy by using an appropriate scheduling / Thread-pool mechanism.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface TaskScheduler {

    <T> ForkJoinTask<T> schedule(Supplier<T> body);

    <A, B> Tuple2<A, B> parallel(Supplier<A> taskA, Supplier<B> taskB);
}
