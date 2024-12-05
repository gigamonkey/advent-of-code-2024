package com.gigamonkeys.aoc2024;

import static java.util.Collections.EMPTY_SET;
import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.*;
import static java.nio.file.Files.lines;
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
    var updates = updates(input);

    return String.valueOf(
      updates.stream().filter(u -> ordered(u, preceeding)).mapToInt(this::middle).sum()
    );
  }

  public String part2(Path input) throws IOException {
    var preceeding = preceeding(input);
    var updates = updates(input);

    var unordered = updates.stream().filter(u -> !ordered(u, preceeding)).toList();
    //System.out.println(unordered);

    var cmp = byOrder(preceeding);
    return String.valueOf(
      unordered.stream().map(lst -> {
          var srt = new ArrayList<Integer>(lst);
          Collections.sort(srt, cmp);
          return srt;
        }).mapToInt(this::middle).sum()
    );

  }

  private Comparator<Integer> byOrder(Map<Integer, Set<Integer>> mustPreceed) {
    return (a, b) -> {
      if (mustPreceed.getOrDefault(a, EMPTY_SET).contains(b)) {
        return -1;
      } else if (mustPreceed.getOrDefault(b, EMPTY_SET).contains(a)) {
        return 1;
      } else {
        return 0;
      }
    };
  }

  private boolean ordered(List<Integer> u, Map<Integer, Set<Integer>> mustPreceed) {
    var seen = new HashSet<Integer>();
    for (int p: u) {
      var mp = mustPreceed.get(p); // pages that we must come before
      if (mp != null && seen.stream().anyMatch(p2 -> mp.contains(p2))) {
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
      //.peek(System.out::println)
      .filter(line -> line.matches("\\d+\\|\\d+"))
      .map(line -> Arrays.stream(line.split("\\|")).map(Integer::valueOf).toList())
      .collect(groupingBy(pair -> pair.get(0), mapping(pair -> pair.get(1), toSet())));
  }

  private List<List<Integer>> updates(Path input) throws IOException {
    return lines(input)
      //.peek(System.out::println)
      .filter(line -> line.matches("\\d+(,\\d+)*"))
      .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).toList())
      .toList();
  }

}
