package com.mgu.parallel.data.mandelbrot;

import javaslang.collection.Array;
import javaslang.collection.Seq;
import javaslang.concurrent.Future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mandelbrot {

    private static final int DEFAULT_MAX_ITERATIONS = 1_000;

    private static final double XR = -2.0;

    private static final double XI = 1.25;

    private static final double YR = 1.2;

    private static final double YI = -1.25;

    private final ExecutorService executorService;

    private final int width;

    private final int height;

    public Mandelbrot(final int width, final int height) {
        this(width, height, Executors.newFixedThreadPool(8));
    }

    public Mandelbrot(final int width, final int height, final ExecutorService executorService) {
        this.width = width;
        this.height = height;
        this.executorService = executorService;
    }

    public Future<Seq<Integer>> computeParallel(final Array<Integer> withIndicesInCells) {
        return Future.traverse(executorService, withIndicesInCells, this::singleComputation);
    }

    private Future<Integer> singleComputation(final Integer index) {
        final int x = index % width;
        final int y = index / width;

        final double xc = XR + (XI - XR) * x / width;
        final double yc = YR + (YI - YR) * y / height;

        return Future.of(() -> mandelbrot(xc, yc));
    }

    private Integer mandelbrot(final double xc, final double yc) {
        return mandelbrot(xc, yc, DEFAULT_MAX_ITERATIONS);
    }

    private Integer mandelbrot(final double xc, final double yc, final int maxIterations) {
        Integer i = 0;
        double x = 0.0;
        double y = 0.0;
        while (x * x + y * y < 4 && i < maxIterations) {
            double xt = x * x - y * y + xc;
            double yt = 2 * x * y + yc;
            x = xt;
            y = yt;
            i++;
        }
        return i;
    }
}
