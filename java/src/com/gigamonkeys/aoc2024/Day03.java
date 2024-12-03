package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.nio.file.Files.lines;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static java.lang.Integer.parseInt;

import java.util.regex.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day03 extends Day {

  private static Pattern MUL = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

  public Day03() {
    super(3);
  }

  private String data(boolean test) throws IOException {
    return text(input(test));
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(MUL.matcher(data(test)).results().mapToInt(r -> parseInt(r.group(1)) * parseInt(r.group(2))).sum());
  }

  public String part2(boolean test) throws IOException {
    return "";
  }

}
