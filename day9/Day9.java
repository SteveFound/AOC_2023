package day9;

import util.Day;
import util.FileLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day9 implements Day {

    /**
     * Create a list of numbers that are the differences between the values in a given sequence
     * @param sequence current sequence
     * @return list of differences
     */
    private List<Integer> getDifferences(List<Integer> sequence) {
        List<Integer> differences = new ArrayList<>();
        for (int i = 0; i < sequence.size() - 1; i++) {
            differences.add(sequence.get(i + 1) - sequence.get(i));
        }
        return differences;
    }

    /**
     * Recursively calculate the next number in a sequence
     * @param sequence the sequence to extend
     * @return the next value in the sequence
     */
    private int getNextNumber(List<Integer> sequence) {
        List<Integer> differences = getDifferences(sequence);
        if (differences.stream().anyMatch(x -> x != 0))
            return sequence.get(sequence.size() - 1) + getNextNumber(differences);
        return sequence.get(sequence.size() - 1);
    }

    /**
     * Get the previous number to a sequence. To do this we simply reverse the sequence and find the next number.
     * @param sequence the sequence to pre-extend
     * @return the value before the start of the sequence
     */
    private int getPreviousNumber(List<Integer> sequence) {
        ArrayList<Integer> numbers = new ArrayList<>(sequence);
        Collections.reverse(numbers);
        return getNextNumber(numbers);
    }

    private long part1(List<List<Integer>> fileContent ){
        return fileContent.stream().mapToLong(this::getNextNumber).sum();
    }

    private long part2(List<List<Integer>> fileContent ) {
        return fileContent.stream().mapToLong(this::getPreviousNumber).sum();
    }

    @Override
    public void run() {
        List<List<Integer>> fileContent =
                FileLoader.readFileAsIntegers("input/day9.txt", "\\s+");

        System.out.println("Part1: " + part1(fileContent));
        System.out.println("Part2: " + part2(fileContent));
    }
}
