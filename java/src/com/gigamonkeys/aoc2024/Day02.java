package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.columns;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.stream.IntStream.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.IntPredicate;

public class Day02 implements Solution {

  private static final IntPredicate GAP_OKAY = n -> 1 <= abs(n) && abs(n) <= 3;

  private List<int[]> data(Path input) throws IOException {
    return columns(input).map(ss -> stream(ss).mapToInt(s -> Integer.parseInt(s)).toArray()).toList();
  }

  public String part1(Path input) throws IOException {
    return String.valueOf(data(input).stream().filter(this::isStrictlySafe).count());
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(data(input).stream().filter(this::isSafe).count());
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
      isStrictlySafe(levels) || range(0, levels.length).mapToObj(i -> without(levels, i)).anyMatch(this::isStrictlySafe)
    );
  }

  private int[] without(int[] levels, int idx) {
    int[] wo = new int[levels.length - 1];
    System.arraycopy(levels, 0, wo, 0, idx);
    System.arraycopy(levels, idx + 1, wo, idx, wo.length - idx);
    return wo;
  }
}
