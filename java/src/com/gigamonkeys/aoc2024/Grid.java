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

public class Grid implements Iterable<Cell> {

  private final int[][] grid;

  public Grid(int[][] grid) {
    this.grid = grid;
  }

  public int rows() {
    return grid.length;
  }

  public int columns() {
    return grid[0].length;
  }

  public int at(Cell cell) {
    return grid[cell.row()][cell.column()];
  }

  public boolean inBounds(Cell cell) {
    return cell.inBounds(grid);
  }

  public Stream<Cell> cells() {
    return range(0, grid.length).boxed().flatMap(r -> range(0, grid[0].length).mapToObj(c -> new Cell(r, c)));
  }

  public Iterable<Cell> neighbors(Cell cell) {
    var row = cell.row();
    var column = cell.column();
    return () -> range(0, 4).mapToObj(i -> new Cell(row + 1 - abs(i - 2), column + 1 - abs(1 - i))).iterator();
  }

  @Override
  public Iterator<Cell> iterator() {
    return cells().iterator();
  }
}
