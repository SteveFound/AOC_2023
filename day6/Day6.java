package day6;

import util.Day;
import util.FileLoader;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 implements Day {

    long part1(List<String> fileContent){
        String line = fileContent.get(0).split(":")[1].trim();
        int[] times = Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();
        line = fileContent.get(1).split(":")[1].trim();
        int[] distances = Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        long total = 1;
        for( int race = 0; race < times.length; race++ ){
            int time = times[race];
            int distance = distances[race];
            int count = 0;
            for( int idx = 1; idx < time/2; idx++ ){
                int test = idx * (time-idx);
                if( test > distance ){
                    count += 2;
                }
            }
            if( time % 2 == 0 ){
                count++;
            } else {
                count += 2;
            }
            total *= count;
        }
        return total;
    }

    long part2(List<String> fileContent){
        String line = fileContent.get(0).split(":")[1].trim().replaceAll("\\s", "");
        long time = Long.parseLong(line);
        line = fileContent.get(1).split(":")[1].trim().replaceAll("\\s", "");
        long distance = Long.parseLong(line);

        long count = 0;
        for( int idx = 1; idx < time/2; idx++ ){
            long test = idx * (time-idx);
            if( test > distance ){
                count += 2;
            }
        }
        if( time % 2 == 0 ){
            count++;
        } else {
            count += 2;
        }
        return count;
    }

    @Override
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day6.txt");
        System.out.println("Day 6");
        System.out.println("Part 1: " + part1(fileContent));
        System.out.println("Part 2: " + part2(fileContent));
    }
}
