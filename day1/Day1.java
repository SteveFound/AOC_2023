package day1;

import util.Day;
import util.FileLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1 implements Day {
    /**
     * Find the values of the first and last elements of a text digit representation in a string and
     * return it as a 2 digit integer.
     * @param line file contents
     * @param digits map of text representations of digits and their numeric values.
     * @return integer created by the first digit * 10 + the last digit. 0 if there are no digits.
     */
    private int firstAndLast(String line, Map<String, Integer> digits) {
        int first = 0, last = 0;
        int firstIdx = 20000;
        int lastIdx = -1;
        int idx;
        String lcase = line.toLowerCase();
        for( String key : digits.keySet()) {
            if( (idx = lcase.indexOf(key)) > -1) {
                if( idx < firstIdx ) {
                    firstIdx = idx;
                    first = digits.get(key);
                }
            }
            if( (idx = lcase.lastIndexOf(key)) > -1) {
                if( idx > lastIdx) {
                    lastIdx = idx;
                    last = digits.get(key);
                }
            }
        }
        return first * 10 + last;
    }

    public void run() {
        Map<String, Integer> digits = Stream.of(new Object[][] {
                {"0", 0}, {"1", 1}, {"2", 2}, {"3", 3}, {"4", 4},
                {"5", 5}, {"6", 6}, {"7", 7}, {"8", 8}, {"9", 9}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        Map<String, Integer> wordsAndDigits = Stream.of(new Object[][] {
                {"zero", 0},  {"0", 0},
                {"one", 1},   {"1", 1},
                {"two", 2},   {"2", 2},
                {"three", 3}, {"3", 3},
                {"four", 4},  {"4", 4},
                {"five", 5},  {"5", 5},
                {"six", 6},   {"6", 6},
                {"seven", 7}, {"7", 7},
                {"eight", 8}, {"8", 8},
                {"nine", 9},  {"9", 9}
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        List<String> fileContent = FileLoader.readFile("input/day1.txt");
        int sum = fileContent.stream().mapToInt(line -> firstAndLast(line,digits)).sum();
        int sum2 = fileContent.stream().mapToInt(line -> firstAndLast(line, wordsAndDigits)).sum();
        System.out.println("Digits Sum : " + sum);
        System.out.println("Text and Digits sum : " + sum2);
    }
}
