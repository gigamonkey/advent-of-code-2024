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
      //System.out.println(p.cell());
      visited.add(p.cell());
      p = move(grid, p);
    }
    return String.valueOf(visited.size());
  }

  public String part2(Path input) throws IOException {
    // Walk the route but whenever we are on a cell we have previously occupied
    // and the cell to our right has been previously occupied while we were
    // going in the direction we'd be going if we turned right and the cell in
    // front of us is not the starting position, putting an obstacle in that
    // cell would create a loop because it would make us turn right and into a
    // path we've already been on.
    int[][] grid = characterGrid(input);
    Cell start = findStart(grid);
    Foo p = new Foo(start, Direction.NORTH);

    Set<Foo> foos = new HashSet<>();
    Set<Cell> visited = new HashSet<>();
    Set<Cell> obstacles = new HashSet<>();

    while (p.cell().inBounds(grid)) {

      // Where we might place an obstacle
      Cell next = next(p);

      if (!next.equals(start)) {
        // Looking to the right from where we are, can we see a cell that we've
        // previously occupied moving in the same direction. If so, turning
        // right now would cause us to enter a loop.

        // Explore where we'd end up if we took a right turn now. This will
        // either take us off the board or into a loop
        if (loopToRight(grid, p, foos)) {
          obstacles.add(next);
        }
      }
      visited.add(p.cell());
      foos.add(p);

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
      if (grid[next.r()][next.c()] == '#') {
        return move(grid, new Foo(cell, d.rightTurn()));
      }
    }
    return new Foo(next, d);
  }

  private Cell next(Foo foo) {
    Cell cell = foo.cell();
    Direction d = foo.direction();
    return new Cell(cell.r() + d.dr(), cell.c() + d.dc());
  }

  private Foo toRight(Foo foo) {
    Cell cell = foo.cell();
    Direction d = foo.direction();
    Direction newD = d.rightTurn();
    return new Foo(new Cell(cell.r() + newD.dr(), cell.c() + newD.dc()), newD);
  }

  private int at(int[][] grid, Cell cell) {
    return grid[cell.r()][cell.c()];
  }

  private Foo nextFoo(Foo foo) {
    return new Foo(next(foo), foo.direction());
  }

  private Set<Foo> pathToRight(int[][] grid, Foo foo, Set<Foo> history) {
    // Current
    Cell cell = foo.cell();
    Direction d = foo.direction();

    Direction newD = d.rightTurn();
    Set<Foo> foos = new HashSet<>(history);

    foo = new Foo(cell, newD);
    while (foo.cell().inBounds(grid) && !foos.contains(foo)) {
      foos.add(foo);
      foo = move(grid, foo);
    }
    return foos;
  }


  private boolean loopToRight(int[][] grid, Foo foo, Set<Foo> history) {
    // Current
    Cell cell = foo.cell();
    Direction d = foo.direction();

    Direction newD = d.rightTurn();
    Set<Foo> foos = new HashSet<>(history);

    foo = new Foo(cell, newD);
    while (true) {
      if (!foo.cell().inBounds(grid)) return false;
      if (foos.contains(foo)) return true;
      foos.add(foo);
      foo = move(grid, foo);
    }
  }





}
