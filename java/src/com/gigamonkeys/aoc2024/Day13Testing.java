package com.gigamonkeys.aoc2024;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.LongPredicate;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.LongStream.*;

public class Day13Testing {

  private static final Pattern BUTTON_PATTERN = Pattern.compile("Button .: X\\+(\\d+), Y\\+(\\d+)");

  private static final Pattern PRIZE_PATTERN = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

  private record Pair(long x, long y) {}

  private record Point(long x, long y) {}

  private record Extended(long gcd, long x, long y) {}

  private record MinAndGap(long min, long gap, long start, long sign, Range range) {}

  private record Presses(long a, long b) {
    long cost() {
      return a * 3 + b;
    }
  }

  private record Range(long low, long high) {
    boolean contains(long value) {
      return low <= value && value <= high;
    }
  }

  private record Machine(long ax, long ay, long bx, long by, long x, long y) {
    Point position(long a, long b) {
      return new Point(ax * a + bx * b, ay * a + by * b);
    }

    boolean winner(long a, long b) {
      return x == (ax * a + bx * b) && y == (ay * a + by * b);
    }

    private Machine embiggen() {
      long amt = 10000000000000L;
      return new Machine(ax, ay, by, by, x + amt, y + amt);
    }
  }

  private List<Machine> machines(Path input) throws IOException {
    String text = text(input);
    String[] chunks = text.split("\n\n");

    return stream(chunks).map(chunk -> {
      var m = BUTTON_PATTERN.matcher(chunk);
      m.find();
      long ax = Long.parseLong(m.group(1));
      long ay = Long.parseLong(m.group(2));
      m.find();
      long bx = Long.parseLong(m.group(1));
      long by = Long.parseLong(m.group(2));
      var m2 = PRIZE_PATTERN.matcher(chunk);
      m2.find();
      long x = Long.parseLong(m2.group(1));
      long y = Long.parseLong(m2.group(2));
      return new Machine(ax, ay, bx, by, x, y);
    }).toList();
  }

  private List<Machine> embiggen(List<Machine> machines) {
    return machines.stream().map(Machine::embiggen).toList();
  }

  private void testMachines() {
    var machines = List.of(new Machine(12, 54, 36, 27, 2052, 3024), new Machine(90, 51, 30, 59, 8370, 5625));

    machines.stream().forEach(m -> {
      var good = bruteForce(m);
      var maybe = maybeSmart(m);
      if (!good.equals(maybe)) {
        System.out.println("FAIL %s got %s; expected: %s".formatted(m, maybe, good));
      }
    });

  }

  /**
   * Brute force solution: iterate through the all the possible set of presses
   * that match up on the X and Y axes and then find the minimum cost one. This
   * doesn't work for really big x,y values.
   *
   * @param m
   * @return
   */
  private static Optional<Presses> bruteForce(Machine m) {
    long maxX = (long) max(ceil(m.x / m.ax), ceil(m.x / m.bx));
    return range(0, maxX).boxed().map(a -> {
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
    }).filter(p -> p != null).collect(minBy(Comparator.comparingLong(Presses::cost)));
  }

