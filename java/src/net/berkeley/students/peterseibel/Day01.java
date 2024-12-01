package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.*;
import static net.berkeley.students.peterseibel.Util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class Day01 extends Day {

  public Day01() {
    super(1);
  }

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

  public String part1(boolean test) throws IOException {
    var cols = data(test);
    Collections.sort(cols.left());
    Collections.sort(cols.right());
    return String.valueOf(
      IntStream.range(0, cols.left().size())
        .map(i -> {
          return Math.abs(cols.left().get(i) - cols.right().get(i));
        })
        .sum()
    );
  }

  public String part2(boolean test) throws IOException {
    var cols = data(test);
    var freq = cols.right().stream().collect(groupingBy(n -> n, counting()));
    return String.valueOf(
      (cols.left().stream().mapToInt(n -> n * freq.getOrDefault(n, 0L).intValue()).sum())
    );
  }
}
