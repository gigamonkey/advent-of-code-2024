package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.*;
import static java.nio.file.Files.lines;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day04 extends Day {

  private static final int[] XMAS = "XMAS".codePoints().toArray();
  private static final int[] MAS = "MAS".codePoints().toArray();

  public String part1(Path input) throws IOException {
    var grid = characterGrid(input);
    int count = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        count += at(grid, r, c);
      }
    }
    return String.valueOf(count);
  }

  public String part2(Path input) throws IOException {
    var grid = characterGrid(input);
    int count = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (xmas(grid, r, c)) count++;
      }
    }
    return String.valueOf(count);
  }

  private int at(int[][] grid, int r, int c) {
    int count = 0;
    for (int dr = -1; dr <= 1; dr++) {
      for (int dc = -1; dc <= 1; dc++) {
        if (atAndInDirection(grid, XMAS, r, c, dr, dc)) count++;
      }
    }
    return count;
  }

  private boolean atAndInDirection(int[][] grid, int[] what, int sr, int sc, int dr, int dc) {
    for (int i = 0; i < what.length; i++) {
      int r = sr + dr * i;
      int c = sc + dc * i;
      if (!inBounds(grid, r, c) || what[i] != grid[r][c]) {
        return false;
      }
    }
    return true;
  }

  private boolean inBounds(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
  }

  private boolean xmas(int[][] grid, int r, int c) {
    return grid[r][c] == 'A' && diag1(grid, r, c) && diag2(grid, r, c);
  }

  private boolean diag1(int[][] grid, int r, int c) {
    for (int d = -1; d <= 1; d += 2) {
      if (atAndInDirection(grid, MAS, r - d, c - d, d, d)) {
        return true;
      }
    }
    return false;
  }

  private boolean diag2(int[][] grid, int r, int c) {
    for (int d = -1; d <= 1; d += 2) {
      if (atAndInDirection(grid, MAS, r + d, c - d, -d, d)) {
        return true;
      }
    }
    return false;
  }
}
