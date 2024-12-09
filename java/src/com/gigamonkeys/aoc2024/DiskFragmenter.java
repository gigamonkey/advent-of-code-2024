package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class DiskFragmenter implements Solution {

  public String part1(Path input) throws IOException {
    List<Integer> nums = Arrays.stream(text(input).split("")).map(Integer::parseInt).toList();

    List<Integer> disk = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < nums.size(); i++) {
      if (i % 2 == 0) {
        for (int j = 0; j < nums.get(i); j++) {
          disk.add(id);
        }
        id++;
      } else {
        for (int j = 0; j < nums.get(i); j++) {
          disk.add(-1);
        }
      }
    }

    //System.out.println(disk);
    compact(disk);
    //System.out.println(disk);

    long checksum = 0;
    for (int i = 0; i < disk.size(); i++) {
      if (disk.get(i) != -1)  {
        checksum += i * disk.get(i);
      }
    }
    return String.valueOf(checksum);
  }

  private void compact(List<Integer> disk) {
    int free = 0;
    int end = disk.size() - 1;

    // move free pointer to actual free block

    while (free < disk.size() && end >= 0) {
      while (disk.get(free) != -1) free++;
      while (disk.get(end) == -1) end--;
      if (end <= free) break;
      //if (free < disk.size() && end >= 0) break;
      //System.out.println("Moving %d at %d to %d".formatted(disk.get(end), end, free));
      disk.set(free++, disk.get(end));
      disk.set(end--, -1);
    }
  }

  public String part2(Path input) throws IOException {
    return "nyi";
  }

}
