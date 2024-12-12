package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;

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
          //System.out.println("members: %d; boundary: %d".formatted(members.size(), boundary.size()));
          total += members.size() * boundary.size();
          seen.addAll(members);
        }
      }
    }
    return String.valueOf(total);
  }


  public String part2(Path input) throws IOException {
    return "nyi";
  }

}
