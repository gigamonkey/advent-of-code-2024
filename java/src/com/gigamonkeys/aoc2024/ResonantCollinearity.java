package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.characterGrid;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.*;

public class ResonantCollinearity implements Solution {

  record Cell(int row, int column) {}

  record Antenna(Cell cell, int what) {}

  public String part1(Path input) throws IOException {
    return solve(input, 2, 2);
  }

  public String part2(Path input) throws IOException {
    return solve(input, 1, Integer.MAX_VALUE);
  }

  private String solve(Path input, int start, int max) throws IOException {
    int[][] grid = characterGrid(input);
    return String.valueOf(
      findAntenna(grid)
        .values()
        .stream()
        .flatMap(list -> antinodes(grid, list, start, max).stream())
        .collect(toSet())
        .size()
    );
  }

  private Map<Integer, List<Antenna>> findAntenna(int[][] grid) {
    return range(0, grid.length)
      .boxed()
      .flatMap(r -> {
        return range(0, grid[0].length)
          .filter(c -> isAntenna(grid[r][c]))
          .mapToObj(c -> new Antenna(new Cell(r, c), grid[r][c]));
      })
      .collect(groupingBy(Antenna::what));
  }

  private List<Cell> antinodes(int[][] grid, List<Antenna> antenna, int start, int max) {
    List<Cell> antinodes = new ArrayList<>();
    for (Antenna a1 : antenna) {
      for (Antenna a2 : antenna) {
        if (a1 != a2) {
          var cell1 = a1.cell();
          var cell2 = a2.cell();
          int r = cell1.row();
          int c = cell1.column();
          int dr = cell2.row() - cell1.row();
          int dc = cell2.column() - cell1.column();
          int step = start;
          while (inBounds(grid, r + (step * dr), c + (step * dc)) && step <= max) {
            antinodes.add(new Cell(r + (step * dr), c + (step * dc)));
            step++;
          }
        }
      }
    }
    return antinodes;
  }

  private boolean inBounds(int[][] grid, int r, int c) {
    return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
  }

  private boolean isAntenna(int c) {
    return Character.isAlphabetic(c) || Character.isDigit(c);
  }
}
