package com.mgu.parallel.task.pi;

import com.mgu.parallel.Tuple4;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.mgu.parallel.Schedulers.parallel;

public class PiEstimator {

    public double estimateSequential(final int iterations) {
        return 4.0 * monteCarloCount(iterations) / iterations;
    }

    public double estimateParallel(final int iterations) throws ExecutionException, InterruptedException {
        final Tuple4<Integer, Integer, Integer, Integer> result = parallel(
                () -> monteCarloCount(iterations/4),
                () -> monteCarloCount(iterations/4),
                () -> monteCarloCount(iterations/4),
                () -> monteCarloCount(iterations-3*iterations/4));
        return 4.0 * (result.a + result.b + result.c + result.d) / iterations;
    }

    private int monteCarloCount(final int iterations) {
        final Random randomX = new Random();
        final Random randomY = new Random();
        int hits = 0;
        for (int i = 0; i < iterations; i++) {
            final double x = randomX.nextDouble();
            final double y = randomY.nextDouble();
            if (x*x + y*y < 1) hits++;
        }
        return hits;
    }
}
