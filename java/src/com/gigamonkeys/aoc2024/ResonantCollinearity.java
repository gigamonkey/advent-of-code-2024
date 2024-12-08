package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.characterGrid;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.IntStream.range;
import static java.util.stream.Collectors.*;

public class ResonantCollinearity implements Solution {

  record Cell(int row, int column) {}

  record Antenna(Cell cell, int what) {}

  public String part1(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    var antenna = findAntenna(grid);

    Set<Cell> antinodes = new HashSet<>();
    for (List<Antenna> list: antenna.values()) {
      antinodes.addAll(antinodes(grid, list));
    }

    return String.valueOf(antinodes.size());
  }

  public String part2(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    var antenna = findAntenna(grid);

    Set<Cell> antinodes = new HashSet<>();
    for (List<Antenna> list: antenna.values()) {
      antinodes.addAll(antinodes2(grid, list));
    }

    return String.valueOf(antinodes.size());
  }

  private Map<Integer, List<Antenna>> findAntenna(int[][] grid) {
    return range(0, grid.length).boxed()
      .flatMap(r -> range(0, grid[0].length).mapToObj(c -> new Antenna(new Cell(r, c), grid[r][c])))
      .filter(cell -> isAntenna(cell.what()))
      .collect(groupingBy(Antenna::what));
  }


  private List<Cell> antinodes(int[][] grid, List<Antenna> antenna) {
    List<Cell> antinodes = new ArrayList<>();
    for (Antenna a1: antenna) {
      for (Antenna a2: antenna) {
        if (a1 != a2) {
          int r = a2.cell().row();
          int c = a2.cell().column();
          int dr = a2.cell().row() - a1.cell().row();
          int dc = a2.cell().column() - a1.cell().column();
          if (inBounds(grid, r + dr, c + dc)) {
            antinodes.add(new Cell(r + dr, c + dc));
          }
        }
      }
    }
    return antinodes;
  }

  private List<Cell> antinodes2(int[][] grid, List<Antenna> antenna) {
    List<Cell> antinodes = new ArrayList<>();
    for (Antenna a1: antenna) {
      for (Antenna a2: antenna) {
        if (a1 != a2) {
          int r = a2.cell().row();
          int c = a2.cell().column();
          int dr = a2.cell().row() - a1.cell().row();
          int dc = a2.cell().column() - a1.cell().column();
          int step = 0;
          while (inBounds(grid, r + (step * dr), c + (step * dc))) {
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
