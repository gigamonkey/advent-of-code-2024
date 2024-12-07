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
import java.util.stream.*;

public class GuardGallivant implements Solution {

  public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public int rowChange() {
      return 1 - Math.abs(ordinal() - 2);
    }

    public int columnChange() {
      return 1 - Math.abs(1 - ordinal());
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

  private record Visit(Cell cell, Direction direction) {}

  private static class Walker {

    private final int[][] grid;
    private final Optional<Cell> extraObstacle;
    private final Set<Visit> path;

    private Cell position;
    private Direction direction;
    private Visit previous = null;
    private boolean hasLooped = false;

    Walker(int[][] grid) {
      this(grid, findStart(grid), Direction.NORTH, new HashSet<>(), null);
    }

    Walker(int[][] grid, Cell position, Direction direction, Set<Visit> path, Cell extraObstacle) {
      this.grid = grid;
      this.position = position;
      this.direction = direction;
      this.extraObstacle = Optional.ofNullable(extraObstacle);
      this.path = path;
      this.path.add(new Visit(position, direction));
    }

    static Cell findStart(int[][] grid) {
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[r].length; c++) {
          if (grid[r][c] == '^') {
            return new Cell(r, c);
          }
        }
      }
      throw new RuntimeException("wat! no guard!!!");
    }

    boolean move() {
      previous = new Visit(position, direction);
      path.add(previous);

      var next = next();
      while (next.inBounds(grid) && isObstacle(next)) {
        direction = direction.rightTurn();
        next = next();
      }
      position = next;
      hasLooped |= path.contains(new Visit(position, direction));
      return position.inBounds(grid);
    }

    Walker copyWithObstacle() {
      var newPath = new HashSet<>(path);
      newPath.remove(new Visit(position, direction));
      return new Walker(grid, previous.cell(), previous.direction(), newPath, position);
    }

    Cell position() {
      return position;
    }

    int at(Cell c) {
      return grid[c.row()][c.col()];
    }

    boolean isObstacle(Cell next) {
      return at(next) == '#' || extraObstacle.map(next::equals).orElse(false);
    }

    Cell next() {
      return new Cell(position.row() + direction.rowChange(), position.col() + direction.columnChange());
    }

    boolean hasLooped() {
      return hasLooped;
    }
  }

  public String part1(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    Walker w = new Walker(grid);
    Set<Cell> visited = new HashSet<>();

    do {
      visited.add(w.position());
    } while (w.move());

    return String.valueOf(visited.size());
  }

  public String part2(Path input) throws IOException {
    int[][] grid = characterGrid(input);

    Set<Cell> obstacles = new HashSet<>();
    Set<Cell> considered = new HashSet<>();

    Walker main = new Walker(grid);

    int count = 0;
    Cell cell = main.position();
    while (cell.inBounds(grid)) {
      if (main.at(cell) == '.' && !considered.contains(cell)) {
        considered.add(cell);
        Walker w = main.copyWithObstacle();
        do {
          if (w.hasLooped()) {
            obstacles.add(cell);
            break;
          }
        } while (w.move());
      }

      main.move();
      cell = main.position();
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
