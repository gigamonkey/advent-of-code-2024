package com.gigamonkeys.aoc2024;

import java.util.Arrays;
import java.util.stream.Stream;

public enum Direction {
  NORTH(-1, 0),
  NORTH_EAST(-1, 1),
  EAST(0, 1),
  SOUTH_EAST(1, 1),
  SOUTH(1, 0),
  SOUTH_WEST(1, -1),
  WEST(0, -1),
  NORTH_WEST(-1, -1);

  private final int rowChange;
  private final int columnChange;

  private Direction(int rowChange, int columnChange) {
    this.rowChange = rowChange;
    this.columnChange = columnChange;
  }

  public static Stream<Direction> cardinal() {
    return Stream.of(NORTH, EAST, SOUTH, WEST);
  }

  public static Stream<Direction> all() {
    return Arrays.stream(values());
  }

  public int rowChange() {
    return rowChange;
  }

  public int columnChange() {
    return columnChange;
  }

  public Direction rightTurn() {
    return Direction.class.getEnumConstants()[(ordinal() + 2) % 8];
  }

  public Direction opposite() {
    return Direction.class.getEnumConstants()[(ordinal() + 4) % 8];
  }
}
