package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.Map.Entry;
import static java.util.Map.entry;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day11_PlutonianPebbles2 implements Solution {

  public String part1(Path input) throws IOException {
    return String.valueOf(solve(longs(input), 25));
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(solve(longs(input), 75));
  }

  private long solve(List<Long> nums, int iters) {
    var state = nums.stream().collect(groupingBy(identity(), counting()));
    for (int i = 0; i < iters; i++) {
      state = step(state);
    }
    return state.values().stream().mapToLong(n -> n).sum();
  }

  private Map<Long, Long> step(Map<Long, Long> state) {
    return state
      .entrySet()
      .stream()
      .flatMap(e -> replacements(e.getKey()).map(n -> entry(n, e.getValue())))
      .collect(groupingBy(Entry::getKey, summingLong(Entry::getValue)));
  }

  private Stream<Long> replacements(long n) {
    if (n == 0) {
      return Stream.of(1L);
    } else {
      var d = digits(n);
      if (d % 2 == 0) {
        var places = (long) pow(10, d / 2);
        var left = n / places;
        var right = n % places;
        return Stream.of(left, right);
      } else {
        return Stream.of(n * 2024);
      }
    }
  }

  private int digits(long n) {
    return n == 0 ? 1 : (int) floor(log10(n)) + 1;
  }
}
