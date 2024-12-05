package com.gigamonkeys.aoc2024;

import static com.gigamonkeys.aoc2024.Util.text;
import static java.lang.Integer.*;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class MullItOver implements Solution {

  private static final Pattern MUL = compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
  private static final Pattern MUL_2 = compile("(?:mul\\((\\d{1,3}),(\\d{1,3})\\))|(do(?:n't)?\\(\\))");

  public String part1(Path input) throws IOException {
    return String.valueOf(MUL.matcher(text(input)).results().mapToInt(this::mul).sum());
  }

  public String part2(Path input) throws IOException {
    var testdata = text(input);
    var enabled = true;
    var total = 0;
    var i = MUL_2.matcher(testdata).results().iterator();
    while (i.hasNext()) {
      var r = i.next();
      if (r.group(3) != null) {
        enabled = r.group(3).equals("do()");
      } else {
        if (enabled) {
          total += mul(r);
        }
      }
    }
    return String.valueOf(total);
  }

  private int mul(MatchResult r) {
    return parseInt(r.group(1)) * parseInt(r.group(2));
  }
}
