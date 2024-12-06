package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.time.temporal.ChronoUnit.DAYS;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public class AdventOfCode {

  private static final ZoneId TZ = ZoneId.of("America/New_York");
  private static final ZonedDateTime START = ZonedDateTime.of(2024, 12, 1, 0, 0, 0, 0, TZ);
  private static final ZonedDateTime NOW = ZonedDateTime.now(TZ);

  private static final int MAX_DAY = (int) DAYS.between(START, NOW) + 1;

  public static final List<Solution> days = new ArrayList<>();

  static {
    days.add(new HistorianHysteria());
    days.add(new RedNosedReports());
    days.add(new MullItOver());
    days.add(new CeresSearch());
    days.add(new PrintQueue());
    days.add(new GuardGallivant());
  }

  private Optional<Solution> number(int day) {
    if ((day - 1) < days.size()) {
      return Optional.of(days.get(day - 1));
    } else {
      return Optional.empty();
    }
  }

  public boolean runPart(Solution s, int day, int part, boolean test) throws IOException {
    String label = test ? "test" : "real";
    Optional<String> expected = expected(day, part, test);

    if (expected.isPresent()) {
      String result = result(s, day, part, test);
      var e = expected.get();
      if (e.equals(result)) {
        System.out.printf("✅ Day %d, part %d (%s): %s%n", day, part, label, result);
        return true;
      } else {
        System.out.printf("❌ Day %d, part %d (%s): %s. Expected: %s%n", day, part, label, result, e);
      }
    } else {
      System.out.printf("🟡 Day %d, part %d (%s): no expected value yet.%n", day, part, label);
    }
    return false;
  }

  private String result(Solution s, int day, int part, boolean test) throws IOException {
    return (
      switch (part) {
        case 1 -> s.part1(input(day, part, test));
        case 2 -> s.part2(input(day, part, test));
        default -> "No part " + part;
      }
    ).trim();
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

  public void run(int start) throws IOException {
    var okay = true;

    for (var day = start; day <= MAX_DAY; day++) {
      var s = number(day);
      if (s.isPresent()) {
        for (var part = 1; part <= 2; part++) {
          okay &= runPart(s.get(), day, part, true);
          okay &= runPart(s.get(), day, part, false);
        }
      } else {
        System.out.println("⚠️ Day %d not implemented yet".formatted(day));
      }
    }

    if (okay) {
      System.out.println("\nAll okay!");
    } else {
      System.out.println("\nUh oh!");
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    int start = args.length > 0 && args[0].equals("--all") ? 1 : MAX_DAY;
    new AdventOfCode().run(start);
  }
}
