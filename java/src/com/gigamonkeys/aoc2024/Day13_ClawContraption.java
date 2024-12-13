package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Direction.*;
import static com.gigamonkeys.aoc2024.Util.*;
import static com.gigamonkeys.aoc2024.Util.characterGrid;
import static com.gigamonkeys.aoc2024.Util.columns;
import static com.gigamonkeys.aoc2024.Util.text;
import static java.lang.Integer.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.System.*;
import static java.lang.System.nanoTime;
import static java.nio.file.Files.*;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.*;
import static java.util.Arrays.stream;
import static java.util.Map.Entry;
import static java.util.Map.entry;
import static java.util.function.Function.*;
import static java.util.regex.Pattern.*;
import static java.util.stream.Collectors.*;
//import static java.util.stream.IntStream.*;
import java.util.stream.LongStream;
import static java.util.stream.LongStream.range;
import static java.util.stream.Stream.*;
import static java.util.stream.Stream.generate;

import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.*;
import java.util.regex.MatchResult;
import java.util.stream.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13_ClawContraption implements Solution {

  private record Point(long x, long y) {}

  private record Presses(long a, long b) {
    long cost() {
      return a * 3 + b;
    }
  }

  private record Machine(long ax, long ay, long bx, long by, long x, long y) {
    Point position(long a, long b) {
      return new Point(ax * a + bx * b, ay * a + by * b);
    }

    boolean winner(long a, long b) {
      return x == (ax * a + bx * b) && y == (ay * a + by * b);
    }
  }

  private static final Pattern button = Pattern.compile("Button .: X\\+(\\d+), Y\\+(\\d+)");
  private static final Pattern prize = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

  public String part1(Path input) throws IOException {
    var machines = machines(input);
    return String.valueOf(machines.stream().map(m -> winner2(m)).mapToLong(op -> op.map(Presses::cost).orElse(0L)).sum());
  }

  public String part2(Path input) throws IOException {
    var machines = embiggen(machines(input));
    return String.valueOf(machines.stream().map(m -> winner2(m)).mapToLong(op -> op.map(Presses::cost).orElse(0L)).sum());

  }

  private Optional<Presses> winner(Machine m) {
    long maxX = (long) max(ceil(m.x / m.ax), ceil(m.x / m.bx));
    long maxY = (long) max(ceil(m.y / m.ay), ceil(m.y / m.by));
    //System.out.println("maxX: %d; maxY: %d".formatted(maxX, maxY));
    return range(0, maxX)
      .boxed()
      .flatMap(a -> range(0, maxY).filter(b -> m.winner(a, b)).mapToObj(b -> new Presses(a, b)))
      .collect(minBy(Comparator.comparingLong(Presses::cost)));
  }

  private Optional<Presses> winner2(Machine m) {
    long maxX = (long) max(ceil(m.x / m.ax), ceil(m.x / m.bx));
    long maxY = (long) max(ceil(m.y / m.ay), ceil(m.y / m.by));
    //System.out.println("maxX: %d; maxY: %d".formatted(maxX, maxY));
    return range(0, maxX)
      .boxed()
      .map(a -> {
          if (a % 100_000_000L == 0) System.out.print(".");
          var xLeft = m.x - (m.ax * a);
          var yLeft = m.y - (m.ay * a);
          if (xLeft % m.bx == 0 && yLeft % m.by == 0) {
            var bForX = xLeft / m.bx;
            var bForY = yLeft / m.by;
            if (bForX == bForY) {
              return new Presses(a, bForX);
            }
          }
          return null;
        }
      )
      .filter(p -> p != null)
      .collect(minBy(Comparator.comparingLong(Presses::cost)));
  }



  private List<Machine> embiggen(List<Machine> machines) {
    long amt = 10000000000000L;

    return machines.stream().map(m -> new Machine(m.ax, m.ay, m.by, m.by, m.x + amt, m.y + amt)).toList();
  }

  private List<Machine> machines(Path input) throws IOException {
    String text = text(input);
    String[] chunks = text.split("\n\n");

    return stream(chunks)
      .map(chunk -> {
        var m = button.matcher(chunk);
        m.find();
        long ax = Long.parseLong(m.group(1));
        long ay = Long.parseLong(m.group(2));
        m.find();
        long bx = Long.parseLong(m.group(1));
        long by = Long.parseLong(m.group(2));
        var m2 = prize.matcher(chunk);
        m2.find();
        long x = Long.parseLong(m2.group(1));
        long y = Long.parseLong(m2.group(2));
        return new Machine(ax, ay, bx, by, x, y);
      })
      .toList();
  }
}
