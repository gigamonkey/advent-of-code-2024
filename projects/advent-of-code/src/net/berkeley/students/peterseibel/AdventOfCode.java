package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdventOfCode {

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    new Day01(true).part1();
    new Day01(false).part1();
    new Day01(true).part2();
    new Day01(false).part2();
  }
}
