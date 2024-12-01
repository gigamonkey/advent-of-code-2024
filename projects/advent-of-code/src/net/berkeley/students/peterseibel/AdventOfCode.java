package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdventOfCode {

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    Day day = Day.number(1);

    day.part1(true);
    day.part1(false);
    day.part2(true);
    day.part2(false);
  }
}
