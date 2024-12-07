package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

public class BridgeRepair implements Solution {

  Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(BigInteger value, List<BigInteger> numbers) {
    boolean solveable(List<BinaryOperator<BigInteger>> ops) {
      return solutions(numbers, ops).anyMatch(value::equals);
    }
  }

  public String part1(Path input) throws IOException {
    return solve(input, List.of(BigInteger::add, BigInteger::multiply));
  }

  public String part2(Path input) throws IOException {
    return solve(input, List.of(BigInteger::add, BigInteger::multiply, BridgeRepair::concat));
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
    var first = numbers.getFirst();
    if (numbers.size() == 1) {
      return Stream.of(first);
    } else {
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