  private Optional<Presses> maybeSmart(Machine m) {
    // These are ordered by cost. Since everything is linear, whatever direction
    // the a value is going in each one it will keep going and by the same
    // amount, I think. Which means we should be able to figure out when, if
    // ever, the two a values are going to be the same. At that point either the
    Iterator<Presses> xs = orderedPresses(m.ax, m.bx, m.x).iterator();
    Iterator<Presses> ys = orderedPresses(m.ay, m.by, m.y).iterator();

    // Possibilities: normal case there are several

    if (!xs.hasNext() || !ys.hasNext()) {
      return Optional.empty();
    }

    Presses x = xs.next();
    Presses y = ys.next();
    while (true) {
      System.out.println("Comparing " + x + " (cost: " + x.cost() + ") and " + y + " (cost: " + y.cost() + ")");
      if (x.a() < y.a()) {
        if (!xs.hasNext())
          return Optional.empty();
        x = xs.next();
      } else if (y.a() < x.a()) {
        if (!ys.hasNext())
          return Optional.empty();
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

  private MinAndGap oneAxis(long a, long b, Pair zeros) {
    var r = kRange(a, b, zeros);
    var lowPresses = presses(a, b, zeros, r.low());
    var highPresses = presses(a, b, zeros, r.high());
    // System.out.println("r: %s; lowPresses: %s; highPresses: %s".formatted(r,
    // lowPresses, highPresses));

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

  private long whichStep(long gap1, long gap2, long start1, long start2) {
    var ext = extendedEuclidean(gap1, gap2);
    var c = gap2 - (start1 - start2) % gap2;
    return floorMod((ext.x() * c), gap2);
  }

  private Optional<Presses> winner(Machine m) {
    long maxX = (long) max(ceil(m.x / m.ax), ceil(m.x / m.bx));
    long maxY = (long) max(ceil(m.y / m.ay), ceil(m.y / m.by));
    // System.out.println("maxX: %d; maxY: %d".formatted(maxX, maxY));
    return range(0, maxX).boxed()
        .flatMap(a -> range(0, maxY).filter(b -> m.winner(a, b)).mapToObj(b -> new Presses(a, b)))
        .collect(minBy(Comparator.comparingLong(Presses::cost)));
  }

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

  private Range kRange(long a, long b, Pair zeros) {
    var gcd = gcd(a, b);
    var one = (long) ceil(-(zeros.x() / (b / gcd)));
    var two = (long) floor(zeros.y() / (a / gcd));
    return new Range(min(one, two), max(one, two));
  }

  /**
   * Compute the kth solution for the equation with the given a and b values and pre-computed zeros.
   *
   * @param a
   * @param b
   * @param zeros
   * @param k
   * @return
   */
  private Presses presses(long a, long b, Pair zeros, long k) {
    var gcd = gcd(a, b);
    return new Presses(zeros.x + k * (b / gcd), zeros.y - k * (a / gcd));
  }

  private Stream<Presses> allPresses(long a, long b, long goal) {
    return zeros(a, b, goal).stream().flatMap(zeros -> {
      var kRange = kRange(a, b, zeros);
      return rangeClosed(kRange.low, kRange.high).mapToObj(k -> presses(a, b, zeros, k));
    });
  }

  /**
   * Stream of presses ordered by cost to achive a single dimension's goal.
   *
   * @param a    Movement on A presses.
   * @param b    Movement on B presses.
   * @param goal Goal for dimension.
   * @return
   */
  private Stream<Presses> orderedPresses(long a, long b, long goal) {
    return zeros(a, b, goal).stream().flatMap(zeros -> {
      var kRange = kRange(a, b, zeros);
      var low = presses(a, b, zeros, kRange.low());
      var high = presses(a, b, zeros, kRange.high());
      LongStream ks = low.cost() < high.cost()
          ? fromTo(kRange.low(), kRange.high())
          : fromTo(kRange.high(), kRange.low());
      return ks.mapToObj(k -> presses(a, b, zeros, k));
    });
  }

  /**
   * Stream of longs on the range [start, end] which can be increasing or decreasing.
   *
   * @param start The starting point.
   * @param end The ending point.
   * @return Stream whose first element is start and whose last element is end.
   */
  private LongStream fromTo(long start, long end) {
    long incr = (long) signum(end - start);
    LongPredicate pred = incr == 1 ? i -> i <= end : i -> i >= end;
    return LongStream.iterate(start, pred, i -> i + incr);
  }

  public static void main(String[] args) throws IOException {
    //new Day13Testing().testMachines();
    long gap1 = Long.parseLong(args[0]);
    long gap2 = Long.parseLong(args[1]);
    long start1 = Long.parseLong(args[2]);
    long start2 = Long.parseLong(args[3]);

    var t = new Day13Testing();
    System.out.println(t.whichStep(gap1, gap2, start1, start2));
  }

}
