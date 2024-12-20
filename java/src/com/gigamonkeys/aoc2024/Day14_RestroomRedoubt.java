package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.Math.*;
import static java.nio.file.Files.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14_RestroomRedoubt implements Solution {

  record Problem(Size size, List<Robot> robots) {}

  record Size(int rows, int columns) {
    int quadrant(Cell cell) {
      int topBottom = cell.row() < rows / 2 ? 0 : 1;
      int leftRight = cell.column() < columns / 2 ? 0 : 1;
      return topBottom << 1 | leftRight;
    }
    boolean notOnLine(Cell cell) {
      return cell.row() != rows / 2 && cell.column() != columns / 2;
    }

    long period(GridOffset offset) {
      long p1 = rows / gcd(rows, abs(offset.dr()));
      long p2 = columns / gcd(columns, abs(offset.dc()));
      return lcm(p1, p2);
    }
  }

  record Robot(Cell start, GridOffset velocity) {
    Cell newPosition(int seconds, Size size) {
      return new Cell(
        Math.floorMod(start.row() + seconds * velocity.dr(), size.rows()),
        Math.floorMod(start.column() + seconds * velocity.dc(), size.columns())
      );
    }
  }

  public String part1(Path input) throws IOException {
    Problem prob = problem(input);
    Size size = prob.size();
    int[] quads = new int[4];
    prob.robots()
      .stream()
      .map(r -> r.newPosition(100, size))
      .filter(size::notOnLine)
      .mapToInt(size::quadrant)
      .forEach(i -> quads[i]++);
    return "" + Arrays.stream(quads).reduce(1, (a, b) -> a * b);
  }

  public String part2(Path input) throws IOException {
    Problem prob = problem(input);
    Size size = prob.size();
    return "" + prob.robots()
      .stream()
      .peek(r -> System.out.println(r + " period: " + size.period(r.velocity())))
      .mapToLong(r -> size.period(r.velocity()))
      .reduce(1, Day14_RestroomRedoubt::lcm);
  }

  private Problem problem(Path input) throws IOException {
    List<String> lines = lines(input).toList();;
    int[] sizes = Arrays.stream(lines.get(0).split("x")).mapToInt(Integer::parseInt).toArray();
    Size size = new Size(sizes[0], sizes[1]);
    List<Robot> robots = lines.subList(1, lines.size()).stream().map(this::parseRobot).toList();
    return new Problem(size, robots);
  }

  private Robot parseRobot(String text) {
    Pattern pat = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");
    Matcher m = pat.matcher(text);
    if (m.matches()) {
      var p = new Cell(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
      var v = new GridOffset(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(3)));
      return new Robot(p, v);
    } else {
      throw new RuntimeException("bad robot: " + text);
    }
  }

  private static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

}
