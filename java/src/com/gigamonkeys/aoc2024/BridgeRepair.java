package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.lang.Integer.parseInt;

import java.math.BigInteger;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class BridgeRepair implements Solution {

  Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(BigInteger value, List<BigInteger> numbers) {

    boolean solveable() {
      return solutions(numbers).anyMatch(n -> n.equals(value));
    }
    boolean solveable2() {
      return solutions2(numbers).anyMatch(n -> n.equals(value));
    }

  }

  static Stream<BigInteger> solutions(List<BigInteger> numbers) {
      var first = numbers.getFirst();
      if (numbers.size() == 1) {
        return Stream.of(first);
      } else {
        var second = numbers.get(1);
        var rest = numbers.subList(2, numbers.size());
        return Stream.concat(
          solutions(Stream.concat(Stream.of(first.add(second)), rest.stream()).toList()),
          solutions(Stream.concat(Stream.of(first.multiply(second)), rest.stream()).toList()));
      }
  }

  static Stream<BigInteger> solutions2(List<BigInteger> numbers) {
      var first = numbers.getFirst();
      if (numbers.size() == 1) {
        return Stream.of(first);
      } else {
        var second = numbers.get(1);
        var rest = numbers.subList(2, numbers.size());
        return
          Stream.concat(
            Stream.concat(
              solutions2(Stream.concat(Stream.of(first.add(second)), rest.stream()).toList()),
              solutions2(Stream.concat(Stream.of(first.multiply(second)), rest.stream()).toList())),
            solutions2(Stream.concat(Stream.of(concat(first, second)), rest.stream()).toList()));
      }
  }

  static BigInteger concat(BigInteger a, BigInteger b) {
    return new BigInteger(a.toString() + b.toString());
  }

  public String part1(Path input) throws IOException {
    List<Equation> equations = lines(input).map(this::parseLine).toList();
    var sum = equations
      .stream()
      .filter(eq -> eq.solveable())
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add);

    return String.valueOf(sum);
  }

  public String part2(Path input) throws IOException {
    List<Equation> equations = lines(input).map(this::parseLine).toList();
    var sum = equations
      .stream()
      .filter(eq -> eq.solveable2())
      .map(Equation::value)
      .reduce(BigInteger.ZERO, BigInteger::add);

    return String.valueOf(sum);
  }


  private Equation parseLine(String line) {
    Matcher m = p.matcher(line);
    if (m.matches()) {
      return new Equation(
        new BigInteger(m.group(1)),
        Arrays.stream(m.group(2).split("\\s+")).map(BigInteger::new).toList());
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }

  public static void main(String[] args) {
    //solutions(List.of(30, 20, 10)).forEach(System.out::println);
  }

}
