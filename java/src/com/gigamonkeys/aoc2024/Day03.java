package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.parseInt;
import static java.lang.Math.*;
import static java.nio.file.Files.lines;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day03 extends Day {

  private static Pattern MUL = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
  private static Pattern MUL_2 = Pattern.compile(
    "(?:mul\\((\\d{1,3}),(\\d{1,3})\\))|(do(?:n't)?\\(\\))"
  );

  public Day03() {
    super(3);
  }

  private String data(boolean test) throws IOException {
    return text(input(test));
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(
      MUL.matcher(data(test))
        .results()
        .mapToInt(r -> parseInt(r.group(1)) * parseInt(r.group(2)))
        .sum()
    );
  }

  public String part2(boolean test) throws IOException {
    var testdata = text(test ? Path.of("inputs/day-03/test2.txt") : input(test));
    var enabled = true;
    var total = 0;
    var i = MUL_2.matcher(testdata).results().iterator();
    while (i.hasNext()) {
      var r = i.next();
      if (r.group(3) != null) {
        enabled = r.group(3).equals("do()");
      } else {
        if (enabled) {
          total += parseInt(r.group(1)) * parseInt(r.group(2));
        }
      }
    }
    return String.valueOf(total);
  }
}
