package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.lang.Integer.parseInt;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class BridgeRepair implements Solution {

  Pattern p = Pattern.compile("^(\\d+):\\s+(\\d+(\\s+\\d+)*)$");

  record Equation(int value, List<Integer> numbers) {

    boolean solveable() {
      return solutions(numbers.reversed()).anyMatch(n -> n == value);
    }
  }

  static Stream<Integer> solutions(List<Integer> numbers) {
      var first = numbers.getFirst();
      if (numbers.size() == 1) {
        return Stream.of(first);
      } else {
        var rest = numbers.subList(1, numbers.size());
        return Stream.concat(
          solutions(rest).map(s -> first + s),
          solutions(rest).map(s -> first * s));
      }
  }

  public String part1(Path input) throws IOException {
    List<Equation> equations = lines(input).map(this::parseLine).toList();
    return String.valueOf(equations.stream().filter(eq -> eq.solveable()).mapToInt(Equation::value).sum());
  }

  public String part2(Path input) throws IOException {
    return String.valueOf("nyi");
  }


  private Equation parseLine(String line) {
    Matcher m = p.matcher(line);
    if (m.matches()) {
      return new Equation(
        parseInt(m.group(1)),
        Arrays.stream(m.group(2).split("\\s+")).map(Integer::parseInt).toList());
    } else {
      throw new RuntimeException("Bad line: '%s'".formatted(line));
    }
  }

  public static void main(String[] args) {
    solutions(List.of(30, 20, 10)).forEach(System.out::println);
  }

}
