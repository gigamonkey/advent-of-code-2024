package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.*;
import static java.lang.Math.*;

import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day02 extends Day {

  private static final IntPredicate GAP_OKAY = n -> 1 <= abs(n) && abs(n) <= 3;

  public Day02() {
    super(2);
  }

  private List<int[]> data(boolean test) throws IOException {
    return columns(input(2, test)).map(ss -> Arrays.stream(ss).mapToInt(s -> Integer.parseInt(s)).toArray()).toList();
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(data(test).stream().filter(this::isStrictlySafe).count());
  }

  public String part2(boolean test) throws IOException {
    return String.valueOf(data(test).stream().filter(this::isSafe).count());
  }

  private boolean isSafe(int[] levels) {
    if (isStrictlySafe(levels)) return true;
    return IntStream.range(0, levels.length)
      .mapToObj(i -> without(levels, i))
      .anyMatch(this::isStrictlySafe);
  }

  private int[] without(int[] levels, int idx) {
    return IntStream.range(0, levels.length).filter(i -> i != idx).map(i -> levels[i]).toArray();
  }

  private boolean isStrictlySafe(int[] levels) {
    var deltas = deltas(levels);
    return Arrays.stream(deltas).allMatch(GAP_OKAY.and(d -> signum(d) == signum(deltas[0])));
  }

  private int[] deltas(int[] levels) {
    return IntStream.range(0, levels.length - 1).map(i -> levels[i] - levels[i + 1]).toArray();
  }
}
