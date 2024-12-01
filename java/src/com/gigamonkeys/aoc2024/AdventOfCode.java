package com.gigamonkeys.aoc2024;

import static java.nio.file.Files.lines;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class AdventOfCode {

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");

    if (args.length < 2) {
      System.out.println("Provide day and part to run code.");
    } else {
      var day = Integer.parseInt(args[0]);
      var part = Integer.parseInt(args[1]);

      var d = Day.number(day);
      d.part(part, true);
      d.part(part, false);
    }
  }
}
