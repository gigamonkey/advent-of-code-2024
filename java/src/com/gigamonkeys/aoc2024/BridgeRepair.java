package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.lines;
import static java.util.Arrays.stream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.regex.Pattern;

public class BridgeRepair implements Solution {

  private static final Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  private static final List<BinaryOperator<Long>> ALL_OPS = List.of(
    (a, b) -> a + b,
    (a, b) -> a * b,
    (a, b) -> Long.parseLong("" + a + b)
  );

  record Equation(long value, List<Long> numbers) {
    boolean check(List<BinaryOperator<Long>> ops) {
      var first = numbers.get(0);
      var rest = numbers.subList(1, numbers.size());
      return BridgeRepair.check(value, first, rest, ops);
    }
  }

  public String part1(Path input) throws IOException {
    return solve(input, ALL_OPS.subList(0, 2));
  }

  public String part2(Path input) throws IOException {
    return solve(input, ALL_OPS);
  }

  private String solve(Path input, List<BinaryOperator<Long>> ops) throws IOException {
    return String.valueOf(
      lines(input).parallel().map(this::parseLine).filter(eq -> eq.check(ops)).mapToLong(Equation::value).sum()
    );
  }

  private static boolean check(long value, long soFar, List<Long> nums, List<BinaryOperator<Long>> ops) {
    if (nums.size() == 0) {
      return soFar == value;
    } else {
      var rest = nums.subList(1, nums.size());
      return ops.stream().anyMatch(op -> check(value, op.apply(soFar, nums.get(0)), rest, ops));
    }
  }

  private Equation parseLine(String line) {
    var m = p.matcher(line);
    if (m.matches()) {
      return new Equation(Long.parseLong(m.group(1)), stream(m.group(2).split("\\s+")).map(Long::valueOf).toList());
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }
}
