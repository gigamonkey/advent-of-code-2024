package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.*;
import static java.lang.Math.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day02 extends Day {

  public Day02() {
    super(2);
  }

  private List<int[]> data(boolean test) throws IOException {
    return columns(input(2, test)).map(ss -> Arrays.stream(ss).mapToInt(s -> Integer.parseInt(s)).toArray()).toList();
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(data(test).stream().filter(this::isSafe).count());
  }


  public String part2(boolean test) throws IOException {
    return "";
  }

  private boolean isSafe(int[] levels) {
    var deltas = IntStream.range(0, levels.length - 1).map(i -> levels[i] - levels[i + 1]).toArray();
    return (
      Arrays.stream(deltas).allMatch(d -> Math.signum(d) == Math.signum(deltas[0])) &&
      Arrays.stream(deltas).map(Math::abs).allMatch(d -> 1 <= d && d <= 3)
    );
  }
}
