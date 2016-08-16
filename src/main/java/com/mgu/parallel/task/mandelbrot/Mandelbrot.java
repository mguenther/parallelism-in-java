package com.mgu.parallel.task.mandelbrot;

import static com.mgu.parallel.Schedulers.parallel;

public class Mandelbrot {

    private static final int DEFAULT_MAX_ITERATIONS = 1_000;
    private static final int DEFAULT_MAX_WORKLOAD_PER_TASK = 1_000;
    private static final double XR = -2.0;
    private static final double XI = 1.25;
    private static final double YR = 1.2;
    private static final double YI = -1.25;

    private final int width;
    private final int height;
    private final int maxWorkloadPerTask;

    public Mandelbrot(final int width, final int height) {
        this(width, height, DEFAULT_MAX_WORKLOAD_PER_TASK);
    }

    public Mandelbrot(final int width, final int height, final int maxWorkloadPerTask) {
        this.width = width;
        this.height = height;
        this.maxWorkloadPerTask = maxWorkloadPerTask;
    }

    private int compute(final double xc, final double yc) {
        return compute(xc, yc, DEFAULT_MAX_ITERATIONS);
    }

    private int compute(final double xc, final double yc, final int maxIterations) {
        int i = 0;
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

    public int[] computeSequential(final int[] values, final int from, final int to) {
        for (int i = from; i < to; i++) {

            final int x = i % width;
            final int y = i / width;

            final double xc = XR + (XI - XR) * x / width;
            final double yc = YR + (YI - YR) * y / height;

            values[i] = compute(xc, yc);
        }
        return values;
    }

    public int[] computeParallel(final int[] values, final int from, final int to) {
        if (thresholdReached(from, to)) {
            computeSequential(values, from, to);
        } else {
            final int half = (to - from) >> 1;
            parallel(
                    () -> computeParallel(values, from, from + half),
                    () -> computeParallel(values, from + half, to));
        }
        return values;
    }

    private boolean thresholdReached(final int from, final int to) {
        return (to - from) <= maxWorkloadPerTask;
    }
}
