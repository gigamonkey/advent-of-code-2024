package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class DiskFragmenter implements Solution {

  public String part1(Path input) throws IOException {
    List<Integer> disk = mapDisk(input);
    compact(disk);
    return String.valueOf(checksum(disk));
  }

  public String part2(Path input) throws IOException {
    List<Integer> disk = mapDisk(input);
    compact2(disk);
    return String.valueOf(checksum(disk));
  }

  private List<Integer> mapDisk(Path input) throws IOException {
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
    return disk;
  }

  private void compact(List<Integer> disk) {
    int free = 0;
    int end = disk.size() - 1;

    while (free < disk.size() && end >= 0) {
      while (disk.get(free) != -1) free++;
      while (disk.get(end) == -1) end--;
      if (end <= free) break;
      disk.set(free++, disk.get(end));
      disk.set(end--, -1);
    }
  }

  private void compact2(List<Integer> disk) {
    int end = disk.size() - 1;

    while (end >= 0) {
      // Move the end back to point at the back end of the next file.
      while (end >= 0 && disk.get(end) == -1) end--;

      // Could be done.
      if (end == -1) break;

      int fileLength = fileLength(disk, end);
      int freeStart = findFreeSpace(disk, fileLength, end);

      if (freeStart != -1) {
        int free = freeStart;
        for (int j = 0; j < fileLength; j++) {
          disk.set(free++, disk.get(end));
          disk.set(end--, -1);
        }
      } else {
        // Couldn't find room for this file so skip it.
        end -= fileLength;
      }
    }
  }

  private int findFreeSpace(List<Integer> disk, int needed, int end) {
    int limit = end + 1 - needed * 2;
    search: for (int i = 0; i <= limit; i++) {
      for (int j = 0; j < needed; j++) {
        if (disk.get(i + j) != -1) continue search;
      }
      return i;
    }
    return -1;
  }

  private int fileLength(List<Integer> disk, int end) {
    int id = disk.get(end);
    int start = end;
    while (end >= 0 && disk.get(end) == id) end--;
    return start - end;
  }

  private long checksum(List<Integer> disk) {
    long checksum = 0;
    for (int i = 0; i < disk.size(); i++) {
      if (disk.get(i) != -1) {
        checksum += i * disk.get(i);
      }
    }
    return checksum;
  }
}
