package com.mgu.parallel;

/**
 * A task represents some kind of computation that yields a result of type {@code T}.
 *
 * @param <T>
 *      type of the result
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@FunctionalInterface
public interface Task<T> {
    /**
     * Computes the result of this {@code Task}.
     *
     * @return
     *      instance of tye {@code T} which represents the result of the computation
     */
    T compute();
}
