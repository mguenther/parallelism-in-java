package com.mgu.parallel.task.pi;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@State
public class PiBenchmark {

    private static final int ITERATIONS = 10_000_000;

    private final PiEstimator estimator;

    public PiBenchmark() {
        this.estimator = new PiEstimator();
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runPiEstimationSequential() {
        estimator.estimateSequential(ITERATIONS);
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runPiEstimationParallel() throws ExecutionException, InterruptedException {
        estimator.estimateParallel(ITERATIONS);
    }
}
