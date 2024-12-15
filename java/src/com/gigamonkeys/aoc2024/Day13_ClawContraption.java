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
import static java.util.stream.LongStream.range;
import static java.util.stream.LongStream.rangeClosed;
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
//import static java.util.stream.IntStream.*;
import java.util.stream.LongStream;
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

    //System.out.println(winner3(machines.get(2)));

    // var machine = new Machine(12, 54, 36, 27, 2052, 3024);

    var machine = new Machine(90, 51, 30, 59, 8370, 5625);
    System.out.println(machine + ": " + winner2(machine));
    System.out.println(machine + ": " + winner5(machine));
    System.exit(1);

    //var machine = new Machine(24, 97, 48, 13, 6624, 10120);
    //System.out.println(winner4(new Machine(24, 97, 48, 13, 6624, 10120)));
    //System.out.println(winner4(new Machine(94, 34, 22, 67, 8400, 5400)));

    //return String.valueOf(machines.stream().map(m -> winner2(m)).mapToLong(op -> op.map(Presses::cost).orElse(0L)).sum());

    // System.out.println("working");
    // machines.stream().forEach(m -> { System.out.println("%s %s".formatted(m, winner2(m))); });
    // System.out.println("not working");
    // machines.stream().forEach(m -> { System.out.println("%s %s".formatted(m, winner4(m))); });

    machines
      .stream()
      .forEach(m -> {
        var w2 = winner2(m);
        var w4 = winner5(m);
        if (w2.equals(w4)) {} else {
          System.out.println("FAIL %s got %s; expected: %s".formatted(m, w4, w2));
        }
      });

    //return String.valueOf(machines.stream().flatMap(m -> winner3(m).stream()).mapToLong(Presses::cost).sum());

    //return String.valueOf(machines.stream().map(m -> winner3(m)).mapToLong(op -> op.map(Presses::cost).orElse(0L)).sum());
    return String.valueOf(
      machines
        .stream()
        .map(m -> {
          return winner4(m);
        })
        .mapToLong(op -> op.map(Presses::cost).orElse(0L))
        .sum()
    );
  }

  public String part2(Path input) throws IOException {
    var machines = embiggen(machines(input));
    //return String.valueOf(machines.stream().map(m -> winner2(m)).mapToLong(op -> op.map(Presses::cost).orElse(0L)).sum());
    return String.valueOf(
      machines
        .stream()
        .map(m -> {
          System.out.println(m);
          return winner3(m);
        })
        .mapToLong(op -> op.map(Presses::cost).orElse(0L))
        .sum()
    );
  }

  private Optional<Presses> winner5(Machine m) {
    Iterator<Presses> xs = orderedPresses(m.ax, m.bx, m.x).iterator();
    Iterator<Presses> ys = orderedPresses(m.ay, m.by, m.y).iterator();

    if (!xs.hasNext() || !ys.hasNext()) {
      return Optional.empty();
    }

    Presses x = xs.next();
    Presses y = ys.next();
    while (true) {
      System.out.println("Comparing " + x + " (cost: " + x.cost() + ") and " + y + " (cost: " + y.cost() + ")");
      if (x.cost() < y.cost()) {
        if (!xs.hasNext()) return Optional.empty();
        x = xs.next();
      } else if (y.cost() < x.cost()) {
        if (!ys.hasNext()) return Optional.empty();
        y = ys.next();
      } else if (x.equals(y)) {
        return Optional.of(x);
      } else {
        if (!xs.hasNext() || !ys.hasNext()) {
          return Optional.empty();
        }
        x = xs.next();
        y = ys.next();
      }
    }
  }

  private Optional<Presses> winner3(Machine m) {
    Set<Presses> xs = allPresses(m.ax, m.bx, m.x).collect(toSet());
    Set<Presses> ys = allPresses(m.ay, m.by, m.y).collect(toSet());
    Set<Presses> intersection = new HashSet<>(xs);
    intersection.retainAll(ys);

    // System.out.println("xs: %s".formatted(xs));
    // System.out.println("ys: %s".formatted(ys));
    // System.out.println("both: %s".formatted(intersection));

    return intersection.stream().collect(minBy(Comparator.comparingLong(Presses::cost)));
  }

  private Optional<Presses> winner4(Machine m) {
    var xZeros = zeros(m.ax(), m.bx(), m.x());
    var yZeros = zeros(m.ay(), m.by(), m.y());

    if (xZeros.isEmpty() || yZeros.isEmpty()) {
      return Optional.empty();
    } else {
      MinAndGap x = oneAxis(m.ax(), m.bx(), xZeros.get());
      MinAndGap y = oneAxis(m.ay(), m.by(), yZeros.get());

      // System.out.println(x);
      // System.out.println(y);

      if (y.gap() == 0) { // || x.gap() == 0) {
        //System.out.println("ZERO GAP");
        System.out.println(m + " has zero gap");
        return Optional.empty();
      }

      var step = whichStep(x, y);
      // System.out.println("step: %d".formatted(step));
      var k = x.start() + step * x.sign();
      if (!x.range().contains(k)) {
        System.out.println("k " + k + " out of range");
        return Optional.empty();
      }

      var p = presses(m.ax(), m.bx(), xZeros.get(), k);
      if (!m.winner(p.a(), p.b())) {
        System.out.println(m + " is not a winner with " + p);
        return Optional.empty();
      }

      return Optional.of(p);
    }
  }

  record MinAndGap(long min, long gap, long start, long sign, Range range) {}

  private MinAndGap oneAxis(long a, long b, Pair zeros) {
    var r = kRange(a, b, zeros);
    var lowPresses = presses(a, b, zeros, r.low());
    var highPresses = presses(a, b, zeros, r.high());
    //System.out.println("r: %s; lowPresses: %s; highPresses: %s".formatted(r, lowPresses, highPresses));

    var gap = abs(presses(a, b, zeros, r.low() + 1).cost() - lowPresses.cost());
    var lowCost = lowPresses.cost();
    var highCost = highPresses.cost();
    if (lowCost < highCost) {
      return new MinAndGap(lowCost, gap, r.low(), 1, r);
    } else {
      return new MinAndGap(highCost, gap, r.high(), -1, r);
    }
  }

  private long whichStep(MinAndGap x, MinAndGap y) {
    var ext = extendedEuclidean(x.gap(), y.gap());
    var a = ext.x();
    var c = y.gap() - ((x.min() - y.min()) % y.gap());
    return floorMod((a * c), y.gap());
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
        //if (a % 100_000_000L == 0) System.out.print(".");
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
      })
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

  record Extended(long gcd, long x, long y) {}

  private long gcd(long a, long b) {
    var r = Math.floorMod(a, b);
    if (r != 0) {
      return gcd(b, r);
    } else {
      return b;
    }
  }

  private Extended extendedEuclidean(long a, long b) {
    if (a == 0) {
      return new Extended(b, 0, 1);
    } else {
      var ext = extendedEuclidean(b % a, a);
      var x = ext.y - ((long) floor(b / a) * ext.x);
      var y = ext.x;
      return new Extended(ext.gcd, x, y);
    }
  }

  private Optional<Pair> zeros(long a, long b, long goal) {
    var ext = extendedEuclidean(a, b);
    if (goal % ext.gcd != 0) {
      return Optional.empty();
    } else {
      var scale = goal / ext.gcd;
      return Optional.of(new Pair(scale * ext.x, scale * ext.y));
    }
  }

  record Pair(long x, long y) {}

  record Range(long low, long high) {
    boolean contains(long value) {
      return low <= value && value <= high;
    }
  }

  private Range kRange(long a, long b, Pair zeros) {
    var gcd = gcd(a, b);
    var one = (long) ceil(-(zeros.x() / (b / gcd)));
    var two = (long) floor(zeros.y() / (a / gcd));
    return new Range(min(one, two), max(one, two));
  }

  private Presses presses(long a, long b, Pair zeros, long k) {
    var gcd = gcd(a, b);
    return new Presses(zeros.x + k * (b / gcd), zeros.y - k * (a / gcd));
  }

  private Stream<Presses> allPresses(long a, long b, long goal) {
    return zeros(a, b, goal)
      .stream()
      .flatMap(zeros -> {
        var kRange = kRange(a, b, zeros);
        return rangeClosed(kRange.low, kRange.high).mapToObj(k -> presses(a, b, zeros, k));
      });
  }

  private Stream<Presses> orderedPresses(long a, long b, long goal) {
    return zeros(a, b, goal)
      .stream()
      .flatMap(zeros -> {
        var kRange = kRange(a, b, zeros);
        var low = presses(a, b, zeros, kRange.low());
        var high = presses(a, b, zeros, kRange.high());
        LongStream ks = low.cost() < high.cost()
          ? fromTo(kRange.low(), kRange.high())
          : fromTo(kRange.high(), kRange.low());
        return ks.mapToObj(k -> presses(a, b, zeros, k));
      });
  }

  private LongStream fromTo(long start, long end) {
    long incr = (long) signum(end - start);
    LongPredicate pred = incr == 1 ? i -> i <= end : i -> i >= end;
    return LongStream.iterate(start, pred, i -> i + incr);
  }
}
