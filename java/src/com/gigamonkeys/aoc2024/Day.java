package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public abstract class Day {

  private static final List<Day> days = new ArrayList<>();

  static {
    days.add(new Day01());
    days.add(new Day02());
    days.add(new Day03());
  }

  private final int day;

  public Day() {
    this.day = days.size() + 1;
  }

  public static Day number(int day) {
    if ((day - 1) < days.size()) {
      return days.get(day - 1);
    } else {
      throw new RuntimeException("Day %d not implemented yet!".formatted(day));
    }
  }

  public boolean part(int part, boolean test) throws IOException {
    String result =
      (switch (part) {
          case 1 -> part1(input(part, test));
          case 2 -> part2(input(part, test));
          default -> "No part " + part;
        }).trim();

    Optional<String> expected = expected(part, test);

    if (expected.isPresent()) {
      var e = expected.get();
      if (e.equals(result)) {
        System.out.printf("Day %d, part %d: %s - ok!%n", day, part, result);
        return true;
      } else {
        System.out.printf("Day %d, part %d: %s - Ooops. Expected: %s%n", day, part, result, e);
      }
    } else {
      System.out.printf("Day %d, part %d: %s - no expected value yet.%n", day, part, result);
    }
    return false;
  }

  /**
   * Get the name of the input file for based on whether it's the test input or
   * the real input.
   */
  public Path input(boolean test) {
    return Path.of("inputs/day-%02d/%s.txt".formatted(day, test ? "test" : "real"));
  }

  public Path input(int part, boolean test) {
    // Special case for some days tat have different test input for part 2
    if (test && part == 2) {
      var p2 = Path.of("inputs/day-%02d/test2.txt".formatted(day));
      if (exists(p2)) return p2;
    }

    // The normal case.
    return Path.of("inputs/day-%02d/%s.txt".formatted(day, test ? "test" : "real"));
  }

  public Optional<String> expected(int part, boolean test) throws IOException {
    var p = expectedPath(part, test);
    return exists(p) ? Optional.of(readString(p).trim()) : Optional.empty();
  }

  public abstract String part1(Path input) throws IOException;

  public abstract String part2(Path input) throws IOException;

  private Path expectedPath(int part, boolean test) {
    return Path.of("inputs/day-%02d/part-%d%s.expected".formatted(day, part, test ? "-test" : ""));
  }
}
