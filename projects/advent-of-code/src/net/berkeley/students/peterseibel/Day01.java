package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;
import static net.berkeley.students.peterseibel.Util.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Day01 {

  private final boolean test;

  public Day01(boolean test) {
    this.test = test;
  }

  private record Columns(List<Integer> left, List<Integer> right) {
    public void add(String[] parts) {
      left.add(Integer.parseInt(parts[0]));
      right.add(Integer.parseInt(parts[1]));
    }
  }

  public void part1() throws IOException {
    var cols = data();
    Collections.sort(cols.left());
    Collections.sort(cols.right());

    var total = 0;
    for (int i = 0; i < cols.left().size(); i++) {
      total += Math.abs(cols.left().get(i) - cols.right().get(i));
    }
    System.out.println(total);
  }

  private Columns data() throws IOException {
    var cols = new Columns(new ArrayList<>(), new ArrayList<>());
    columns(input(1, 1, test)).forEach(cols::add);
    return cols;
  }

}
