package com.mgu.parallel.task.mandelbrot;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class MandelbrotBenchmark {

    private final Mandelbrot mandelbrot;

    public MandelbrotBenchmark() {
        this.mandelbrot = new Mandelbrot(4000, 4000);
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runMandelbrotSequential() {
        final int[] iterationsPerCoordinate = new int[4000*4000];
        mandelbrot.computeSequential(iterationsPerCoordinate, 0, iterationsPerCoordinate.length);
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runMandelbrotParallel() {
        final int[] iterationsPerCoordinate = new int[4000*4000];
        mandelbrot.computeParallel(iterationsPerCoordinate, 0, iterationsPerCoordinate.length);
    }
}
