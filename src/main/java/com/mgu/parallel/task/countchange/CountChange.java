package com.mgu.parallel.task.countchange;

import com.mgu.parallel.Tuple2;
import javaslang.collection.List;

import java.util.function.BiPredicate;

import static com.mgu.parallel.Schedulers.parallel;

public class CountChange {

    public int countChange(final int money, final List<Integer> coins) {
        if (money == 0) {
            return 1;
        } else if (money > 0 && coins.nonEmpty()) {
            return countChange(money - coins.head(), coins) + countChange(money, coins.tail());
        } else {
            return 0;
        }
    }

    public int parCountChange(final int money, final List<Integer> coins, final BiPredicate<Integer, List<Integer>> threshold) {
        if (solveSequentially(money, coins, threshold)) {
            return countChange(money, coins);
        } else {
            final Tuple2<Integer, Integer> result = parallel(
                    () -> parCountChange(money - coins.head(), coins, threshold),
                    () -> parCountChange(money, coins.tail(), threshold));
            return result.a + result.b;
        }
    }

    private boolean solveSequentially(final int money, final List<Integer> coins, final BiPredicate<Integer, List<Integer>> threshold) {
        return threshold.test(money, coins) || money <= 0 || coins.isEmpty();
    }

    public static BiPredicate<Integer, List<Integer>> moneyThreshold(final int startingMoney) {
        return (money, coins) -> startingMoney * 2 / 3 >= money;
    }

    public static BiPredicate<Integer, List<Integer>> totalCoinsThreshold(final int totalCoins) {
        return (money, coins) -> totalCoins * 2 / 3 >= coins.size();
    }

    public static BiPredicate<Integer, List<Integer>> combinedThreshold(final int startingMoney, final List<Integer> allCoins) {
        return (money, coins) -> money * coins.size() <= startingMoney * allCoins.size() / 2;
    }
}
