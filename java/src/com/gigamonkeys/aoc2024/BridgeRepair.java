package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.lines;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.regex.*;

public class BridgeRepair implements Solution {

  private static final List<Op> ALL_OPS = List.of(
    new Op(0, (a, b) -> a + b),
    new Op(1, (a, b) -> a * b),
    new Op(0, (a, b) -> Long.valueOf("" + a + b))
  );

  private static final Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(long value, List<Long> numbers) {}

  record Op(long identity, BinaryOperator<Long> op) {
    public Optional<Long> apply(Optional<Long> soFar, long next) {
      return Optional.of(op.apply(soFar.orElse(identity), next));
    }
  }

  public String part1(Path input) throws IOException {
    return solve(input, ALL_OPS.subList(0, 2));
  }

  public String part2(Path input) throws IOException {
    return solve(input, ALL_OPS);
  }

  private String solve(Path input, List<Op> ops) throws IOException {
    return String.valueOf(
      lines(input)
        .map(this::parseLine)
        .filter(eq -> check(eq.value(), eq.numbers(), Optional.empty(), ops))
        .mapToLong(Equation::value)
        .sum()
    );
  }

  private boolean check(long value, List<Long> nums, Optional<Long> soFar, List<Op> ops) {
    if (nums.size() == 0) {
      return soFar.map(n -> n == value).orElse(false);
    } else {
      var rest = nums.subList(1, nums.size());
      return ops
        .stream()
        .anyMatch(op -> {
          return check(value, rest, op.apply(soFar, nums.get(0)), ops);
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
