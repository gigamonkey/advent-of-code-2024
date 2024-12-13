package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.abs;
import static java.util.Arrays.stream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class Day10_HoofIt implements Solution {

  private record Hiker(Grid grid) {
    int sumScores() {
      return sumTrails(uniquePeaks());
    }

    int sumRatings() {
      return sumTrails(this::pathCounter);
    }

    int sumTrails(Supplier<ToIntFunction<Cell>> scorerSupplier) {
      return grid.cells().filter(c -> grid.at(c) == 0).mapToInt(c -> hike(c, scorerSupplier.get())).sum();
    }

    int hike(Cell cell, ToIntFunction<Cell> trailScorer) {
      if (grid.at(cell) == 9) {
        return trailScorer.applyAsInt(cell);
      } else {
        return Direction.cardinal()
          .map(cell::neighbor)
          .filter(n -> uphill(n, grid.at(cell)))
          .mapToInt(n -> hike(n, trailScorer))
          .sum();
      }
    }

    Supplier<ToIntFunction<Cell>> uniquePeaks() {
      return () -> {
        var seen = new boolean[grid.rows()][grid.columns()];
        return cell -> {
          var r = cell.row();
          var c = cell.column();
          var s = seen[r][c] ? 0 : 1;
          seen[r][c] = true;
          return s;
        };
      };
    }

    ToIntFunction<Cell> pathCounter() {
      return cell -> 1;
    }

    boolean uphill(Cell cell, int current) {
      return grid.inBounds(cell) && grid.at(cell) - current == 1;
    }
  }

  public String part1(Path input) throws IOException {
    return String.valueOf(new Hiker(new Grid(digitGrid(input))).sumScores());
  }

  public String part2(Path input) throws IOException {
    return String.valueOf(new Hiker(new Grid(digitGrid(input))).sumRatings());
  }
}
