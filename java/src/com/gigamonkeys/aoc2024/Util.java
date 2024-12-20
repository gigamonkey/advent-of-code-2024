package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Util {

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

  public static int[][] characterGrid(Path p) throws IOException {
    return lines(p).map(line -> line.codePoints().toArray()).toArray(int[][]::new);
  }

  public static int[][] digitGrid(Path p) throws IOException {
    return lines(p).map(line -> line.codePoints().map(cp -> Character.digit(cp, 10)).toArray()).toArray(int[][]::new);
  }

  public static List<Long> longs(Path input) throws IOException {
    return Arrays.stream(text(input).split("\\s+")).map(Long::parseLong).toList();
  }

  public static long gcd(long a, long b) {
    var r = Math.floorMod(a, b);
    if (r != 0) {
      return gcd(b, r);
    } else {
      return b;
    }
  }

}
