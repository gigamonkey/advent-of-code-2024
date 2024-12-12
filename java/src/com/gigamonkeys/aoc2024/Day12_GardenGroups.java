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
    Cell north() {
      return new Cell(row - 1, column);
    }

    Cell south() {
      return new Cell(row + 1, column);
    }

    Cell east() {
      return new Cell(row, column + 1);
    }

    Cell west() {
      return new Cell(row, column - 1);
    }
  }

  record Surveyor(int[][] grid) {
    long survey(BiFunction<Set<Cell>, List<Cell>, Integer> measurer) {
      Set<Cell> seen = new HashSet<>();

      long total = 0;

      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
          Cell cell = new Cell(r, c);
          if (!seen.contains(cell)) {
            Set<Cell> members = new HashSet<>();
            List<Cell> boundary = new ArrayList<>();
            walk(cell, members, boundary);
            total += members.size() * measurer.apply(members, boundary);
            seen.addAll(members);
          }
        }
      }
      return total;
    }

    void walk(Cell cell, Set<Cell> members, List<Cell> boundary) {
      members.add(cell);

      for (int i = 0; i < 4; i++) {
        var nextRow = cell.row() + 1 - abs(i - 2);
        var nextCol = cell.column() + 1 - abs(1 - i);
        Cell newCell = new Cell(nextRow, nextCol);
        if (!inBounds(newCell)) {
          boundary.add(newCell);
        } else if (marker(newCell) != marker(cell)) {
          boundary.add(newCell);
        } else if (!members.contains(newCell)) {
          walk(newCell, members, boundary);
        }
      }
    }

    boolean inBounds(Cell cell) {
      return 0 <= cell.row() && cell.row() < grid.length && 0 <= cell.column() && cell.column() < grid[0].length;
    }

    int marker(Cell cell) {
      return grid[cell.row()][cell.column];
    }
  }

  public String part1(Path input) throws IOException {
    return solve(input, (ignore, boundary) -> boundary.size());
  }

  public String part2(Path input) throws IOException {
    return solve(input, this::sides);
  }

  public String solve(Path input, BiFunction<Set<Cell>, List<Cell>, Integer> measurer) throws IOException {
    return String.valueOf(new Surveyor(characterGrid(input)).survey(measurer));
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
    Function<Cell, Integer> groupBy
  ) {
    return unique.stream().filter(c -> members.contains(dir.apply(c))).collect(groupingBy(groupBy));
  }

  private int countSides(Map<Integer, List<Cell>> facing, Function<Cell, Integer> extract) {
    return facing.values().stream().mapToInt(cells -> segments(cells.stream().map(extract).sorted().toList())).sum();
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
