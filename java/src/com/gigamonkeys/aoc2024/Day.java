package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public abstract class Day {

  private final int day;

  public Day() {
    this.day = AdventOfCode.days.size() + 1;
  }

  public int day() { return day; }

  /**
   * Solve part1 for the given input.
   */
  public abstract String part1(Path input) throws IOException;

  /**
   * Solve part2 for the given input.
   */
  public abstract String part2(Path input) throws IOException;

}
