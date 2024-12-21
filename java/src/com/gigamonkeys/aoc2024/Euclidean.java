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
    if (a == 0) {
      return new Extended(b, 0, 1);
    } else {
      var ext = extendedEuclidean(b % a, a);
      var x = ext.y - ((long) floor(b / a) * ext.x);
      var y = ext.x;
      return new Extended(ext.gcd, x, y);
    }
  }

  private Eq solve(long a, long b) {
    return extendedEuclidean(a, b).solution(a, b);
  }

  private long findFirst(long g1, long g2, long s1, long s2) {
    return range(0, g1 * g2).map(i -> s2 + g2 * i).filter(n -> floorMod(n, g1) == s1).findFirst().orElseThrow();
  }

  private Stream<Coefficients> bezout(long a, long b) {
    Extended ext = extendedEuclidean(a, b);
    return iterate(0, k -> k + 1).mapToObj(k -> bezout(a, b, ext, k));
  }

  private Coefficients bezout(long a, long b, Extended ext, long k) {
    return new Coefficients(ext.x + k * (b / ext.gcd), ext.y - k * (a / ext.gcd));
  }

  public static void main(String[] args) {
    long[] nums = Arrays.stream(args).mapToLong(Long::parseLong).toArray();

    // These are the actual inputs in terms of the gap and starting points of two sequences.
    long g1 = nums[0];
    long g2 = nums[1];
    long s1 = nums.length > 2 ? nums[2] : 0;
    long s2 = nums.length > 3 ? nums[3] : 0;
    Euclidean e = new Euclidean();
    //System.out.println("findfirst: %d".formatted(e.findFirst(g1, g2, s1, s2)));

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

    // XXX getting better. Not sure why t2 works better for the cases I've
    // tested so far. Feels like they should by symetrical.

    // s1 + g1 * ans.x >= s1
    // g1 * ans.x >= 0
    // g1 * (start.x() + eq.b() * t) >= 0
    // g1 * start.x() + g1 * eq.b() * t >= 0
    // g1 * start.x() + g1 * eq.b() * t >= 0
    // g1 * eq.b() * t >= -(g1 * start.x())
    // eq.b() * t >= -start.x()
    // t >= -start.x() / eq.b()
    var td = (double) -start.x() / eq.b();
    var t = (long) floor(td);

    // s2 + g2 * ans.y >= s2
    // g2 * ans.y >= 0
    // g2 * (start.y() - eq.a() * t) >= 0
    // g2 * start.y() + g2 * -eq.a() * t >= 0
    // g2 * -eq.a() * t >= -(g2 * start.y())
    // -eq.a() * t >= -start.y()
    // t >= -start.y() / -eq.a()
    var td2 = (double) start.y() / eq.a();
    var t2 = (long) floor(td2);

    if (t != t2) {
      System.out.println("td: %f; td2: %f; t: %d; t2: %d".formatted(td, td2, t, t2));
    }

    //System.out.println(eq);
    var ans = new Coefficients(start.x() + eq.b() * t2, start.y() - eq.a() * t2);
    //System.out.println("ans: %d; other: %d + %d * %d = %d; s1: %d".formatted(ans.x(), start.x(), eq.b(), t, start.x() + eq.b() * t, s1));
    //System.out.println("start: %s; -start.x(): %d; eq.b(): %d, td: %f; t: %d".formatted(start, -start.x(), eq.b(), td, t));;
    System.out.println("%s -> with %d and %d".formatted(ans, s1 + g1 * ans.x(), s2 + g2 * ans.y()));


    // From here any coefficients
    // System.out.println("UP");
    // Stream.iterate(start, p -> new Coefficients(p.x() + eq.b(), p.y() - eq.a())).limit(5).forEach(c -> {
    //     System.out.println("%s -> with %d and %d".formatted(c, s1 + g1 * c.x(), s2 + g2 * c.y()));
    //   });

    // System.out.println("DOWN");
    // Stream.iterate(start, p -> new Coefficients(p.x() - eq.b(), p.y() + eq.a())).limit(5).forEach(c -> {
    //     System.out.println("%s -> with %d and %d".formatted(c, s1 + g1 * c.x(), s2 + g2 * c.y()));
    //   });

  }

}
