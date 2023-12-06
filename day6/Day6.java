package day6;

import util.Day;
import util.FileLoader;

import java.util.Arrays;
import java.util.List;

public class Day6 implements Day {

    /**
     * Run a race
     * @param time      Race time
     * @param distance  current best distance
     * @return the number of ways there are of beating the best distance
     */
    long runRace(long time, long distance) {
        long count = 0;
        /**
         * We only need to test half of the values since they will be mirrored past the halfway
         * point.
         */
        for( int idx = 1; idx < time/2; idx++ ){
            long test = idx * (time-idx);
            if( test > distance ){
                // Add 2 because we're only testing half the numbers
                count += 2;
            }
        }
        /**
         * The value in the centre will always be the maximum possible value so we make a huge
         * assumption here that it's better than the distance value. It the time was even then there
         * will be one max value. If the time was odd, there will be 2.
         */
        if( time % 2 == 0 ){
            count++;
        } else {
            count += 2;
        }
        return count;
    }

    long part1(List<String> fileContent){
        String line = fileContent.get(0).split(":")[1].trim();
        int[] times = Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();
        line = fileContent.get(1).split(":")[1].trim();
        int[] distances = Arrays.stream(line.split("\\s+")).mapToInt(Integer::parseInt).toArray();

        long total = 1;
        for( int race = 0; race < times.length; race++ ){
            int time = times[race];
            int distance = distances[race];
            long count = runRace(time, distance);
            total *= count;
        }
        return total;
    }

    long part2(List<String> fileContent){
        String line = fileContent.get(0).split(":")[1].trim().replaceAll("\\s", "");
        long time = Long.parseLong(line);
        line = fileContent.get(1).split(":")[1].trim().replaceAll("\\s", "");
        long distance = Long.parseLong(line);
        return runRace(time, distance);
    }

    @Override
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day6.txt");
        System.out.println("Day 6");
        System.out.println("Part 1: " + part1(fileContent));
        System.out.println("Part 2: " + part2(fileContent));
    }
}
