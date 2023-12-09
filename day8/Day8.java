package day8;

import util.Day;
import util.FileLoader;
import util.Mathematics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day8 implements Day {

    private record Connection(String left, String right ){}

    private Map<String, Connection> parseFileContent( List<String> fileContent ){
        // TreeMap
        Map<String, Connection> connectionMap = new TreeMap<>();

        // AAA = (BBB, CCC)
        Pattern pattern = Pattern.compile("^([A-Z]+)\\s+=\\s+\\(([A-Z]+),\\s+([A-Z]+)\\)");
        Matcher matcher;
        for( String line : fileContent ) {
            matcher = pattern.matcher(line);
            while(matcher.find()){
                connectionMap.put(matcher.group(1), new Connection(matcher.group(2), matcher.group(3)));
            }
        }
        return connectionMap;
    }

    int followRoute(String start, char[] directions, Map<String, Connection> routes){
        String location = start;
        int directionIdx = 0;
        int steps = 0;
        while(!location.endsWith("Z")) {
            char turn = directions[directionIdx];
            directionIdx++;
            if( directionIdx >= directions.length ) directionIdx = 0;
            Connection connection = routes.get(location);
            if( turn == 'L') {
                location = connection.left;
            } else {
                location = connection.right;
            }
            steps++;
        }

        return steps;
    }

    int part1(List<String> fileContent){
        char[] directions = fileContent.get(0).toCharArray();
        Map<String, Connection> routes = parseFileContent(fileContent);
        return followRoute("AAA", directions, routes);
    }

    long part2(List<String> fileContent) {
        char[] directions = fileContent.get(0).toCharArray();
        Map<String, Connection> routes = parseFileContent(fileContent);

        List<Long> steps = new ArrayList<>();
        for( String location : routes.keySet()){
            if( location.endsWith("A")){
                steps.add((long)followRoute(location, directions, routes));
            }
        }
        return Mathematics.lcm(steps);
    }


    @Override
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day8.txt");
        System.out.println("Day 8");
        System.out.println("Part 1: " + part1(fileContent));
        System.out.println("Part 2: " + part2(fileContent));
    }
}
