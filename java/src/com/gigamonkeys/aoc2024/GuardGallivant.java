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
    var next = new Cell(cell.r() + d.dr(), cell.c() + d.dc());
    if (next.inBounds(grid)) {
      if (grid[next.r()][next.c()] == '#') {
        return move(grid, new Foo(cell, d.rightTurn()));
      }
    }
    return new Foo(next, d);
  }

  public String part2(Path input) throws IOException {
    return "nyi";
  }



}
