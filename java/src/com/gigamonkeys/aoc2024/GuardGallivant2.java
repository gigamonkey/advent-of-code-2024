package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.lines;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuardGallivant2 implements Solution {

  public enum Direction {

    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    private final int dr;
    private final int dc;

    Direction(int dr, int dc) {
      this.dr = dr;
      this.dc = dc;
    }

    public int dr() {
      return dr;
    }

    public int dc() {
      return dc;
    }

    public Direction rightTurn() {
      return Direction.class.getEnumConstants()[(ordinal() + 1) % 4];
    }
  }

  private record Cell(int row, int col) {
    public boolean inBounds(int[][] grid) {
      return (0 <= row && row < grid.length && 0 <= col && col < grid[row].length);
    }
  }

  private record Foo(Cell cell, Direction direction) {}

  private static class Walker {

    private final int[][] grid;
    private int row;
    private int col;

    private Direction direction = Direction.NORTH;

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
      return cell.inBounds(grid);
    }

    int at(Cell c) {
      return grid[c.row()][c.col()];
    }

    Cell cell() {
      return new Cell(row, col);
    }

    private boolean move() {
      Cell next = new Cell(row + direction.dr(), col + direction.dc());
      while (inBounds(next) && at(next) == '#') {
        direction = direction.rightTurn();
        next = new Cell(row + direction.dr(), col + direction.dc());
      }
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

    Set<Foo> paths = new HashSet<>();
    Set<Cell> obstacles = new HashSet<>();
    Set<Cell> obstacles2 = new HashSet<>();

    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[r].length; c++) {
        if (grid[r][c] == '.') {
          int[][] copy = copyGrid(grid);
          copy[r][c] = '#';
          Walker w = new Walker(copy);
          Set<Cell> visited = new HashSet<>();

          // Just gonna detect loops in the dumbest possible way
          int count = 0;
          do {
            visited.add(w.cell());
            if (count++ >= cells) {
              obstacles.add(new Cell(r, c));
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