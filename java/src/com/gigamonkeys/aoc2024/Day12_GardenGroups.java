package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day12_GardenGroups implements Solution {

  record Cell(int row, int column) {
    boolean inBounds(int[][] grid) {
      return 0 <= row && row < grid.length && 0 <= column && column < grid[0].length;
    }
    int marker(int[][] grid) {
      return grid[row][column];
    }
    Cell north() {
      return new Cell(row - 1, column);
    }

    Cell east() {
      return new Cell(row, column + 1);
    }

    Cell south() {
      return new Cell(row + 1, column);
    }

    Cell west() {
      return new Cell(row, column - 1);
    }
  }

  record Walker(int[][] grid) {
    void walk(Cell cell, Set<Cell> members, List<Cell> boundary) {
      members.add(cell);

      int marker = cell.marker(grid);

      for (int i = 0; i < 4; i++) {
        var nextRow = cell.row() + 1 - abs(i - 2);
        var nextCol = cell.column() + 1 - abs(1 - i);
        Cell newCell = new Cell(nextRow, nextCol);
        if (!newCell.inBounds(grid)) {
          boundary.add(newCell);
        } else if (newCell.marker(grid) != marker) {
          boundary.add(newCell);
        } else if (!members.contains(newCell)) {
          walk(newCell, members, boundary);
        }
      }
    }
  }

  public String part1(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    Walker w = new Walker(grid);

    Set<Cell> seen = new HashSet<>();

    long total = 0;

    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        Cell cell = new Cell(r, c);
        if (!seen.contains(cell)) {
          Set<Cell> members = new HashSet<>();
          List<Cell> boundary = new ArrayList<>();
          w.walk(cell, members, boundary);
          total += members.size() * boundary.size();
          seen.addAll(members);
        }
      }
    }
    return String.valueOf(total);
  }

  public String part2(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    Walker w = new Walker(grid);

    Set<Cell> seen = new HashSet<>();

    long total = 0;

    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        Cell cell = new Cell(r, c);
        if (!seen.contains(cell)) {
          Set<Cell> members = new HashSet<>();
          List<Cell> boundary = new ArrayList<>();
          w.walk(cell, members, boundary);
          total += members.size() * sides(members, boundary);
          seen.addAll(members);
        }
      }
    }
    return String.valueOf(total);
  }

  private int sides(Set<Cell> members, List<Cell> boundary) {
    Set<Cell> unique = new HashSet<>(boundary);

    int sides = 0;
    sides += countSides(bordering(unique, members, Cell::north, Cell::row), Cell::column);
    sides += countSides(bordering(unique, members, Cell::south, Cell::row), Cell::column);
    sides += countSides(bordering(unique, members, Cell::east, Cell::column), Cell::row);
    sides += countSides(bordering(unique, members, Cell::west, Cell::column), Cell::row);
    return sides;
  }

  private Map<Integer, List<Cell>> bordering(
    Set<Cell> unique,
    Set<Cell> members,
    UnaryOperator<Cell> dir,
    Function<Cell, Integer> group
  ) {
    return unique.stream().filter(c -> members.contains(dir.apply(c))).collect(groupingBy(group));
  }

  private int countSides(Map<Integer, List<Cell>> facing, Function<Cell, Integer> extract) {
    int sides = 0;
    for (List<Cell> cells : facing.values()) {
      sides += segments(cells.stream().map(extract).sorted().toList());
    }
    return sides;
  }

  private int segments(List<Integer> numbers) {
    int segments = 1;
    for (int i = 0; i < numbers.size() - 1; i++) {
      if (numbers.get(i) + 1 != numbers.get(i + 1)) {
        segments++;
      }
    }
    return segments;
  }
}
