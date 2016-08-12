package com.mgu.parallel.pi;

import com.mgu.parallel.Tuple4;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.mgu.parallel.Schedulers.parallel;

public class PiEstimation {

    public double computeSequentially(final int iterations) {
        return 4.0 * monteCarloCount(iterations) / iterations;
    }

    public double computeParallel(final int iterations) throws ExecutionException, InterruptedException {
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

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final PiEstimation piMonteCarlo = new PiEstimation();
        long start = System.nanoTime();
        final double piSeq = piMonteCarlo.computeSequentially(50000000);
        long end = System.nanoTime();
        long duration = (end - start) / 1000000;
        System.out.println("Computed PI (" + piSeq + ") sequentially in " + duration + " ms.");
        start = System.nanoTime();
        final double piPar = piMonteCarlo.computeParallel(50000000);
        end = System.nanoTime();
        duration = (end - start) / 1000000;
        System.out.println("Computed PI (" + piPar + ") parallel in " + duration + " ms.");
    }
}
