package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static net.berkeley.students.peterseibel.Util.*;

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
    String result = switch (part) {
      case 1 -> part1(test);
      case 2 -> part2(test);
      default -> "No part " + part;
    };

    Optional<String> expected = Util.expected(day, part, test);
    System.out.printf("got: %s; expected: %s\n", result, expected);

  }

  public abstract String part1(boolean test) throws IOException;

  public abstract String part2(boolean test) throws IOException;

}
