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

  record Equation(BigInteger value, List<BigInteger> numbers) {
    boolean solveable(List<BinaryOperator<BigInteger>> ops) {
      return solutions(numbers, ops).anyMatch(value::equals);
    }
  }

  record Op(BigInteger identity, BinaryOperator<BigInteger> op) {}

  public String part1(Path input) throws IOException {
    //return solve(input, List.of(BigInteger::add, BigInteger::multiply));
    return solve2(input, ALL_OPS.subList(0, 2));
  }

  public String part2(Path input) throws IOException {
    //return solve(input, List.of(BigInteger::add, BigInteger::multiply, BridgeRepair::concat));
    return solve2(input, ALL_OPS);
  }

  private boolean check(BigInteger value, List<BigInteger> nums, Optional<BigInteger> soFar) {
    if (nums.size() == 0) {
      return soFar.map(n -> n.equals(value)).orElse(false);
    } else {
      var rest = nums.subList(1, nums.size());
      return (
        check(value, rest, soFar.or(() -> Optional.of(BigInteger.ZERO)).map(n -> n.add(nums.get(0)))) ||
        check(value, rest, soFar.or(() -> Optional.of(BigInteger.ONE)).map(n -> n.multiply(nums.get(0))))
      );
    }
  }

  private boolean check2(BigInteger value, List<BigInteger> nums, Optional<BigInteger> soFar) {
    if (nums.size() == 0) {
      return soFar.map(n -> n.equals(value)).orElse(false);
    } else {
      var rest = nums.subList(1, nums.size());
      return (
        check2(value, rest, soFar.or(() -> Optional.of(BigInteger.ZERO)).map(n -> n.add(nums.get(0)))) ||
        check2(value, rest, soFar.or(() -> Optional.of(BigInteger.ONE)).map(n -> n.multiply(nums.get(0)))) ||
        check2(value, rest, soFar.or(() -> Optional.of(BigInteger.ZERO)).map(n -> concat(n, nums.get(0))))
      );
    }
  }

  private String solve2(Path input, List<Op> ops) throws IOException {
    return lines(input)
      .map(this::parseLine)
      .filter(eq -> check3(eq.value(), eq.numbers(), Optional.empty(), ops))
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add)
      .toString();
  }

  private boolean check3(BigInteger value, List<BigInteger> nums, Optional<BigInteger> soFar, List<Op> ops) {
    if (nums.size() == 0) {
      return soFar.map(n -> n.equals(value)).orElse(false);
    } else {
      var rest = nums.subList(1, nums.size());
      return ops.stream().anyMatch(op -> {
          return check3(
            value,
            rest,
            soFar.or(() -> Optional.of(op.identity())).map(n -> op.op().apply(n, nums.get(0))),
            ops);
        });
    }
  }

  private String solve(Path input, List<BinaryOperator<BigInteger>> ops) throws IOException {
    return lines(input)
      .map(this::parseLine)
      .filter(eq -> eq.solveable(ops))
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add)
      .toString();
  }

  private static Stream<BigInteger> solutions(List<BigInteger> numbers, List<BinaryOperator<BigInteger>> ops) {
    if (numbers.size() == 1) {
      return numbers.stream();
    } else {
      var first = numbers.get(0);
      var second = numbers.get(1);
      var rest = numbers.subList(2, numbers.size());
      return ops.stream().flatMap(op -> solutions(cons(op.apply(first, second), rest), ops));
    }
  }

  private static <T> List<T> cons(T first, List<T> rest) {
    List<T> foo = new ArrayList<>();
    foo.add(first);
    foo.addAll(rest);
    return foo;
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
