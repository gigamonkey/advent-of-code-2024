package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Direction.*;
import static com.gigamonkeys.aoc2024.Util.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

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
        if (!grid.inBounds(n) || grid.at(n) != grid.at(cell)) {
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
          .map(d -> bordering(unique, d, Cell::row))
          .mapToInt(facing -> countSides(facing, Cell::column))
          .sum() +
        Stream.of(EAST, WEST)
          .map(d -> bordering(unique, d, Cell::column))
          .mapToInt(facing -> countSides(facing, Cell::row))
          .sum()
      );
    }

    private Map<Integer, List<Cell>> bordering(Set<Cell> unique, Direction dir, Function<Cell, Integer> groupBy) {
      return unique.stream().filter(c -> members.contains(c.neighbor(dir))).collect(groupingBy(groupBy));
    }

    private int countSides(Map<Integer, List<Cell>> facing, Function<Cell, Integer> extract) {
      return facing.values().stream().mapToInt(cells -> segments(cells.stream().map(extract).sorted().toList())).sum();
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
