package day10;

import util.Day;
import util.FileLoader;

import java.util.*;

/**
 * Define a pipe with two entrances (& exits) represented by the map characters |-F7JL. Directions can be N, S, E or W
 * Each pipe has two directions. | -> N,S  F -> E,S  L -> E,N, etc
 * @param first a pipe direction of entry or exit
 * @param second the other direction of entry or exit
 */
record Pipe( char first, char second ) {

    /**
     * Given a pipe entry direction, get the pipe direction of the exit ooint. An entry direction of East means the
     * pipe is being entered at the East so the direction of travel is actually West.
     * @param entryPoint direction of pipe entry point
     * @return direction of pipe exit point
     */
    public char getExitDirection(char entryPoint) {
        return (entryPoint == first) ? second : first;
    }

    /**
     * Checks if a pipe has an exit point in a given pipe direction
     * @param direction direction of exit
     * @return true if pipe has an exit in the given pipe direction
     */
    public boolean hasExitDirection(char direction) {
        return ((direction == first) || (direction == second));
    }
}

/**
 * Define a polygon vertex
 */
record Vertex(int row, int col) {};

/**
 * Define a Tile. A Tile represents a row column and pipe symbol
 */
class Tile {
    private final char symbol;
    private final int row;
    private final int col;
    private int count;

    public Tile(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.count = 0;
    }

    /**
     * Check if this tile is inside the route polygon
     * @param polygon each coordinate that is part of the route from Start back to Start.
     * @return true if this tile is inside the polygon
     */
    public boolean isInside(List<Vertex> polygon) {
        // draw a line in one direction and count the number of times it crosses the polygon.
        // if the number of crossings is odd, the point is inside the polygon.
        // Uses the even-odd rule algorithm or crossing number algorithm
        boolean inside = false;
        int head = 0;
        int tail = polygon.size() - 1;
        while( head < polygon.size() ) {
            Vertex curr = polygon.get(head);
            Vertex prev = polygon.get(tail);
            if( ((curr.row() > row) != (prev.row() > row)) &&
                    (col < (prev.col() - curr.col()) * (row - curr.row()) / (prev.row() - curr.row()) + curr.col())){
                inside = !inside;
            }
            tail = head++;
        }
        return inside;
    }


    /***********************************************************************************************************
     * Getters and setters
     **********************************************************************************************************/

    public char getSymbol() {
        return symbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCount() {
        return count;
    }

    public void setCount( int count ){
        this.count = count;
    }
    public int incCount() {
        this.count++;
        return count;
    }

    // Code for testing equality

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return symbol == tile.symbol && row == tile.row && col == tile.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, row, col);
    }

    @Override
    public String toString() {
        return "[ row:" + row + " col:" + col + " symbol:" + symbol + " count:" + count + "]";
    }
}

/**
 * Manage a grid of tiles
 */
class TileGrid {
    private final Tile[][] tiles;
    private final int rows;
    private final int cols;

    public TileGrid(int rows, int columns ){
        this.rows = rows;
        this.cols = columns;

        tiles = new Tile[rows][];
        for(int row=0; row<rows; row++) {
            tiles[row] = new Tile[columns];
        }
    }

    /**
     * Create grid from data file
     * @param fileContent file content as strings
     */
    public void createFromFile(List<String> fileContent ){
        int row = 0;
        for( String line : fileContent ){
            if(line.trim().isEmpty()) continue;
            int col = 0;
            for(char c : line.toCharArray()) {
                setTile(row, col, new Tile( row, col, c));
                col++;
            }
            row++;
        }
    }

    /**
     * Find the start tile
     * @return the start Tile
     */
    public Tile findStart() {
        // Stream[Stream<Tile[]> -> flatten to Stream<Tile> -> filter start tile -> find it
        return Arrays.stream(tiles)
            .flatMap(Arrays::stream)
                .filter(tile -> tile.getSymbol() == 'S')
                .findAny().orElse(null);
    }

