package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.*;
import static java.nio.file.Files.lines;
import static java.util.Collections.emptySet;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day05 implements Solution {

  public String part1(Path input) throws IOException {
    var mustPreceed = mustPreceed(input);
    var cmp = byOrderingRules(mustPreceed);
    return String.valueOf(updates(input).stream().filter(u -> inOrder(u, cmp)).mapToInt(this::middle).sum());
  }

  public String part2(Path input) throws IOException {
    var mustPreceed = mustPreceed(input);
    var cmp = byOrderingRules(mustPreceed);
    return String.valueOf(
      updates(input)
        .stream()
        .filter(u -> !inOrder(u, cmp))
        .map(lst -> lst.stream().sorted(cmp).toList())
        .mapToInt(this::middle)
        .sum()
    );
  }

  private Comparator<Integer> byOrderingRules(Map<Integer, Set<Integer>> mustPreceed) {
    return (a, b) ->
      mustPreceed.getOrDefault(a, emptySet()).contains(b)
        ? -1
        : mustPreceed.getOrDefault(b, emptySet()).contains(a)
        ? 1
        : 0;
  }

  private <T> boolean inOrder(List<T> list, Comparator<T> cmp) {
    return range(0, list.size() - 1).allMatch(i -> cmp.compare(list.get(i), list.get(i + 1)) < 0);
  }

  private <T> T middle(List<T> list) {
    return list.get(list.size() / 2);
  }

  private Map<Integer, Set<Integer>> mustPreceed(Path input) throws IOException {
    return lines(input)
      .filter(line -> line.matches("\\d+\\|\\d+"))
      .map(line -> Arrays.stream(line.split("\\|")).map(Integer::valueOf).toList())
      .collect(groupingBy(pair -> pair.get(0), mapping(pair -> pair.get(1), toSet())));
  }

  private List<List<Integer>> updates(Path input) throws IOException {
    return lines(input)
      .filter(line -> line.matches("\\d+(,\\d+)*"))
      .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).toList())
      .toList();
  }
}
