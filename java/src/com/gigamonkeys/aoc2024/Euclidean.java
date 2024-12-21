package com.gigamonkeys.aoc2024;

import java.util.Arrays;
import static java.lang.Math.*;
import static java.util.stream.LongStream.*;
import java.util.stream.*;

public class Euclidean {

  record Coefficients(long x, long y) {
    Coefficients scale(long scale) {
      return new Coefficients(x * scale, y * scale);
    }
  }

  record Solution(long a, long b, long gcd, long x, long y) {
    Coefficients coefficients(long k, long scale) {
      return new Coefficients(scale * (x + k * (b / gcd)), scale * (y - k * (a / gcd)));
    }
    Coefficients zero(long scale) {
      return coefficients(0, scale);
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

  private Solution solve(long a, long b) {
    // Extended Euclidean algorithm
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

    return new Solution(a, b, oldR, oldS, oldT);
  }

  // Brute force solution for checking
  private long findFirst(long g1, long g2, long s1, long s2) {
    return range(0, g1 * g2)
      .map(i -> s2 + g2 * i)
      .filter(n -> n >= s1)
      .filter(n -> floorMod(n - s1, g1) == 0)
      .findFirst().orElseThrow();
  }

  private long findFirstMath(long g1, long g2, long s1, long s2) {

    // We're looking for m and n such that s1 + g2 * m == s2 + g2 * n
    // Additonally we want the m such that (s1 + g2) * m is the smallest value
    // greater than or equal to max(s1, s2)

    // Step 1: Solve the Diophantine equation g1 * x + (-g2 * y) = (s2 - s1)

    // Step 1a: Solve the equation g1 * x + -g2 * y = gcd(g1, g2) using the
    // extended Euclidean algorithm.
    var eq = solve(g1, -g2);

    // Scale both sides to get coefficients
    var zero = eq.zero((s2 - s1) / eq.gcd());

    // Need to pick t so that the result we get is bigger than both s1 and s2.
    // We can use either equation (s1 + g1 * ans.x() or s2 + g2 * ans.y() as the
    // whole point is they produce the same result.)

    // s1 + g1 * ans.x() >= max(s1, s2)
    // g1 * ans.x() >= max(s1, s2) - s1
    // g1 * (zero.x() + eq.b() * t) >= max(s1, s2) - s1
    // g1 * zero.x() + g1 * eq.b() * t >= max(s1, s2) - s1
    // g1 * zero.x() + g1 * eq.b() * t >= max(s1, s2) - s1
    // g1 * eq.b() * t >= (max(s1, s2) - s1) - (g1 * zero.x())
    // eq.b() * t >= (max(s1, s2) - s1) / g1 - zero.x()
    // t >= ((max(s1, s2) - s1) / g1 - zero.x()) / eq.b()

    var td = ((double) (max(s1, s2) - s1) / g1 - zero.x()) / eq.b();
    var t = (long) floor(td);

    var coef = new Coefficients(zero.x() + eq.b() * t, zero.y() - eq.a() * t);

    if (s1 + g1 * coef.x() != s2 + g2 * coef.y()) {
      throw new Error("Uh oh! %s is wrong.".formatted(coef));
    }

    return s1 + g1 * coef.x();
  }

  // Redo calculations and emit output about what went wrong.
  private void showWork(long g1, long g2, long s1, long s2, long ff) {

    var eq    = solve(g1, -g2);
    var zero  = eq.zero((s2 - s1) / eq.gcd());
    var td    = ((double) (max(s1, s2) - s1) / g1 - zero.x()) / eq.b();
    var t     = (long) floor(td);
    var coef   = new Coefficients(zero.x() + eq.b() * t, zero.y() - eq.a() * t);

    if (s1 + g1 * coef.x() != s2 + g2 * coef.y()) {
      throw new Error("Uh oh! %s is wrong.".formatted(coef));
    }

    var answer = s1 + g1 * coef.x();
    System.out.println("BAD: %d (%f) %s -> with %d %s %d".formatted(t, td, coef, answer, answer == ff ? "==" : "!=", ff));
  }



  public static void main(String[] args) {
    long[] nums = Arrays.stream(args).mapToLong(Long::parseLong).toArray();

    // These are the actual inputs in terms of the gap and starting points of two sequences.
    long g1 = nums[0];
    long g2 = nums[1];
    long s1 = nums.length > 2 ? nums[2] : 0;
    long s2 = nums.length > 3 ? nums[3] : 0;
    Euclidean e = new Euclidean();

    var ff = e.findFirst(g1, g2, s1, s2);
    var answer = e.findFirstMath(g1, g2, s1, s2);

    if (answer == ff) {
      System.out.println("GOOD: %d".formatted(answer));
    } else {
      e.showWork(g1, g2, s1, s1, ff);
    }
  }

}
