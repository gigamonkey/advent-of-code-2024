package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;

import java.io.IOException;
import java.nio.file.Path;

public class Day04_CeresSearch implements Solution {

  private static final int[] XMAS = "XMAS".codePoints().toArray();
  private static final int[] MAS = "MAS".codePoints().toArray();

  public String part1(Path input) throws IOException {
    Grid grid = new Grid(characterGrid(input));
    return String.valueOf(grid.cells().mapToInt(c -> at(grid, c)).sum());
  }

  public String part2(Path input) throws IOException {
    var grid = new Grid(characterGrid(input));
    return String.valueOf(grid.cells().filter(c -> xmas(grid, c)).count());
  }

  private int at(Grid grid, Cell cell) {
    int count = 0;
    for (int dr = -1; dr <= 1; dr++) {
      for (int dc = -1; dc <= 1; dc++) {
        var offset = new GridOffset(dr, dc);
        if (atAndInDirection(grid, XMAS, cell, offset)) count++;
      }
    }
    return count;
  }

  private boolean atAndInDirection(Grid grid, int[] what, Cell cell, GridOffset offset) {
    for (int i = 0; i < what.length; i++) {
      var next = cell.step(offset, i);
      if (!grid.inBounds(next) || what[i] != grid.at(next)) {
        return false;
      }
    }
    return true;
  }

  private boolean xmas(Grid grid, Cell cell) {
    return grid.at(cell) == 'A' && diag1(grid, cell) && diag2(grid, cell);
  }

  private boolean diag1(Grid grid, Cell cell) {
    for (int d = -1; d <= 1; d += 2) {
      var offset = new GridOffset(d, d);
      var start = new Cell(cell.row() - d, cell.column() - d);
      if (atAndInDirection(grid, MAS, start, offset)) {
        return true;
      }
    }
    return false;
  }

  private boolean diag2(Grid grid, Cell cell) {
    for (int d = -1; d <= 1; d += 2) {
      var offset = new GridOffset(-d, d);
      var start = new Cell(cell.row() + d, cell.column() - d);
      if (atAndInDirection(grid, MAS, start, offset)) {
        return true;
      }
    }
    return false;
  }
}
