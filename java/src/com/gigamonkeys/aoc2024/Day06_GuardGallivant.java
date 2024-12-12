package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.characterGrid;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Day06_GuardGallivant implements Solution {

  private record Visit(Cell cell, Direction direction) {}

  private static class Guard {

    private final Grid grid;
    private final Optional<Cell> extraObstacle;
    private final Set<Visit> path;

    private Cell position;
    private Direction direction;
    private Visit current;
    private Visit previous = null;
    private boolean hasLooped = false;

    Guard(Grid grid) {
      this(grid, findStart(grid), Direction.NORTH, new HashSet<>(), null);
    }

    Guard(Grid grid, Cell position, Direction direction, Set<Visit> path, Cell extraObstacle) {
      this.grid = grid;
      this.position = position;
      this.direction = direction;
      this.path = path;
      this.extraObstacle = Optional.ofNullable(extraObstacle);
      this.current = new Visit(position, direction);
      path.add(current);
    }

    static Cell findStart(Grid grid) {
      return grid.cells().filter(cell -> grid.at(cell) == '^').findFirst().orElseThrow();
    }

    Cell move() {
      var next = position.neighbor(direction);
      while (grid.inBounds(next) && isObstacle(next)) {
        direction = direction.rightTurn();
        next = position.neighbor(direction);
      }
      position = next;
      previous = current;
      current = new Visit(position, direction);
      hasLooped |= !path.add(current);
      return position;
    }

    Guard copyWithObstacle() {
      return new Guard(grid, previous.cell(), previous.direction(), new HashSet<>(path), position);
    }

    Cell position() {
      return position;
    }

    boolean isObstacle(Cell next) {
      return grid.at(next) == '#' || extraObstacle.map(next::equals).orElse(false);
    }

    boolean causesLoop() {
      while (!hasLooped && grid.inBounds(position)) {
        move();
      }
      return hasLooped;
    }
  }

  public String part1(Path input) throws IOException {
    Grid grid = new Grid(characterGrid(input));
    Guard guard = new Guard(grid);
    return String.valueOf(iterate(guard.position(), grid::inBounds, c -> guard.move()).collect(toSet()).size());
  }

  public String part2(Path input) throws IOException {
    Grid grid = new Grid(characterGrid(input));
    Guard guard = new Guard(grid);
    Set<Cell> considered = new HashSet<>();
    int obstacles = 0;

    Cell cell = guard.position();
    while (grid.inBounds(cell)) {
      if (grid.at(cell) == '.' && !considered.contains(cell)) {
        considered.add(cell);
        if (guard.copyWithObstacle().causesLoop()) {
          obstacles++;
        }
      }
      cell = guard.move();
    }
    return String.valueOf(obstacles);
  }
}
