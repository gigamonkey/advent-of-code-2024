package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;

import java.util.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Util {

  /**
   * Get the name of the input file for a given day and part and whether it's
   * the test input or the real input.
   */
  public static Path input(int day, int part, boolean test) {
    return Path.of("inputs/day-%02d-part-%d%s.txt".formatted(day, part, test ? "-test" : ""));
  }

  /**
   * Stream of whitespace delimited columnar inputs.
   */
  public static Stream<String[]> columns(Path p) throws IOException {
    return lines(p).map(line -> line.split("\\s+"));
  }

}
