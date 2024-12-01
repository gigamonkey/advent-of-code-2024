package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.exists;

import java.util.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Util {

  /**
   * Get the name of the input file for a given day and part and whether it's
   * the test input or the real input.
   */
  public static Path input(int day, boolean test) {
    return Path.of("inputs/day-%02d/%s.txt".formatted(day, test ? "test" : "real"));
  }

  private static Path expectedPath(int day, int part, boolean test) {
    return Path.of("inputs/day-%02d/part-%d-%s.expected".formatted(day, test ? "test" : "real"));
  }

  /**
   * Stream of whitespace delimited columnar inputs.
   */
  public static Stream<String[]> columns(Path p) throws IOException {
    return lines(p).map(line -> line.split("\\s+"));
  }

  /**
   * Contents of expected file.
   */
  public Optional<String> expected(int day, int part, boolean test) throws IOException {
    var p = expectedPath(day, part, test);
    return exists(p) ? Optional.of(readString(p).trim()) : Optional.empty();
  }

}
