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

  record Antenna(Cell cell, int what) {}

  public String part1(Path input) throws IOException {
    return solve(input, 2, 2, false);
  }

  public String part2(Path input) throws IOException {
    return solve(input, 1, Integer.MAX_VALUE, true);
  }

  private String solve(Path input, int start, int max, boolean normalize) throws IOException {
    var grid = new Grid(characterGrid(input));
    return String.valueOf(
      findAntenna(grid)
        .values()
        .stream()
        .flatMap(list -> antinodes(grid, list, start, max, normalize).stream())
        .collect(toSet())
        .size()
    );
  }

  private Map<Integer, List<Antenna>> findAntenna(Grid grid) {
    return grid
      .cells()
      .filter(c -> isAntenna(grid.at(c)))
      .map(c -> new Antenna(c, grid.at(c)))
      .collect(groupingBy(Antenna::what));
  }

  private List<Cell> antinodes(Grid grid, List<Antenna> antenna, int start, int max, boolean normalize) {
    List<Cell> antinodes = new ArrayList<>();
    for (Antenna a1 : antenna) {
      for (Antenna a2 : antenna) {
        if (a1 != a2) {
          GridOffset d = a1.cell().to(a2.cell());
          if (normalize) d = d.normalize();
          int step = start;
          Cell next = a1.cell().step(d, step);
          while (grid.inBounds(next) && step++ <= max) {
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
