package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdventOfCode {

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    day1(false);
  }

  public static Path input(int day, boolean test) {
    return Path.of("inputs/day-%02d%s.txt".formatted(day, test ? "-test" : ""));
  }

  public static void day1(boolean test) throws IOException {
    var left = new ArrayList<Integer>();
    var right = new ArrayList<Integer>();

    lines(input(1, test)).forEach(line -> {
      var parts = line.split("\\s+");
      left.add(Integer.parseInt(parts[0]));
      right.add(Integer.parseInt(parts[1]));
    });

    Collections.sort(left);
    Collections.sort(right);

    var total = 0;
    for (int i = 0; i < left.size(); i++) {
      total += Math.abs(left.get(i) - right.get(i));
    }
    System.out.println(total);
  }
}
