package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Util {

  private static Path expectedPath(int day, int part, boolean test) {
    return Path.of("inputs/day-%02d/part-%d%s.expected".formatted(day, part, test ? "-test" : ""));
  }

  /**
   * Stream of whitespace delimited columnar inputs.
   */
  public static Stream<String[]> columns(Path p) throws IOException {
    return lines(p).map(line -> line.split("\\s+"));
  }

  /**
   * Text of file.
   */
  public static String text(Path p) throws IOException {
    return readString(p).trim();
  }

  /**
   * Contents of expected file.
   */
  public static Optional<String> expected(int day, int part, boolean test) throws IOException {
    var p = expectedPath(day, part, test);
    return exists(p) ? Optional.of(readString(p).trim()) : Optional.empty();
  }
}
