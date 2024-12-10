package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.characterGrid;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day08_ResonantCollinearity implements Solution {

  record Cell(int row, int column) {
    boolean inBounds(int[][] grid) {
      return 0 <= row && row < grid.length && 0 <= column && column < grid[0].length;
    }
    Cell step(Direction d, int steps) {
      return new Cell(row + (steps * d.dr()), column + (steps * d.dc()));
    }
    Direction to(Cell other) {
      return new Direction(other.row - row, other.column - column);
    }
  }

  record Direction(int dr, int dc) {
    Direction normalize() {
      var gcd = gcd(Math.abs(dr), Math.abs(dc));
      return new Direction(dr / gcd, dc / gcd);
    }
  }

  record Antenna(Cell cell, int what) {}

  public String part1(Path input) throws IOException {
    return solve(input, 2, 2, false);
  }

  public String part2(Path input) throws IOException {
    return solve(input, 1, Integer.MAX_VALUE, true);
  }

  private String solve(Path input, int start, int max, boolean normalize) throws IOException {
    int[][] grid = characterGrid(input);
    return String.valueOf(
      findAntenna(grid)
        .values()
        .stream()
        .flatMap(list -> antinodes(grid, list, start, max, normalize).stream())
        .collect(toSet())
        .size()
    );
  }

  private static int gcd(int a, int b) {
    var r = Math.floorMod(a, b);
    if (r != 0) {
      return gcd(b, r);
    } else {
      return b;
    }
  }

  private Map<Integer, List<Antenna>> findAntenna(int[][] grid) {
    return range(0, grid.length)
      .boxed()
      .flatMap(r ->
        range(0, grid[0].length)
          .filter(c -> isAntenna(grid[r][c]))
          .mapToObj(c -> new Antenna(new Cell(r, c), grid[r][c]))
      )
      .collect(groupingBy(Antenna::what));
  }

  private List<Cell> antinodes(int[][] grid, List<Antenna> antenna, int start, int max, boolean normalize) {
    List<Cell> antinodes = new ArrayList<>();
    for (Antenna a1 : antenna) {
      for (Antenna a2 : antenna) {
        if (a1 != a2) {
          Direction d = a1.cell().to(a2.cell());
          if (normalize) d = d.normalize();
          int step = start;
          Cell next = a1.cell().step(d, step);
          while (next.inBounds(grid) && step++ <= max) {
            antinodes.add(next);
            next = a1.cell().step(d, step);
          }
        }
      }
    }
    return antinodes;
  }

  private boolean isAntenna(int c) {
    return Character.isAlphabetic(c) || Character.isDigit(c);
  }
}
