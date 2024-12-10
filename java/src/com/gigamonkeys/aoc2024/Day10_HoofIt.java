package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.generate;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.*;

public class Day10_HoofIt implements Solution {

  private record Walker(int[][] grid) {

    int sumScores() {
      return sum((r, c) -> hike1(r, c, seen()));
    }

    int sumRatings() {
      return sum((r, c) -> hike2(r, c));
    }

    int sum(IntBinaryOperator op) {
      int total = 0;
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
          if (grid[r][c] == 0) {
            total += op.applyAsInt(r, c);
          }
        }
      }
      return total;
    }

    boolean[][] seen() {
      return new boolean[grid.length][grid[0].length];
    }

    int hike1(int r, int c, boolean[][] seen) {
      if (grid[r][c] == 9) {
        if (!seen[r][c]) {
          seen[r][c] = true;
          return 1;
        } else {
          return 0;
        }
      } else {
        int total = 0;
        for (int dr = -1; dr <= 1; dr++) {
          for (int dc = -1; dc <= 1; dc++) {
            if (!(dr == 0 && dc == 0) && (dr == 0 || dc == 0)) {
              if (uphill(r, c, dr, dc)) {
                total += hike1(r + dr, c + dc, seen);
              }
            }
          }
        }
        return total;
      }
    }

    int hike2(int r, int c) {
      if (grid[r][c] == 9) {
        return 1;
      } else {
        int total = 0;
        for (int dr = -1; dr <= 1; dr++) {
          for (int dc = -1; dc <= 1; dc++) {
            if (!(dr == 0 && dc == 0) && (dr == 0 || dc == 0)) {
              if (uphill(r, c, dr, dc)) {
                total += hike2(r + dr, c + dc);
              }
            }
          }
        }
        return total;
      }
    }

    boolean inBounds(int r, int c) {
      return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
    }

    boolean uphill(int r, int c, int dr, int dc) {
      return inBounds(r + dr, c + dc) && (grid[r][c] + 1) == grid[r + dr][c + dc];
    }
  }

  public String part1(Path input) throws IOException {
    return String.valueOf(new Walker(digitGrid(input)).sumScores());
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(new Walker(digitGrid(input)).sumRatings());
  }
}
