package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public record Cell(int row, int column) {
  public Cell neighbor(Direction d) {
    return new Cell(row + d.rowChange(), column + d.columnChange());
  }

  public Cell step(GridOffset offset, int steps) {
    return new Cell(row + (steps * offset.dr()), column + (steps * offset.dc()));
  }

  public Cell step(Direction d, int steps) {
    return new Cell(row + (steps * d.rowChange()), column + (steps * d.columnChange()));
  }

  public GridOffset to(Cell other) {
    return new GridOffset(other.row - row, other.column - column);
  }

  public boolean inBounds(int[][] grid) {
    return (0 <= row && row < grid.length && 0 <= column && column < grid[row].length);
  }
}
