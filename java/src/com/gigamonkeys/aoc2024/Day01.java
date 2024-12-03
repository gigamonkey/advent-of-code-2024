package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day01 extends Day {

  public Day01() {
    super(1);
  }

  private record Columns(List<Integer> left, List<Integer> right) {
    public void add(String[] parts) {
      left.add(Integer.valueOf(parts[0]));
      right.add(Integer.valueOf(parts[1]));
    }
  }

  private Columns data(boolean test) throws IOException {
    var cols = new Columns(new ArrayList<>(), new ArrayList<>());
    columns(input(test)).forEach(cols::add);
    return cols;
  }

  public String part1(boolean test) throws IOException {
    return switch (data(test)) {
      case Columns(var left, var right) -> {
        Collections.sort(left);
        Collections.sort(right);
        yield String.valueOf(
          IntStream.range(0, left.size()).map(i -> abs(left.get(i) - right.get(i))).sum()
        );
      }
    };
  }

  public String part2(boolean test) throws IOException {
    return switch (data(test)) {
      case Columns(var left, var right) -> {
        var freq = right.stream().collect(groupingBy(n -> n, counting()));
        yield String.valueOf(
          left.stream().mapToInt(n -> n * freq.getOrDefault(n, 0L).intValue()).sum()
        );
      }
    };
  }
}
