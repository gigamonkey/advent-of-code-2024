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

  record Cell(int row, int column) {}

  record Direction(int dr, int dc) {}

  private static class Walker {

    private final int[][] grid;

    Walker(int[][] grid) {
      this.grid = grid;
    }

    int count() {
      int total = 0;
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
          if (grid[r][c] == 0) {
            //System.out.println("TRAILHEAD grid[%d][%d] = %d".formatted(r, c, grid[r][c]));
            total += hike(r, c, seen());
          }
        }
      }
      return total;
    }

    int count2() {
      int total = 0;
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
          if (grid[r][c] == 0) {
            //System.out.println("TRAILHEAD grid[%d][%d] = %d".formatted(r, c, grid[r][c]));
            total += hike2(r, c);
          }
        }
      }
      return total;
    }

    boolean[][] seen() {
      return new boolean[grid.length][grid[0].length];
    }

    int hike(int r, int c, boolean[][] seen) {
      if (grid[r][c] == 9) {
        if (!seen[r][c]) {
          //System.out.println("Found peak at %d,%d".formatted(r, c));
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
                //System.out.println("uphill from hiking from %d,%d = %d to %d,%d = %d".formatted(r, c, grid[r][c], r + dr, c + dc, grid[r + dr][c + dc]));
                total += hike(r + dr, c + dc, seen);
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
                //System.out.println("uphill from hiking from %d,%d = %d to %d,%d = %d".formatted(r, c, grid[r][c], r + dr, c + dc, grid[r + dr][c + dc]));
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
    int[][] grid = digitGrid(input);
    var w = new Walker(grid);
    return String.valueOf(w.count());


  }


  public String part2(Path input) throws IOException {
    int[][] grid = digitGrid(input);
    var w = new Walker(grid);
    return String.valueOf(w.count2());
  }
}
