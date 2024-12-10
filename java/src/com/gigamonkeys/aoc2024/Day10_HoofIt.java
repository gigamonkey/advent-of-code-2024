package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

public class Day10_HoofIt implements Solution {

  private record Hiker(int[][] grid) {
    int sumScores() {
      return sumTrails(uniquePeaks());
    }

    int sumRatings() {
      return sumTrails(this::pathCounter);
    }

    int sumTrails(Supplier<IntBinaryOperator> scorerSupplier) {
      int total = 0;
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
          if (grid[r][c] == 0) {
            total += hike(r, c, scorerSupplier.get());
          }
        }
      }
      return total;
    }

    int hike(int r, int c, IntBinaryOperator trailScorer) {
      if (grid[r][c] == 9) {
        return trailScorer.applyAsInt(r, c);
      } else {
        int total = 0;

        // A trick for efficiently enumerating NESW points.
        for (int i = 0; i < 4; i++) {
          var nextRow = r + 1 - Math.abs(i - 2);
          var nextCol = c + 1 - Math.abs(1 - i);
          if (uphill(nextRow, nextCol, grid[r][c])) {
            total += hike(nextRow, nextCol, trailScorer);
          }
        }
        return total;
      }
    }

    Supplier<IntBinaryOperator> uniquePeaks() {
      return () -> {
        var seen = new boolean[grid.length][grid[0].length];
        return (r, c) -> {
          var s = seen[r][c] ? 0 : 1;
          seen[r][c] = true;
          return s;
        };
      };
    }

    IntBinaryOperator pathCounter() {
      return (r, c) -> 1;
    }

    boolean inBounds(int r, int c) {
      return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
    }

    boolean uphill(int r, int c, int current) {
      return inBounds(r, c) && grid[r][c] - current == 1;
    }
  }

  public String part1(Path input) throws IOException {
    return String.valueOf(new Hiker(digitGrid(input)).sumScores());
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(new Hiker(digitGrid(input)).sumRatings());
  }
}
