package net.berkeley.students.peterseibel;

import static java.nio.file.Files.lines;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdventOfCode {

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");
    Day.number(1, true).part1();
    Day.number(1, false).part1();
    Day.number(1, true).part2();
    Day.number(1, false).part2();
  }
}
