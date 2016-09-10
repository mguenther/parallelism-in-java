package com.mgu.parallel.data.mandelbrot;

import javaslang.collection.Array;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class MandelbrotBenchmark {

    private static final int LENGTH_SIDE_A = 1_000;

    private static final int LENGTH_SIDE_B = 1_000;

    private final Mandelbrot mandelbrot;

    public MandelbrotBenchmark() {
        this.mandelbrot = new Mandelbrot(LENGTH_SIDE_A, LENGTH_SIDE_B);
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runMandelbrotDataParallel() {
        final Array<Integer> iterationsPerCoordinate = Array.range(0, LENGTH_SIDE_A * LENGTH_SIDE_B);
        mandelbrot.computeParallel(iterationsPerCoordinate);
    }
}
