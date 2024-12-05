package com.gigamonkeys.aoc2024;

import java.io.IOException;
import java.nio.file.Path;

public interface Solution {

  /**
   * Solve part1 for the given input.
   */
  public abstract String part1(Path input) throws IOException;

  /**
   * Solve part2 for the given input.
   */
  public abstract String part2(Path input) throws IOException;

}
