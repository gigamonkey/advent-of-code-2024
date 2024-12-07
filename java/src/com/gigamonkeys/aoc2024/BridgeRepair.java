package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.regex.*;
import java.util.stream.Stream;

public class BridgeRepair implements Solution {

  private static final List<Op> ALL_OPS = List.of(
        new Op(BigInteger.ZERO, BigInteger::add),
        new Op(BigInteger.ONE, BigInteger::multiply),
        new Op(BigInteger.ZERO, BridgeRepair::concat)
    );


  Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(BigInteger value, List<BigInteger> numbers) {}

  record Op(BigInteger identity, BinaryOperator<BigInteger> op) {}

  public String part1(Path input) throws IOException {
    return solve(input, ALL_OPS.subList(0, 2));
  }

  public String part2(Path input) throws IOException {
    return solve(input, ALL_OPS);
  }

  private String solve(Path input, List<Op> ops) throws IOException {
    return lines(input)
      .map(this::parseLine)
      .filter(eq -> check(eq.value(), eq.numbers(), Optional.empty(), ops))
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add)
      .toString();
  }

  private boolean check(BigInteger value, List<BigInteger> nums, Optional<BigInteger> soFar, List<Op> ops) {
    if (nums.size() == 0) {
      return soFar.map(n -> n.equals(value)).orElse(false);
    } else {
      var rest = nums.subList(1, nums.size());
      return ops.stream().anyMatch(op -> {
          return check(
            value,
            rest,
            soFar.or(() -> Optional.of(op.identity())).map(n -> op.op().apply(n, nums.get(0))),
            ops);
        });
    }
  }


  private static BigInteger concat(BigInteger a, BigInteger b) {
    return new BigInteger(a.toString() + b.toString());
  }

  private Equation parseLine(String line) {
    Matcher m = p.matcher(line);
    if (m.matches()) {
      return new Equation(
        new BigInteger(m.group(1)),
        Arrays.stream(m.group(2).split("\\s+")).map(BigInteger::new).toList()
      );
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }
}
