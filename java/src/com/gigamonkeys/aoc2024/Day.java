package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public abstract class Day {

  private final int day;

  public Day(int day) {
    this.day = day;
  }

  public static Day number(int day) {
    return switch (day) {
      case 1 -> new Day01();
      default -> throw new RuntimeException("Day %d not implemented yet!".formatted(day));
    };
  }

  public void part(int part, boolean test) throws IOException {
    String result =
      (switch (part) {
          case 1 -> part1(test);
          case 2 -> part2(test);
          default -> "No part " + part;
        }).trim();

    Optional<String> expected = Util.expected(day, part, test);

    expected.ifPresentOrElse(
      e -> {
        if (e.equals(result)) {
          System.out.printf("Day %d, part %d: %s - ok!%n", day, part, result);
        } else {
          System.out.printf("Day %d, part %d: %s - Ooops. Expected: %s%n", day, part, result, e);
        }
      },
      () -> System.out.printf("Day %d, part %d: %s - no expected value yet.%n", day, part, result)
    );
  }

  public abstract String part1(boolean test) throws IOException;

  public abstract String part2(boolean test) throws IOException;
}
