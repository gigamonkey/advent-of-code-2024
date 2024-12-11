package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day11_PlutonianPebbles implements Solution {

  /*

    If the stone is engraved with the number 0, it is replaced by a stone
    engraved with the number 1.

    If the stone is engraved with a number that has an even number of digits, it
    is replaced by two stones. The left half of the digits are engraved on the
    new left stone, and the right half of the digits are engraved on the new
    right stone. (The new numbers don't keep extra leading zeroes: 1000 would
    become stones 10 and 0.)

    If none of the other rules apply, the stone is replaced by a new stone; the
    old stone's number multiplied by 2024 is engraved on the new stone.

  */

  record Key(long n, int iterations) {}

  private final Map<Key, Long> cache = new HashMap<>();

  public String part1(Path input) throws IOException {
    return String.valueOf(solve(longs(input), 25));
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(solve(longs(input), 75));
  }

  private long solve(List<Long> nums, int iters) {
    return nums.stream().map(n -> new Key(n, iters)).mapToLong(this::number).sum();
  }

  private long number(Key key) {
    switch (key) {
      case Key(var num, var iters) -> {
        if (iters == 0) {
          return 1;
        } else {
          if (!cache.containsKey(key)) {
            long count = replacements(num).map(n -> new Key(n, iters - 1)).mapToLong(this::number).sum();
            cache.put(key, count);
          }
          return cache.get(key);
        }
      }
    }
  }

  private Stream<Long> replacements(long n) {
    if (n == 0) {
      return Stream.of(1L);
    } else {
      var d = digits(n);
      if (d % 2 == 0) {
        var places = (long) Math.pow(10, d / 2);
        var left = n / places;
        var right = n % places;
        return Stream.of(left, right);
      } else {
        return Stream.of(n * 2024);
      }
    }
  }

  private int digits(long n) {
    return (int) floor(log10(n)) + 1;
  }
}
