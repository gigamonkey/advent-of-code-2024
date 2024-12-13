package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Direction.*;
import static com.gigamonkeys.aoc2024.Util.*;
import static com.gigamonkeys.aoc2024.Util.characterGrid;
import static com.gigamonkeys.aoc2024.Util.columns;
import static com.gigamonkeys.aoc2024.Util.text;
import static java.lang.Integer.*;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.System.*;
import static java.lang.System.nanoTime;
import static java.nio.file.Files.*;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.lines;
import static java.nio.file.Files.readString;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.*;
import static java.util.Arrays.stream;
import static java.util.Map.Entry;
import static java.util.Map.entry;
import static java.util.function.Function.*;
import static java.util.regex.Pattern.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.*;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.*;
import static java.util.stream.Stream.generate;

import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.function.BinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.*;
import java.util.regex.MatchResult;
import java.util.stream.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13_ClawContraption implements Solution {

  private record Point(int x, int y) {}

  private record Presses(int a, int b) {
    int cost() {
      return a * 3 + b;
    }
  }

  private record Machine(int ax, int ay, int bx, int by, int x, int y) {
    Point position(int a, int b) {
      return new Point(ax * a + bx * b, ay * a + by * b);
    }

    boolean winner(int a, int b) {
      return x == (ax * a + bx * b) && y == (ay * a + by * b);
    }
  }

  private static final Pattern button = Pattern.compile("Button .: X\\+(\\d+), Y\\+(\\d+)");
  private static final Pattern prize = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

  public String part1(Path input) throws IOException {
    var machines = machines(input);
    return String.valueOf(machines.stream().map(m -> winner(m)).mapToInt(op -> op.map(Presses::cost).orElse(0)).sum());
  }

  public String part2(Path input) throws IOException {
    return "nyi";
  }

  private Optional<Presses> winner(Machine m) {
    return range(0, 100)
      .boxed()
      .flatMap(a -> range(0, 100).filter(b -> m.winner(a, b)).mapToObj(b -> new Presses(a, b)))
      .collect(minBy(Comparator.comparingInt(Presses::cost)));
  }

  private List<Machine> machines(Path input) throws IOException {
    String text = text(input);
    String[] chunks = text.split("\n\n");

    return stream(chunks)
      .map(chunk -> {
        var m = button.matcher(chunk);
        m.find();
        int ax = Integer.parseInt(m.group(1));
        int ay = Integer.parseInt(m.group(2));
        m.find();
        int bx = Integer.parseInt(m.group(1));
        int by = Integer.parseInt(m.group(2));
        var m2 = prize.matcher(chunk);
        m2.find();
        int x = Integer.parseInt(m2.group(1));
        int y = Integer.parseInt(m2.group(2));
        return new Machine(ax, ay, bx, by, x, y);
      })
      .toList();
  }
}
