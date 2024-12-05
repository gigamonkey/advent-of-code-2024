package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.*;
import static java.nio.file.Files.lines;
import static java.util.Collections.emptySet;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day05 extends Day {

  public String part1(Path input) throws IOException {
    var preceeding = preceeding(input);
    return String.valueOf(updates(input).stream().filter(u -> ordered(u, preceeding)).mapToInt(this::middle).sum());
  }

  public String part2(Path input) throws IOException {
    var preceeding = preceeding(input);
    var cmp = byOrderingRules(preceeding);

    return String.valueOf(
      updates(input)
        .stream()
        .filter(u -> !ordered(u, preceeding))
        .map(lst -> lst.stream().sorted(cmp).toList())
        .mapToInt(this::middle)
        .sum()
    );
  }

  private Comparator<Integer> byOrderingRules(Map<Integer, Set<Integer>> mustPreceed) {
    return (a, b) -> {
      if (mustPreceed.getOrDefault(a, emptySet()).contains(b)) {
        return -1;
      } else if (mustPreceed.getOrDefault(b, emptySet()).contains(a)) {
        return 1;
      } else {
        return 0;
      }
    };
  }

  private boolean ordered(List<Integer> u, Map<Integer, Set<Integer>> mustPreceed) {
    var seen = new HashSet<Integer>();
    for (int p : u) {
      var mp = mustPreceed.getOrDefault(p, emptySet()); // pages that we must come before
      if (seen.stream().anyMatch(p2 -> mp.contains(p2))) {
        // Pages that have already been printed include pages we must come before.
        return false;
      }
      seen.add(p);
    }
    return true;
  }

  private <T> T middle(List<T> list) {
    return list.get(list.size() / 2);
  }

  private Map<Integer, Set<Integer>> preceeding(Path input) throws IOException {
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
