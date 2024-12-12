package com.gigamonkeys.aoc2024;

public record GridOffset(int dr, int dc) {
  GridOffset normalize() {
    var gcd = gcd(Math.abs(dr), Math.abs(dc));
    return new GridOffset(dr / gcd, dc / gcd);
  }

  private int gcd(int a, int b) {
    var r = Math.floorMod(a, b);
    if (r != 0) {
      return gcd(b, r);
    } else {
      return b;
    }
  }
}
