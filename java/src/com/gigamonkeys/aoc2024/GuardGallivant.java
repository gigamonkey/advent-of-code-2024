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

    public int dr() {
      return 1 - Math.abs(ordinal() - 2);
    }

    public int dc() {
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
    private final Optional<Cell> obstacle;

    private Cell position;
    private Set<Visit> path = new HashSet<>();
    private boolean hasLooped = false;
    private Visit previous = null;

    private Direction direction = Direction.NORTH;

    Walker(int[][] grid) {
      this(grid, Optional.empty());
    }

    Walker(int[][] grid, Optional<Cell> obstacle) {
      this.grid = grid;
      this.obstacle = obstacle;
      this.position = findStart();
      path.add(new Visit(position, direction));
    }

    Walker(int[][] grid, Cell start, Direction d, Set<Visit> path, Cell obstacle) {
      this.grid = grid;
      this.obstacle = Optional.of(obstacle);
      this.position = start;
      this.direction = d;
      this.path = path;
      path.add(new Visit(start, d));
    }

    Walker copyWithObstacle() {
      //System.out.println("position: %s, %s; previous: %s".formatted(position, direction, previous));
      Set<Visit> newPath = new HashSet<>(path);
      newPath.remove(new Visit(position, direction));
      return new Walker(grid, previous.cell(), previous.direction(), newPath, position);
    }

    final Cell findStart() {
      for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[r].length; c++) {
          if (grid[r][c] == '^') {
            return new Cell(r, c);
          }
        }
      }
      throw new RuntimeException("wat! no guard!!!");
    }

    Cell position() {
      return position;
    }

    int at(Cell c) {
      return grid[c.row()][c.col()];
    }

    boolean move() {
      previous = new Visit(position, direction);
      path.add(previous);
      Cell next = new Cell(position.row() + direction.dr(), position.col() + direction.dc());
      while (next.inBounds(grid) && (at(next) == '#' || obstacle.map(next::equals).orElse(false))) {
        direction = direction.rightTurn();
        next = new Cell(position.row() + direction.dr(), position.col() + direction.dc());
      }
      position = next;
      Visit nextVisit = new Visit(position, direction);
      boolean wasLooped = hasLooped;
      hasLooped |= path.contains(nextVisit);
      if (!wasLooped && hasLooped) {
        //System.out.println("Noted loop at %s from %s".formatted(next, previous));
      }
      return position.inBounds(grid);
    }

    Cell next() {
      return new Cell(position.row() + direction.dr(), position.col() + direction.dc());
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

    Walker main = new Walker(grid);

    int count = 0;
    Cell cell = main.position();
    while (cell.inBounds(grid)) {
      if (main.at(cell) == '.' ) {
        count++;
        Walker w = new Walker(grid, Optional.of(cell));
        //Walker w = main.copyWithObstacle();
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
    System.out.println("count: %d".formatted(count));
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
