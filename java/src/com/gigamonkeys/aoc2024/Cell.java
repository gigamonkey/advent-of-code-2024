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
  public Cell north() {
    return new Cell(row - 1, column);
  }

  public Cell south() {
    return new Cell(row + 1, column);
  }

  public Cell east() {
    return new Cell(row, column + 1);
  }

  public Cell west() {
    return new Cell(row, column - 1);
  }

  public boolean inBounds(int[][] grid) {
    return (0 <= row && row < grid.length && 0 <= column && column < grid[row].length);
  }
}