    /**
     * Get a neighbouring cell from a given location and direction
     * @param row grid row
     * @param col grid column
     * @param direction direction of movement
     * @return the Tile in that direction or null
     */
    public Tile getNeighbour(int row, int col, char direction) {
        Tile neighbour = null;
        switch (direction) {
            case 'N' ->  neighbour = getTile(row-1, col);
            case 'E' ->  neighbour = getTile(row, col+1);
            case 'S' ->  neighbour = getTile(row+1, col);
            case 'W' ->  neighbour = getTile(row, col-1);
            default ->  neighbour = null;
        };
        return neighbour;
    }

    /************************************************************************************************************
     * Getters and setters
     ***********************************************************************************************************/

    public void setTile(int row, int col, Tile tile ){
        tiles[row][col] = tile;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}

public class Day10 implements Day {
    private TileGrid tileGrid;
    private final List<Vertex> polygon;
    private final Map<Character, Pipe> pipes;

    public Day10() {
        polygon = new ArrayList<>();

        pipes = new HashMap<>();
        pipes.put('|', new Pipe('N','S'));
        pipes.put('-', new Pipe('E','W'));
        pipes.put('7', new Pipe('W','S'));
        pipes.put('L', new Pipe('N','E'));
        pipes.put('F', new Pipe('S','E'));
        pipes.put('J', new Pipe('W','N'));
    }

    /**
     * Follow the pipeline
     * @param tile              current tile
     * @param entryDirection    direction of travel
     * @param start             the pipeline start
     * @return half the number of pipe sections in the pipeline
     */
    private int followPipe(Tile tile, char entryDirection, Tile start) {
        int count = 0;
        Tile next;
        do {
            if( tile == null || tile.getSymbol() == '.') break;
            char exitDirection = pipes.get(tile.getSymbol()).getExitDirection(entryDirection);
            next = tileGrid.getNeighbour(tile.getRow(), tile.getCol(), exitDirection);
            if(next==null) break;
            if(next == start) {
                count = tile.incCount() / 2;
                break;
            }
            next.setCount(tile.incCount());
            entryDirection = getReverseDirection(exitDirection);
            tile=next;
            polygon.add(new Vertex(tile.getRow(),tile.getCol()));
        } while(true);
        return count;
    }

    /**
     * reverse a given direction
     * @param direction the direction
     * @return the opposite of the entry direction
     */
    private char getReverseDirection(char direction) {
        return switch (direction) {
            case 'N' -> 'S';
            case 'E' -> 'W';
            case 'S' -> 'N';
            case 'W' -> 'E';
            default -> 0;
        };
    }

    private int part1() {
        Tile start = tileGrid.findStart();
        polygon.add(new Vertex(start.getRow(),start.getCol()));

        // Find a valid direction to move
        char [] exits = {'N','E','S','W'};
        for(char exit : exits) {
            Tile tile = tileGrid.getNeighbour(start.getRow(), start.getCol(), exit);
            if(tile==null || tile.getSymbol()=='.' || tile.getCount() != 0) continue;
            char reverse = getReverseDirection(exit);
            if(pipes.get(tile.getSymbol()).hasExitDirection(reverse)) {
                tile.incCount();
                polygon.add(new Vertex(tile.getRow(),tile.getCol()));
                return followPipe(tile, reverse, start);
            }
        }
        return 0;
    }

    private int part2() {
        List<Tile> toTest = new ArrayList<>();

        Tile start = tileGrid.findStart();
        for(int row = 0; row< tileGrid.getRows(); ++row) {
            for(int col = 0; col< tileGrid.getCols(); ++col) {
                Tile tile = tileGrid.getTile(col,row);
                if(tile.getCount()==0 && !tile.equals(start)) {
                    toTest.add(tile);
                }
            }
        }

        int count=0;
        for(Tile tile : toTest) {
            if( tile.isInside(polygon)) {
                count++;
            }
        }
        return count;
    }

    public void run() {
        List<String>fileContent = (FileLoader.readFile("input/day10.txt"));
        tileGrid = new TileGrid(fileContent.size(), fileContent.get(0).length());
        tileGrid.createFromFile(fileContent);

        System.out.println( "Part 1: " + part1());
        System.out.println( "Part 2: " + part2());
    }
}
