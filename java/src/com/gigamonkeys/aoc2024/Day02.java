package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.columns;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.stream.IntStream.*;

import java.io.IOException;
import java.util.List;
import java.util.function.IntPredicate;

public class Day02 extends Day {

  private static final IntPredicate GAP_OKAY = n -> 1 <= abs(n) && abs(n) <= 3;

  private List<int[]> data(boolean test) throws IOException {
    return columns(input(test))
      .map(ss -> stream(ss).mapToInt(s -> Integer.parseInt(s)).toArray())
      .toList();
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(data(test).stream().filter(this::isStrictlySafe).count());
  }

  public String part2(boolean test) throws IOException {
    return String.valueOf(data(test).stream().filter(this::isSafe).count());
  }

  private boolean isStrictlySafe(int[] levels) {
    var deltas = deltas(levels);
    return stream(deltas).allMatch(GAP_OKAY.and(d -> signum(d) == signum(deltas[0])));
  }

  private int[] deltas(int[] levels) {
    return range(0, levels.length - 1).map(i -> levels[i] - levels[i + 1]).toArray();
  }

  private boolean isSafe(int[] levels) {
    return (
      isStrictlySafe(levels) ||
      range(0, levels.length).mapToObj(i -> without(levels, i)).anyMatch(this::isStrictlySafe)
    );
  }

  private int[] without(int[] levels, int idx) {
    return range(0, levels.length).filter(i -> i != idx).map(i -> levels[i]).toArray();
  }
}
