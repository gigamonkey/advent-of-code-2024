package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.lines;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.util.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuardGallivant2 implements Solution {

  private record Cell(int row, int col) {
    public boolean inBounds(int[][] grid) {
      return (
        0 <= row && row < grid.length &&
        0 <= col && col < grid[row].length
      );
    }
  }

  private static class Walker {

    private final int[][] grid;
    private int row;
    private int col;

    private int dRow = -1;
    private int dCol = 0;

    Walker(int[][] grid) {
      this.grid = grid;
      Cell start = findStart();
      this.row = start.row();
      this.col = start.col();
    }

    final Cell findStart() {
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[r].length; c++) {
          if (grid[r][c] == '^') {
            return new Cell(r, c);
          }
        }
      }
      throw new Error("wat!");
    }

    boolean inBounds(Cell cell) {
      var x = cell.inBounds(grid);
      //if (!x) System.out.println("*** OUT");
      return x;
    }

    int at(Cell c) {
      return grid[c.row()][c.col()];
    }

    void turnRight() {
      var tmpDRow = dRow == 0 ? dCol : 0;
      var tmpDCol = dCol == 0 ? -dRow : 0;
      dRow = tmpDRow;
      dCol = tmpDCol;
    }

    Cell cell() {
      return new Cell(row, col);
    }

    private boolean move() {
      //System.out.println("dir: " + dRow + "," + dCol);
      Cell next = new Cell(row + dRow, col + dCol);
      while (inBounds(next) && at(next) == '#') {
        turnRight();
        next = new Cell(row + dRow, col + dCol);
      }
      //System.out.println("Moving from " + cell() + " to " + next);
      row = next.row();
      col = next.col();
      return inBounds(next);
    }
  }


  public String part1(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    Walker w = new Walker(grid);
    Set<Cell> visited = new HashSet<>();

    do {
      visited.add(w.cell());
    } while (w.move());

    return String.valueOf(visited.size());
  }

  public String part2(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    int cells = grid.length * grid[0].length;

    Set<Cell> obstacles = new HashSet<>();

    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[r].length; c++) {
        if (grid[r][c] == '.') {
          //System.out.println("Checking %d,%d".formatted(r, c));
          int[][] copy = copyGrid(grid);
          copy[r][c] = '#';
          Walker w = new Walker(copy);
          Set<Cell> visited = new HashSet<>();

          int count = 0;
          do {
            visited.add(w.cell());
            if (count++ >= cells) {
              obstacles.add(new Cell(r, c));
              //System.out.println("Found loop");
              break;
            }
          } while (w.move());
        }
      }
    }
    return String.valueOf(obstacles.size());
  }

  private static int[][] copyGrid(int[][] grid) {
    int[][] copy = new int[grid.length][];
    for (int i = 0; i < copy.length; i++) {
      copy[i] = Arrays.copyOf(grid[i], grid[i].length);
    }
    return copy;
  }
}
