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
    boolean solveable(Function<List<BigInteger>, Stream<BigInteger>> solutions) {
      return solutions.apply(numbers).anyMatch(n -> n.equals(value));
    }
  }

  static Stream<BigInteger> solutions(List<BigInteger> numbers) {
    var first = numbers.getFirst();
    if (numbers.size() == 1) {
      return Stream.of(first);
    } else {
      var second = numbers.get(1);
      var rest = numbers.subList(2, numbers.size());
      return Stream.concat(solutions(cons(first.add(second), rest)), solutions(cons(first.multiply(second), rest)));
    }
  }

  static Stream<BigInteger> solutions2(List<BigInteger> numbers) {
    BigInteger first = numbers.getFirst();
    if (numbers.size() == 1) {
      return Stream.of(first);
    } else {
      BigInteger second = numbers.get(1);
      List<BigInteger> rest = numbers.subList(2, numbers.size());
      return Stream.concat(
        Stream.concat(solutions2(cons(first.add(second), rest)), solutions2(cons(first.multiply(second), rest))),
        solutions2(cons(concat(first, second), rest))
      );
    }
  }

  static <T> List<T> cons(T first, List<T> rest) {
    List<T> foo = new ArrayList<>();
    foo.add(first);
    foo.addAll(rest);
    return foo;
  }

  static BigInteger concat(BigInteger a, BigInteger b) {
    return new BigInteger(a.toString() + b.toString());
  }

  public String part1(Path input) throws IOException {
    return solve(input, BridgeRepair::solutions);
  }

  public String part2(Path input) throws IOException {
    return solve(input, BridgeRepair::solutions2);
  }

  private String solve(Path input, Function<List<BigInteger>, Stream<BigInteger>> solutions) throws IOException {
    return lines(input)
      .map(this::parseLine)
      .filter(eq -> eq.solveable(solutions))
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add)
      .toString();
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

  public static void main(String[] args) {
    //solutions(List.of(30, 20, 10)).forEach(System.out::println);
  }
}
