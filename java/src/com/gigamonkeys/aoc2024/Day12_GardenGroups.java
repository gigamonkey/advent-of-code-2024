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

  record Region(int area, int perimiter) {
    public int price() { return area * perimiter; }
  };

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
          //System.out.println("walking %s marker %s".formatted(cell, (char)cell.marker(grid)));
          Set<Cell> members = new HashSet<>();
          List<Cell> boundary = new ArrayList<>();
          w.walk(cell, members, boundary);
          //System.out.println("members: %s; boundary: %s".formatted(members, boundary));
          //
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
          // System.out.println("walking %s marker %s".formatted(cell, (char)cell.marker(grid)));
          // System.out.println("members: %s".formatted(members));
          // System.out.println("boundary: %s".formatted(boundary));
          // System.out.println("members: %d; boundary: %d".formatted(members.size(), sides(members, boundary)));
          seen.addAll(members);
        }
      }
    }
    return String.valueOf(total);
  }

  private int sides(Set<Cell> members, List<Cell> boundary) {
    Set<Cell> unique = new HashSet<>(boundary);
    //System.out.println("unique: %s".formatted(unique));

    // Map<Integer, List<Cell>> byRow = unique.stream().filter(c -> isHorizontal(c, members)).collect(groupingBy(Cell::row));
    // Map<Integer, List<Cell>> byColumn = unique.stream().filter(c -> isVertical(c, members)).collect(groupingBy(Cell::column));

    Map<Integer, List<Cell>> north = unique.stream().filter(c -> members.contains(c.south())).collect(groupingBy(Cell::row));
    Map<Integer, List<Cell>> east = unique.stream().filter(c -> members.contains(c.west())).collect(groupingBy(Cell::column));
    Map<Integer, List<Cell>> south = unique.stream().filter(c -> members.contains(c.north())).collect(groupingBy(Cell::row));
    Map<Integer, List<Cell>> west = unique.stream().filter(c -> members.contains(c.east())).collect(groupingBy(Cell::column));

    int sides = 0;
    sides += inRow(north);
    sides += inRow(south);
    sides += inColumn(east);
    sides += inColumn(west);

    return sides;
  }

  private int inRow(Map<Integer, List<Cell>> byRow) {
    int sides = 0;
    for (List<Cell> inRow : byRow.values()) {
      //System.out.println("Row %d %s".formatted(inRow.get(0).row(), inRow));
      sides += segments(inRow.stream().map(Cell::column).sorted().toList());
    }
    return sides;
  }

  private int inColumn(Map<Integer, List<Cell>> byColumn) {
    int sides = 0;
    for (List<Cell> inColumn : byColumn.values()) {
      //System.out.println("Column %d %s".formatted(inColumn.get(0).column(), inColumn));
      sides += segments(inColumn.stream().map(Cell::row).sorted().toList());
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
    //System.out.println("%d segments in %s".formatted(segments, numbers));
    return segments;
  }

  private boolean isVertical(Cell cell, Set<Cell> members) {
    return members.contains(cell.east()) || members.contains(cell.west());
  }

  private boolean isHorizontal(Cell cell, Set<Cell> members) {
    return members.contains(cell.north()) || members.contains(cell.south());
  }


}
