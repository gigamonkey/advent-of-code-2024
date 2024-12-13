package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Direction.*;
import static com.gigamonkeys.aoc2024.Util.*;
import static java.util.stream.IntStream.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.*;

public class Day04_CeresSearch implements Solution {

  private static final int[] XMAS = "XMAS".codePoints().toArray();
  private static final int[] MAS = "MAS".codePoints().toArray();

  public String part1(Path input) throws IOException {
    Grid grid = new Grid(characterGrid(input));
    return String.valueOf(grid.cells().mapToInt(c -> numberAt(grid, c)).sum());
  }

  public String part2(Path input) throws IOException {
    var grid = new Grid(characterGrid(input));
    return String.valueOf(grid.cells().filter(c -> xmas(grid, c)).count());
  }

  private int numberAt(Grid grid, Cell cell) {
    return (int) Direction.all().filter(d -> atAndInDirection(grid, XMAS, cell, d)).count();
  }

  private boolean atAndInDirection(Grid grid, int[] what, Cell cell, Direction d) {
    return range(0, what.length).allMatch(i -> {
      var next = cell.step(d, i);
      return grid.inBounds(next) && what[i] == grid.at(next);
    });
  }

  private boolean xmas(Grid grid, Cell cell) {
    return grid.at(cell) == 'A' && diagFrom(grid, cell, NORTH_EAST) && diagFrom(grid, cell, SOUTH_EAST);
  }

  private boolean diagFrom(Grid grid, Cell cell, Direction d) {
    return diag(grid, cell, d) || diag(grid, cell, d.opposite());
  }

  private boolean diag(Grid grid, Cell cell, Direction d) {
    var start = cell.neighbor(d.opposite());
    return atAndInDirection(grid, MAS, start, d);
  }
}
