package com.gigamonkeys.aoc2024;

import static java.lang.System.nanoTime;
import static java.nio.file.Files.*;
import static java.time.temporal.ChronoUnit.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdventOfCode {

  private static final ZoneId TZ = ZoneId.of("America/New_York");
  private static final ZonedDateTime START = ZonedDateTime.of(2024, 12, 1, 0, 0, 0, 0, TZ);
  private static final ZonedDateTime NOW = ZonedDateTime.now(TZ);

  private static final int MAX_DAY = (int) DAYS.between(START, NOW) + 1;

  private static final List<Solution> SOLUTIONS = List.of(
    new Day01_HistorianHysteria(),
    new Day02_RedNosedReports(),
    new Day03_MullItOver(),
    new Day04_CeresSearch(),
    new Day05_PrintQueue(),
    new Day06_GuardGallivant(),
    new Day07_BridgeRepair(),
    new Day08_ResonantCollinearity(),
    new Day09_DiskFragmenter(),
    new Day10_HoofIt()
  );

  private Optional<Solution> solutionFor(int day) {
    if ((day - 1) < SOLUTIONS.size()) {
      return Optional.of(SOLUTIONS.get(day - 1));
    } else {
      return Optional.empty();
    }
  }

  private boolean runPart(Solution s, int day, int part, boolean test) throws IOException {
    var start = nanoTime();
    var result = result(s, day, part, test);
    var elapsed = Math.round((nanoTime() - start) / 1e6);

    var expected = expected(day, part, test);
    var okay = result.equals(expected);

    var label = test ? "test" : "real";
    var synopsis = "Day %d, part %d (%s): %s".formatted(day, part, label, result);
    var time = "(%d ms)".formatted(elapsed);

    if (okay) {
      System.out.printf("✅ %s %s%n".formatted(synopsis, time));
    } else if (expected.equals("")) {
      System.out.printf("🟡 %s %s. No expected value yet.%n", synopsis, time);
    } else {
      System.out.printf("❌ %s. Expected: %s %s%n", synopsis, expected, time);
    }
    return okay;
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

  private String expected(int day, int part, boolean test) throws IOException {
    var p = Path.of("inputs/day-%02d/part-%d%s.expected".formatted(day, part, test ? "-test" : ""));
    return exists(p) ? readString(p).trim() : "";
  }

  private void run(int firstDay, int lastDay) throws IOException {
    var okay = true;

    long start = nanoTime();
    for (var day = firstDay; day <= lastDay; day++) {
      var s = solutionFor(day);
      if (s.isPresent()) {
        for (var part = 1; part <= 2; part++) {
          okay &= runPart(s.get(), day, part, true);
          if (okay) {
            // Only run the real problem if we're passing the test case. Mostly
            // so we can emit debugging output and not get spammed because the
            // real test case is so much bigger.
            okay &= runPart(s.get(), day, part, false);
          }
        }
      } else {
        System.out.println("⚠️ Day %d not implemented yet".formatted(day));
      }
    }
    var elapsed = (nanoTime() - start) / 1e9;

    var label = okay ? "All okay" : "Uh oh";
    System.out.println("\n%s! (%f seconds)".formatted(label, elapsed));
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    var aoc = new AdventOfCode();
    if (args.length > 0 && args[0].equals("--all")) {
      aoc.run(1, MAX_DAY);
    } else {
      var day = args.length > 0 ? Integer.parseInt(args[0]) : MAX_DAY;
      aoc.run(day, day);
    }
  }
}
