package com.gigamonkeys.aoc2024;

import java.util.Arrays;
import static java.lang.Math.*;
import static java.util.stream.LongStream.*;
import java.util.stream.*;

public class Euclidean {

  record Extended(long gcd, long x, long y) {
    public Eq solution(long a, long b) {
      return new Eq(a, b, gcd, x, y);
    }
  }

  record Coefficients(long x, long y) {
    Coefficients scale(long scale) {
      return new Coefficients(x * scale, y * scale);
    }
  }

  record Eq(long a, long b, long gcd, long x, long y) {
    Stream<Coefficients> coeffecients(long from, long to) {
      long incr = (to - from) / abs(to - from);
      return iterate(from, k -> k != to, k -> k + incr).mapToObj(this::pair);
    }

    Stream<Coefficients> scaledCoeffecients(long from, long to, long scale) {
      long incr = (to - from) / abs(to - from);
      return iterate(from, k -> k != to, k -> k + incr).mapToObj(k -> scaledPair(k, scale));
    }

    Coefficients pair(long k) {
      return new Coefficients(x + k * (b / gcd), y - k * (a / gcd));
    }

    Coefficients scaledPair(long k, long scale) {
      return new Coefficients(scale * (x + k * (b / gcd)), scale * (y - k * (a / gcd)));
    }
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
    var oldR = a;
    var r = b;
    var oldS = 1L;
    var s = 0L;
    var oldT = 0L;
    var t = 1L;

    while (r != 0) {
      var q = oldR / r;
      var newR = oldR - q * r;
      var newS = oldS - q * s;
      var newT = oldT - q * t;
      oldR = r;
      oldS = s;
      oldT = t;
      r = newR;
      s = newS;
      t = newT;
    }

    return new Extended(oldR, oldS, oldT);
  }

  private Eq solve(long a, long b) {
    return extendedEuclidean(a, b).solution(a, b);
  }

  private long findFirst(long g1, long g2, long s1, long s2) {
    return range(0, g1 * g2)
      .map(i -> s2 + g2 * i)
      .filter(n -> n >= s1)
      .filter(n -> floorMod(n - s1, g1) == 0)
      .findFirst().orElseThrow();
  }


  public static void main(String[] args) {
    long[] nums = Arrays.stream(args).mapToLong(Long::parseLong).toArray();

    // These are the actual inputs in terms of the gap and starting points of two sequences.
    long g1 = nums[0];
    long g2 = nums[1];
    long s1 = nums.length > 2 ? nums[2] : 0;
    long s2 = nums.length > 3 ? nums[3] : 0;
    Euclidean e = new Euclidean();

    // We're looking for m and n such that s1 + g2 * m == s2 + g2 * n
    // Additonally we want the m such that (s1 + g2) * m is the smallest value
    // greater than or equal to s1.

    // Solve the Diophantine equation g1 * x + (-g2 * y) = (s2 - s1)

    // First solve the equation g1 * x + -g2 * y = gcd(g1, g2) using the
    // extended Euclidean algorithm.
    var eq = e.solve(g1, -g2);

    // Scale both sides to get coefficients
    var scale = (s2 - s1) / eq.gcd();
    var start = eq.scaledPair(0, scale);

    // Need to pick t so that the result we get is bigger than both s1 and s2.

    // s1 + g1 * ans.x() >= max(s1, s2)
    // g1 * ans.x() >= max(s1, s2) - s1
    // g1 * (start.x() + eq.b() * t) >= max(s1, s2) - s1
    // g1 * start.x() + g1 * eq.b() * t >= max(s1, s2) - s1
    // g1 * start.x() + g1 * eq.b() * t >= max(s1, s2) - s1
    // g1 * eq.b() * t >= (max(s1, s2) - s1) - (g1 * start.x())
    // eq.b() * t >= (max(s1, s2) - s1) / g1 - start.x()
    // t >= ((max(s1, s2) - s1) / g1 - start.x()) / eq.b()
    var td = (double) ((max(s1, s2) - s1) / g1 - start.x()) / eq.b();
    var t = (long) floor(td);

    var ans = new Coefficients(start.x() + eq.b() * t, start.y() - eq.a() * t);

    if (s1 + g1 * ans.x() != s2 + g2 * ans.y()) {
      System.out.println("Uh oh! %s is wrong ans.".formatted(ans));
    }
    var answer = s1 + g1 * ans.x();
    var ff = e.findFirst(g1, g2, s1, s2);

    if (answer == ff) {
      System.out.println("GOOD: %d".formatted(answer));
    } else {
      System.out.println("BAD: %d (%f) %s -> with %d %s %d".formatted(t, td, ans, answer, answer == ff ? "==" : "!=", ff));
    }
  }

}
