package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Direction.*;
import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Day12_GardenGroups implements Solution {

  static class Region {

    private Set<Cell> members = new HashSet<>();
    private List<Cell> boundary = new ArrayList<>();

    Region(Cell cell, Grid grid) {
      walk(cell, grid);
    }

    private void walk(Cell cell, Grid grid) {
      members.add(cell);
      for (Cell n : grid.neighbors(cell)) {
        if (!grid.inBounds(n)) {
          boundary.add(n);
        } else if (grid.at(n) != grid.at(cell)) {
          boundary.add(n);
        } else if (!members.contains(n)) {
          walk(n, grid);
        }
      }
    }

    public int area() {
      return members.size();
    }

    public int fenceSegments() {
      return boundary.size();
    }

    public int sides() {
      var unique = new HashSet<Cell>(boundary);
      return (
        Stream.of(NORTH, SOUTH)
          .mapToInt(d -> countSides(bordering(unique, members, d, Cell::row), Cell::column))
          .sum() +
        Stream.of(EAST, WEST).mapToInt(d -> countSides(bordering(unique, members, d, Cell::column), Cell::row)).sum()
      );
    }

    private int countSides(Map<Integer, List<Cell>> facing, Function<Cell, Integer> extract) {
      return facing.values().stream().mapToInt(cells -> segments(cells.stream().map(extract).sorted().toList())).sum();
    }

    private Map<Integer, List<Cell>> bordering(
      Set<Cell> unique,
      Set<Cell> members,
      Direction dir,
      Function<Cell, Integer> groupBy
    ) {
      return unique.stream().filter(c -> members.contains(c.neighbor(dir))).collect(groupingBy(groupBy));
    }

    private int segments(List<Integer> numbers) {
      int segments = 1;
      for (int i = 0; i < numbers.size() - 1; i++) {
        if (numbers.get(i) + 1 != numbers.get(i + 1)) {
          segments++;
        }
      }
      return segments;
    }
  }

  public String part1(Path input) throws IOException {
    return survey(input, Region::fenceSegments);
  }

  public String part2(Path input) throws IOException {
    return survey(input, Region::sides);
  }

  private String survey(Path input, Function<Region, Integer> measurer) throws IOException {
    var grid = new Grid(characterGrid(input));
    var seen = new HashSet<Cell>();
    var total = 0;

    for (Cell cell : grid) {
      if (!seen.contains(cell)) {
        var region = new Region(cell, grid);
        total += region.area() * measurer.apply(region);
        seen.addAll(region.members);
      }
    }
    return String.valueOf(total);
  }
}
