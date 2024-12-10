package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.*;
import static java.util.Arrays.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

// Alternate solution to Day 7. Works well for part 1 because of efficient bit
// twiddling. Not as good for part 2, presumably because dividing by 3 is more
// expensive.
public class Day07_BridgeRepair2 implements Solution {

  private static final Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(long value, List<Long> numbers) {
    boolean check() {
      int limit = 1 << numbers.size();
      for (int x = 0; x < limit; x++) {
        var total = numbers.get(0);
        var bits = x;
        for (long n : numbers.subList(1, numbers.size())) {
          if ((bits & 1) == 0) {
            total += n;
          } else {
            total *= n;
          }
          bits >>>= 1;
        }
        if (total == value) return true;
      }
      return false;
    }

    boolean check2() {
      int limit = (int) Math.pow(3, numbers.size());
      for (int x = 0; x < limit; x++) {
        var total = numbers.get(0);
        var bits = x;
        for (long n : numbers.subList(1, numbers.size())) {
          switch (bits % 3) {
            case 0 -> total += n;
            case 1 -> total *= n;
            case 2 -> total = Long.parseLong("" + total + n);
          }
          bits /= 3;
        }
        if (total == value) return true;
      }
      return false;
    }
  }

  public String part1(Path input) throws IOException {
    return solve(input, Equation::check);
  }

  public String part2(Path input) throws IOException {
    return solve(input, Equation::check2);
  }

  private String solve(Path input, Predicate<Equation> p) throws IOException {
    return String.valueOf(lines(input).parallel().map(this::parseLine).filter(p).mapToLong(Equation::value).sum());
  }

  private Equation parseLine(String line) {
    var m = p.matcher(line);
    if (m.matches()) {
      return new Equation(Long.parseLong(m.group(1)), stream(m.group(2).split("\\s+")).map(Long::valueOf).toList());
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }
}
