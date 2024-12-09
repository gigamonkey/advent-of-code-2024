package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.*;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;
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
    int[] disk = mapDisk(input);
    compact(disk);
    return String.valueOf(checksum(disk));
  }

  public String part2(Path input) throws IOException {
    int[] disk = mapDisk(input);
    compact2(disk);
    return String.valueOf(checksum(disk));
  }

  private int[] mapDisk(Path input) throws IOException {
    var nums = Arrays.stream(text(input).split("")).map(Integer::parseInt).toList();
    var disk = new ArrayList<Integer>();
    var id = 0;
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
    return disk.stream().mapToInt(n -> n).toArray();
  }

  private void compact(int[] disk) {
    int free = 0;
    int end = disk.length - 1;

    while (free < disk.length && end >= 0) {
      while (disk[free] != -1) free++;
      while (disk[end] == -1) end--;
      if (end <= free) break;
      disk[free++] = disk[end];
      disk[end--] = -1;
    }
  }

  private void compact2(int[] disk) {
    int end = disk.length - 1;

    while (end >= 0) {
      // Move the end back to point at the back end of the next file.
      while (end >= 0 && disk[end] == -1) end--;

      // Could be done.
      if (end == -1) break;

      int fileLength = fileLength(disk, end);
      int freeStart = findFreeSpace(disk, fileLength, end);

      if (freeStart != -1) {
        int fileStart = end + 1 - fileLength;
        arraycopy(disk, fileStart, disk, freeStart, fileLength);
        fill(disk, fileStart, end + 1, -1);
      }
      end -= fileLength;
    }
  }

  private int findFreeSpace(int[] disk, int needed, int end) {
    int limit = end + 1 - needed * 2;
    search: for (int i = 0; i <= limit; i++) {
      for (int j = 0; j < needed; j++) {
        if (disk[i + j] != -1) continue search;
      }
      return i;
    }
    return -1;
  }

  private int fileLength(int[] disk, int end) {
    int id = disk[end];
    int start = end;
    while (end >= 0 && disk[end] == id) end--;
    return start - end;
  }

  private long checksum(int[] disk) {
    return IntStream.range(0, disk.length).filter(i -> disk[i] != -1).mapToLong(i -> i * disk[i]).sum();
  }
}
