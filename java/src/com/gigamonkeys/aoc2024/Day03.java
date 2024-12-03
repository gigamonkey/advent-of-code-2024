package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day03 extends Day {

  private static final Pattern MUL = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
  private static final Pattern MUL_2 = Pattern.compile(
    "(?:mul\\((\\d{1,3}),(\\d{1,3})\\))|(do(?:n't)?\\(\\))"
  );

  public Day03() {
    super(3);
  }

  private String data(boolean test) throws IOException {
    return text(input(test));
  }

  public String part1(boolean test) throws IOException {
    return String.valueOf(MUL.matcher(data(test)).results().mapToInt(this::mul).sum());
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
          total += mul(r);
        }
      }
    }
    return String.valueOf(total);
  }

  private int mul(MatchResult r) {
    return parseInt(r.group(1)) * parseInt(r.group(2));
  }
}
