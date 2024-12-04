package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.text;
import static java.lang.Integer.*;
import static java.util.regex.Pattern.compile;
import static java.nio.file.Files.lines;

import java.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day04 extends Day {

  private static final int[] XMAS = "XMAS".codePoints().toArray();

  public String part1(Path input) throws IOException {
    var grid = data(input);
    int count = 0;
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        count += at(grid, r, c);
      }
    }
    return String.valueOf(count);
  }

  private int at(int[][] grid, int r, int c) {
    int count = 0;
    for (int dr = -1; dr <= 1; dr++) {
      for (int dc = -1; dc <= 1; dc++) {
        if (atAndInDirection(grid, r, c, dr, dc)) count++;
      }
    }
    return count;
  }

  private boolean atAndInDirection(int[][] grid, int sr, int sc, int dr, int dc) {
    for (int i = 0; i < XMAS.length; i++) {
      int r = sr + dr * i;
      int c = sc + dc * i;
      if (!inBounds(grid, r, c) || XMAS[i] != grid[r][c]) {
        return false;
      }
    }
    return true;
  }

  private boolean inBounds(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
  }


  public String part2(Path input) throws IOException {
    return "";
  }


  private int[][] data(Path input) throws IOException {
    return lines(input).map(line -> line.codePoints().toArray()).toArray(int[][]::new);
  }
}
