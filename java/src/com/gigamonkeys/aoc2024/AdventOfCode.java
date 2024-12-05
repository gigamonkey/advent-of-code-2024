package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.time.temporal.ChronoUnit.DAYS;

import java.nio.file.*;
import java.util.*;
import java.io.IOException;
import java.time.*;

public class AdventOfCode {

  private static final ZoneId TZ = ZoneId.of("America/New_York");
  private static final ZonedDateTime START = ZonedDateTime.of(2024, 12, 1, 0, 0, 0, 0, TZ);
  private static final ZonedDateTime NOW = ZonedDateTime.now(TZ);

  private static final int MAX_DAY = (int) DAYS.between(START, NOW) + 1;

  public static final List<Day> days = new ArrayList<>();

  static {
    days.add(new Day01());
    days.add(new Day02());
    days.add(new Day03());
    days.add(new Day04());
    days.add(new Day05());
  }

  private Day number(int day) {
    if ((day - 1) < days.size()) {
      return days.get(day - 1);
    } else {
      throw new RuntimeException("Day %d not implemented yet!".formatted(day));
    }
  }

  public boolean runPart(Day s, int day, int part, boolean test) throws IOException {
    String result =
      (switch (part) {
        case 1 -> s.part1(input(day, part, test));
        case 2 -> s.part2(input(day, part, test));
        default -> "No part " + part;
      }).trim();

    Optional<String> expected = expected(day, part, test);

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
   * Get the Path of the input file for either the test or real input.
   */
  private Path input(int day, int part, boolean test) {
    // Special case for some days that have different test input for part 2
    if (test && part == 2) {
      var p2 = Path.of("inputs/day-%02d/test2.txt".formatted(day));
      if (exists(p2)) return p2;
    }

    // The normal case.
    return Path.of("inputs/day-%02d/%s.txt".formatted(day, test ? "test" : "real"));
  }

  private Optional<String> expected(int day, int part, boolean test) throws IOException {
    var p = Path.of("inputs/day-%02d/part-%d%s.expected".formatted(day, part, test ? "-test" : ""));
    return exists(p) ? Optional.of(readString(p).trim()) : Optional.empty();
  }

  public boolean run(int start) throws IOException {

    boolean okay = true;

    for (var day = start; day <= MAX_DAY; day++) {
      for (var part = 1; part <= 2; part++) {
        var s = number(day);
        okay &= runPart(s, day, part, true);
        okay &= runPart(s, day, part, false);
      }
    }

    return okay;
  }


  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");

    int start = args.length > 0 && args[0].equals("--all") ? 1 : MAX_DAY;
    var aoc = new AdventOfCode();
    boolean okay = aoc.run(start);

    if (okay) {
      System.out.println("\nAll okay!");
    } else {
      System.out.println("\nUh oh!");
    }
  }
}
