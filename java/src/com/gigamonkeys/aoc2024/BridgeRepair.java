package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.lines;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.regex.*;

public class BridgeRepair implements Solution {

  private static final Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  private static final List<BinaryOperator<Long>> ALL_OPS = List.of(
    (a, b) -> a + b,
    (a, b) -> a * b,
    (a, b) -> Long.valueOf("" + a + b)
  );

  record Equation(long value, List<Long> numbers) {}

  public String part1(Path input) throws IOException {
    return solve(input, ALL_OPS.subList(0, 2));
  }

  public String part2(Path input) throws IOException {
    return solve(input, ALL_OPS);
  }

  private String solve(Path input, List<BinaryOperator<Long>> ops) throws IOException {
    return String.valueOf(
      lines(input)
        .map(this::parseLine)
        .filter(eq -> {
          var first = eq.numbers().get(0);
          var rest = eq.numbers().subList(1, eq.numbers().size());
          return check(eq.value(), first, rest, ops);
        })
        .mapToLong(Equation::value)
        .sum()
    );
  }

  private boolean check(long value, long soFar, List<Long> nums, List<BinaryOperator<Long>> ops) {
    if (nums.size() == 0) {
      return soFar == value;
    } else {
      var rest = nums.subList(1, nums.size());
      return ops
        .stream()
        .anyMatch(op -> {
          return check(value, op.apply(soFar, nums.get(0)), rest, ops);
        });
    }
  }

  private Equation parseLine(String line) {
    Matcher m = p.matcher(line);
    if (m.matches()) {
      return new Equation(
        Long.valueOf(m.group(1)),
        Arrays.stream(m.group(2).split("\\s+")).map(Long::valueOf).toList()
      );
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }
}
