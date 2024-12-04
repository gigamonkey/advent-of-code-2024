package com.gigamonkeys.aoc2024;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.IOException;
import java.time.*;

public class AdventOfCode {

  private static final ZoneId TZ = ZoneId.of("America/New_York");
  private static final ZonedDateTime START = ZonedDateTime.of(2024, 12, 1, 0, 0, 0, 0, TZ);
  private static final ZonedDateTime NOW = ZonedDateTime.now(TZ);

  private static final int MAX_DAY = (int) DAYS.between(START, NOW) + 1;

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to Advent of Code!");

    int start = args.length > 0 && args[0].equals("--all") ? 1 : MAX_DAY;

    boolean okay = true;

    for (var day = start; day <= MAX_DAY; day++) {
      for (var part = 1; part <= 2; part++) {
        var d = Day.number(day);
        okay &= d.part(part, true);
        okay &= d.part(part, false);
      }
    }
    if (okay) {
      System.out.println("\nAll okay!");
    } else {
      System.out.println("\nUh oh!");
    }
  }
}
