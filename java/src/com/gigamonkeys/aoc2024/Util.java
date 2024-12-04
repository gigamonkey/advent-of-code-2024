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
}
