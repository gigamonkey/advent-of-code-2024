package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static net.berkeley.students.peterseibel.Util.*;

import static java.util.stream.Collectors.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public interface Day {

  public static Day number(int day) {
    return switch (day) {
      case 1 -> new Day01();
      default -> throw new RuntimeException("Day %d not implemented yet!".formatted(day));
    };
  }

  public default void part(int part, boolean test) throws IOException {
    switch (part) {
      case 1 -> part1(test);
      case 2 -> part2(test);
    };
  }

  public void part1(boolean test) throws IOException;

  public void part2(boolean test) throws IOException;

}
