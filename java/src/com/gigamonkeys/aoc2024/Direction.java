package com.gigamonkeys.aoc2024;

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
