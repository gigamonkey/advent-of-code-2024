package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static net.berkeley.students.peterseibel.Util.*;

import static java.util.stream.Collectors.*;
import java.util.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Day01 implements Day {

  private record Columns(List<Integer> left, List<Integer> right) {
    public void add(String[] parts) {
      left.add(Integer.parseInt(parts[0]));
      right.add(Integer.parseInt(parts[1]));
    }
  }

  private Columns data(boolean test) throws IOException {
    var cols = new Columns(new ArrayList<>(), new ArrayList<>());
    columns(input(1, test)).forEach(cols::add);
    return cols;
  }

  public void part1(boolean test) throws IOException {
    var cols = data(test);
    Collections.sort(cols.left());
    Collections.sort(cols.right());
    System.out.println(
      IntStream.range(0, cols.left().size()).map(i ->  {
          return Math.abs(cols.left().get(i) - cols.right().get(i));
        }).sum()
    );
  }

  public void part2(boolean test) throws IOException {
    var cols = data(test);
    var freq = cols.right().stream().collect(groupingBy(n -> n, counting()));
    System.out.println(cols.left().stream().mapToInt(n -> n * freq.getOrDefault(n, 0L).intValue()).sum());
  }

}
