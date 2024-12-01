package net.berkeley.students.peterseibel;

import java.io.*;
import java.nio.file.*;
import static java.nio.file.Files.lines;

public class AdventOfCode {
  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    lines(Path.of("inputs/day-01-test.txt")).forEach(System.out::println);
  }
}
