package com.mgu.parallel.task.countchange;

import javaslang.collection.List;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class CountChangeBenchmark {

    private final CountChange countChange;

    public CountChangeBenchmark() {
        this.countChange = new CountChange();
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runCountChangeSequential() {
        final int startingMoney = 250;
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50);
        countChange.countChange(startingMoney, coins);
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runCountChangeParallelMoneyHeuristic() {
        final int startingMoney = 250;
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50);
        countChange.parCountChange(startingMoney, coins, CountChange.moneyThreshold(startingMoney));
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runCountChangeParallelTotalCoinsHeuristic() {
        final int startingMoney = 250;
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50);
        countChange.parCountChange(startingMoney, coins, CountChange.totalCoinsThreshold(coins.size()));
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runCountChangeParallelCombinedHeuristic() {
        final int startingMoney = 250;
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50);
        countChange.parCountChange(startingMoney, coins, CountChange.combinedThreshold(startingMoney, coins));
    }
}
