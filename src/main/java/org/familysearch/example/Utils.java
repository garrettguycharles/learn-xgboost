package org.familysearch.example;

import akka.japi.Util;
import scala.tools.jline_embedded.internal.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

  public static List<String> readLines(String fileName) {
    try {
      return Files.lines(Paths.get(fileName)).collect(Collectors.toList());
    }
    catch (IOException e) {
      System.err.println("Unable to read from file " + fileName);
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  public static List<Integer> intArrayToList(int[] dateFields) {
    List<Integer> intList = new ArrayList<>();
    for (final int dateField : dateFields) {
      intList.add(dateField);
    }
    return intList;
  }

  public static int editDistance(String word1, String word2) {
    // taken from https://www.programcreek.com/2013/12/edit-distance-in-java/
    int len1 = word1.length();
    int len2 = word2.length();

    // len1+1, len2+1, because finally return dp[len1][len2]
    int[][] dp = new int[len1 + 1][len2 + 1];

    for (int i = 0; i <= len1; i++) {
      dp[i][0] = i;
    }

    for (int j = 0; j <= len2; j++) {
      dp[0][j] = j;
    }

    //iterate though, and check last char
    for (int i = 0; i < len1; i++) {
      char c1 = word1.charAt(i);
      for (int j = 0; j < len2; j++) {
        char c2 = word2.charAt(j);

        //if last two chars equal
        if (c1 == c2) {
          //update dp value for +1 length
          dp[i + 1][j + 1] = dp[i][j];
        } else {
          int replace = dp[i][j] + 1;
          int insert = dp[i][j + 1] + 1;
          int delete = dp[i + 1][j] + 1;

          int min = replace > insert ? insert : replace;
          min = delete > min ? min : delete;
          dp[i + 1][j + 1] = min;
        }
      }
    }

    return dp[len1][len2];
  }

  /**
   * Get a bucketed score between 0 and numBuckets for how well first and second match
   * eachother according to a longest-common-subsequence edit distance.
   *
   * @param first the first string to compare
   * @param second the second string to compare
   * @param numBuckets the number of score buckets to choose from (greater than 0)
   * @return a number between 0 and numBuckets, where numBuckets is the highest quality match.
   */
  public static int editDistanceScore(String first, String second, int numBuckets) {

    int lengthDifference = Math.abs(first.length() - second.length());
    int editDistance = Utils.editDistance(first, second);

    int lengthAdjustedEditDistance = editDistance - lengthDifference;

    float editDistanceLengthRatio = lengthAdjustedEditDistance / (float) Math.min(first.length(), second.length());

    // place in numBuckets buckets
    int roundedBucketValue = (int) Math.floor(editDistanceLengthRatio * (numBuckets + 1));
    return Math.max(0, numBuckets - roundedBucketValue);
  }

  public static void main(String[] args) {
    System.out.println(Utils.editDistance("Mabel Doherty", "Mabel Ruth Weaver"));
    System.out.println(Utils.editDistanceScore("Margot Doherty", "Mabel Ruth Weaver", 5));
  }
}
