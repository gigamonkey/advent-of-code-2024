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

public class GuardGallivant implements Solution {

  private record Cell(int r, int c) {

    public boolean inBounds(int[][] grid) {
      return 0 <= r && r < grid.length && 0 <= c && c < grid[r].length;
    }
  }

  private record Foo(Cell cell, Direction direction) {}

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

    public int dr() { return dr; }
    public int dc() { return dc; }

    public Direction rightTurn() {
      return Direction.class.getEnumConstants()[(ordinal() + 1) % 4];
    }
  }

  public String part1(Path input) throws IOException {
    int[][] grid = characterGrid(input);
    Foo p = new Foo(findStart(grid), Direction.NORTH);
    Set<Cell> visited = new HashSet<>();
    while (p.cell().inBounds(grid)) {
      visited.add(p.cell());
      p = move(grid, p);
    }
    return String.valueOf(visited.size());
  }

  public String part2(Path input) throws IOException {

    int[][] grid = characterGrid(input);

    //System.out.println(grid.length + " by " + grid[0].length);

    Cell start = findStart(grid);
    Foo p = new Foo(start, Direction.NORTH);

    Set<Foo> path = new HashSet<>();
    Set<Cell> obstacles = new HashSet<>();

    while (p.cell().inBounds(grid)) {

      path.add(p);

      // Where we might place an obstacle
      Cell next = next(p);

      if (next.inBounds(grid) && !next.equals(start) && at(grid, next) != '#') {
        // Explore where we'd end up if next was actually an obstacle. This will
        // either take us off the board or into a loop.
        if (isLoopToRight(grid, p, path, next)) {
          //System.out.println("Adding obstacle at " + next + " from " + p);
          obstacles.add(next);
        }
      }
      p = move(grid, p);
    }
    return String.valueOf(obstacles.size());
  }

  private Cell findStart(int[][] grid) {
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[r].length; c++) {
        if (grid[r][c] == '^') {
          return new Cell(r, c);
        }
      }
    }
    throw new Error("wat!");
  }

  private Foo move(int[][] grid, Foo foo) {
    Cell cell = foo.cell();
    Direction d = foo.direction();
    var next = next(foo);
    if (next.inBounds(grid)) {
      if (at(grid, next) == '#') {
        return move(grid, new Foo(cell, d.rightTurn()));
      }
    }
    return new Foo(next, d);
  }

  private Foo move2(int[][] grid, Foo foo, Cell extra) {
    Cell cell = foo.cell();
    Direction d = foo.direction();
    var next = next(foo);
    if (next.inBounds(grid)) {
      if (at(grid, next) == '#' || next.equals(extra)) {
        return move2(grid, new Foo(cell, d.rightTurn()), extra);
      }
    }
    return new Foo(next, d);
  }

  private Cell next(Foo foo) {
    Cell cell = foo.cell();
    Direction d = foo.direction();
    return new Cell(cell.r() + d.dr(), cell.c() + d.dc());
  }

  private int at(int[][] grid, Cell cell) {
    return grid[cell.r()][cell.c()];
  }

  private Foo nextFoo(Foo foo) {
    return new Foo(next(foo), foo.direction());
  }

  private boolean isLoopToRight(int[][] grid, Foo foo, Set<Foo> history, Cell obstacle) {

    Set<Foo> path = new HashSet<>(history);
    Foo current = move2(grid, foo, obstacle);
    while (true) {
      if (!current.cell().inBounds(grid)) return false;
      if (path.contains(current)) {
        return true;
      }
      path.add(current);
      current = move2(grid, current, obstacle);
    }
  }
}
