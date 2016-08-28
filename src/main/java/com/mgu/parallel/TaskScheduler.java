package com.mgu.parallel;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * Provides the means to schedule single tasks or multiple tasks in parallel. Implementing classes define the
 * scheduling strategy by using an appropriate scheduling / Thread-pool mechanism.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface TaskScheduler {
    /**
     * Schedules the execution of the given {@code Supplier}. Yields a {@code Future} of parametric
     * type {@code T}.
     *
     * @param body
     *      a function that consumes no input arguments but returns a result of
     *      type {@code T}, scheduled for later execution
     * @param <T>
     *      type of the result
     * @return
     *      instance of {@code Future} that will eventually conclude with a result of type
     *      {@code T}
     */
    <T> Future<T> schedule(Supplier<T> body);

    /**
     * Schedules the execution of the given {@code Runnable}. Yields a {@code Future} of parametric
     * type {@code Void}. This variant of {@code schedule} should be used if the given {@code Runnable} closes
     * over its input data and the input data is the target as well.
     *
     * @param body
     *      a procedure that consumes no input arguments and returns no result either, scheduled for
     *      later execution
     * @return
     *      instance of {@code Future} that will eventually conclude
     */
    Future<Void> schedule(Runnable body);

    /**
     * Executes both given {@code Supplier}s in parallel. Waits for termination of both and returns their
     * result represented using the tuple type {@code Tuple2}. The result of {@code taskA} is represented
     * as {@code Tuple2#a} while the result of {@code taskB} is represented as {@code Tuple2#b}.
     *
     * @param taskA
     *      a function that consumes no input arguments (it typically closes over them) but returns a
     *      result of type {@code A}, scheduled for parallel execution alongside {@code taskB}
     * @param taskB
     *      a function that consumes no input arguments (it typically closes over them) but returns a
     *      result of type {@code B}, scheduled for parallel execution alongside {@code taskA}
     * @param <A>
     *     type of result for {@code taskA}
     * @param <B>
     *     type of result for {@code taskB}
     * @return
     *      instance of {@code Tuple2} which yields the results of the parallel computation
     */
    <A, B> Tuple2<A, B> parallel(Supplier<A> taskA, Supplier<B> taskB);

    /**
     * Executes both given {@code Runnable}s in parallel and awaits their termination. This variant of
     * {@code parallel} should be used if the given {@code Runnable} closes over its input data and the
     * input data is the target as well.
     *
     * @param taskA
     *      a procedure that consumes no input arguments and returns no result either, scheduled for
     *      parallel execution alongside {@code taskB}
     * @param taskB
     *      a procedure that consumes no input arguments and returns no result either, scheduled for
     *      parallel execution alongside {@code taskA}
     */
    void parallel(Runnable taskA, Runnable taskB);
}
