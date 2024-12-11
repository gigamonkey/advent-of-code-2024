package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;

import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import java.util.*;
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

  public String part1(Path input) throws IOException {
    List<Long> list = Arrays.stream(text(input).split("\\s+")).map(Long::parseLong).toList();
    for (int i = 0; i < 25; i++) {
      list = list.stream().flatMap(this::replacements).toList();
      // if (i < 6) {
      //   System.out.println(list);
      // }
    }
    return String.valueOf(list.size());
  }

  record Key(long n, int iterations) {}

  private final Map<Key, Long> cache = new HashMap<>();

  public String part2(Path input) throws IOException {
    List<Long> list = Arrays.stream(text(input).split("\\s+")).map(Long::parseLong).toList();

    return String.valueOf(list.stream().map(n -> new Key(n, 75)).mapToLong(this::number).sum());
  }


  private long number(Key key) {
    int iters = key.iterations();
    if (iters == 0) {
      return 1;
    } else {
      if (!cache.containsKey(key)) {
        long count = replacements(key.n()).map(n -> new Key(n, iters - 1)).mapToLong(this::number).sum();
        cache.put(key, count);
      }
      return cache.get(key);
    }
  }

  private Stream<Long> replacements(long n) {
    if (n < 0) throw new Error("n is negative: " + n);
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

  private long digits(long n) {
    //return n == 0 ? 1 : (int) ceil(log10(n));
    return String.valueOf(n).length();
  }

}
