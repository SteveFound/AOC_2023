package day11;

import util.Day;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

record Galaxy(long row, long col) {};

class GalaxyGrid {
    private List<Galaxy> galaxies;

    /**
     * Build the galaxy grid
     * @param fileContent the grid as a file
     * @param multiplier multiplier for each empty row or column
     */
    public GalaxyGrid(List<String> fileContent, int multiplier){
        List<Integer> emptyColumns = new ArrayList<>();
        List<Integer> emptyRows = new ArrayList<>();

        // scan the input looking for empty columns
        for( int col = 0; col < fileContent.get(0).length(); col++ ){
            boolean isEmpty = true;
            for( String line : fileContent ){
                if( line.charAt(col) == '#'){
                    isEmpty = false;
                    break;
                }
            }
            if( isEmpty ) emptyColumns.add(col);
        }

        // scan for empty rows and create the galaxy list
        galaxies = new ArrayList<>();
        for( int row = 0; row < fileContent.size(); row++ ){
            // If the row is empty register it
            if(!fileContent.get(row).contains("#")) {
                emptyRows.add(row);
                continue;
            }

            long rc = 0;
            for (int r : emptyRows) {
                if (r < row) {
                    rc += multiplier-1;
                }
            }

            // The row contains galaxies so register them
            char[] chars = fileContent.get(row).toCharArray();
            for( int col = 0; col < chars.length; col++) {
                if (chars[col] == '#') {
                    long cc = 0;
                    for (int c : emptyColumns) {
                        if (c < col) {
                            cc += multiplier-1;
                        }
                    }
                    galaxies.add(new Galaxy(row + rc, col + cc));
                }
            }
        }
    }

    /**
     * Calculate the manhatten distance between two galaxies.
     * @param from first galaxy
     * @param to second galaxy
     * @return manhatten distance
     */
    public long manhattenDistance(Galaxy from, Galaxy to ) {
        return abs(from.row() - to.row()) + abs(from.col() - to.col());
    }

    /**
     * Sum the distances between every galaxy and every other galaxy
     * @return sum of all distances
     */
    public long visitAllGalaxies() {
        long distance = 0;
        for( int galaxy = 0; galaxy < galaxies.size()-1; galaxy++ ){
            Galaxy from = galaxies.get(galaxy);
            for( int visit = galaxy+1; visit < galaxies.size(); visit++ ){
                Galaxy to = galaxies.get(visit);
                distance += manhattenDistance( from, to );
            }
        }
        return distance;
    }

    @Override
    public String toString() {
        return galaxies.toString();
    }
}

public class Day11 implements Day {
    public long part1(List<String> fileContent){
        GalaxyGrid galaxyGrid = new GalaxyGrid(fileContent, 2);
        return galaxyGrid.visitAllGalaxies();
    }

    public long part2(List<String> fileContent) {
        GalaxyGrid galaxyGrid = new GalaxyGrid(fileContent, 1000000);
        return galaxyGrid.visitAllGalaxies();
    }

    @Override
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day11.txt");
        System.out.println("Part 1: " + part1(fileContent)) ;
        System.out.println("Part 2: " + part2(fileContent)) ;
    }
}
