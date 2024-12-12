package com.gigamonkeys.aoc2024;

import static java.lang.Integer.*;
import static java.nio.file.Files.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Day05_PrintQueue implements Solution {

  private record Rule(int before, int after) {}

  public String part1(Path input) throws IOException {
    var cmp = byOrderingRules(rules(input));
    return String.valueOf(updates(input).stream().filter(u -> inOrder(u, cmp)).mapToInt(this::middle).sum());
  }

  public String part2(Path input) throws IOException {
    var cmp = byOrderingRules(rules(input));
    return String.valueOf(
      updates(input)
        .stream()
        .filter(u -> !inOrder(u, cmp))
        .map(lst -> lst.stream().sorted(cmp).toList())
        .mapToInt(this::middle)
        .sum()
    );
  }

  private Comparator<Integer> byOrderingRules(Set<Rule> rules) {
    return (a, b) -> rules.contains(new Rule(a, b)) ? -1 : rules.contains(new Rule(b, a)) ? 1 : 0;
  }

  private <T> boolean inOrder(List<T> list, Comparator<T> cmp) {
    return range(0, list.size() - 1).allMatch(i -> cmp.compare(list.get(i), list.get(i + 1)) < 0);
  }

  private <T> T middle(List<T> list) {
    return list.get(list.size() / 2);
  }

  private Set<Rule> rules(Path input) throws IOException {
    return lines(input)
      .filter(line -> line.matches("\\d+\\|\\d+"))
      .map(line -> line.split("\\|"))
      .map(pair -> new Rule(parseInt(pair[0]), parseInt(pair[1])))
      .collect(toSet());
  }

  private List<List<Integer>> updates(Path input) throws IOException {
    return lines(input)
      .filter(line -> line.matches("\\d+(,\\d+)*"))
      .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).toList())
      .toList();
  }
}
