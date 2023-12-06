package day3;

import util.Day;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class Day3 implements Day {

    /**
     * Define a symbol
     * @param symbol the symbol character
     * @param row grid row
     * @param col grid column
     */
    private record Symbol( Character symbol, int row, int col ){

        /**
         * Checks if this symbol (which is always a single character) is adjacent to a block
         * the block is always 1 character high but can be multiple characters in length.
         * @param row grid row
         * @param col grid column of block start
         * @param length length of block
         * @return true if this symbol is adjacent to the block, false if not
         */
        public boolean touches( int row, int col, int length ){
            int end = col + length - 1;
            // Get row distance
            int rowDist = abs(this.row - row);
            // get column distance
            int colDist = 0;
            if( this.col < col ) colDist = col - this.col;
            if( this.col > end ) colDist = this.col - end;
            // if both distances are 0 or 1 then we must be touching
            return (( rowDist <= 1) && (colDist <= 1));
        }
    }

    /**
     * record to store a gear part. The part consists of a symbol and the value of the number
     * that the symbol first touched.
     * @param symbol the symbol
     * @param value the value of the number the symbol is touching
     */
    private record GearPart( Symbol symbol, int value) {}

    /**
     * Extract all the symbols from a string grid representation. Each line is a grid row and
     * each character is a grid column.
     * @param fileContent the grid representation
     * @return list of all symbols found in the grid with their coordinates
     */
    private List<Symbol> extractSymbols(List<String> fileContent) {
        List<Symbol> symbols = new ArrayList<>();
        int row = 0;
        char chr;
        for( String line : fileContent ){
            char[] lineChars = line.toCharArray();
            for( int col = 0; col < lineChars.length; col++ ){
                chr = lineChars[col];
                // A symbol is anything that isn't a '.' or a digit
                if( chr != '.' && !Character.isDigit(chr)){
                    symbols.add(new Symbol(chr, row, col));
                }
            }
            row++;
        }
        return symbols;
    }

    /**
     * Sum all the gears. A gear is defined as any two numbers that touch the same '*' symbol.
     * These two numbers are multiplied together to form the gear value and these are all summed.
     * @param fileContent the grid representation
     * @param symbols list of all symbols found in the grid with their coordinates
     * @return sum of all the gears
     */
    private int sumGears(List<String> fileContent, List<Symbol> symbols){
        // A GearPart is the first half of a possible gear number pair
        ArrayList<GearPart> gearParts = new ArrayList<>();
        int sum = 0;
        int row = 0;
        String nbr;
        int value;
        Matcher matcher;

        Pattern integerPattern = Pattern.compile("\\d+");
        for( String line : fileContent ){
            matcher = integerPattern.matcher(line);
            // Find the next number on this line
            while (matcher.find()) {
                // Number as text
                nbr = matcher.group();
                value = Integer.parseInt(nbr);
                int col = matcher.start();
                int length = nbr.length();

                // Is this number touched by any * symbols ?
                for( Symbol symbol : symbols){
                    if(( symbol.symbol() == '*') && ( symbol.touches(row, col, length))) {
                        GearPart gearPart = null;
                        // See if we have a record of this * touching another value
                        for( GearPart part : gearParts ){
                            if( part.symbol().equals(symbol) ){
                                gearPart = part;
                                break;
                            }
                        }
                        if( gearPart == null ) {
                            // This is the first number this * has touched so store it
                            gearParts.add(new GearPart(symbol, value));
                        } else {
                            // This * is touching another number so remove it from the store, get
                            // the gear value and add it to the sum
                            gearParts.remove(gearPart);
                            sum += value * gearPart.value();
                        }
                    }
                }
            }
            row++;
        }
        return sum;
    }

    /**
     * Sum all numbers that are touched by a symbol
     * @param fileContent the grid representation
     * @param symbols list of all symbols found in the grid with their coordinates
     * @return sum of all the numbers touched by symbols
     */
    private int sumSymbolledNumbers(List<String> fileContent, List<Symbol> symbols){
        int sum = 0;
        int row = 0;
        String nbr;
        Matcher matcher;

        Pattern integerPattern = Pattern.compile("\\d+");
        for( String line : fileContent ){
            matcher = integerPattern.matcher(line);
            while (matcher.find()) {
                nbr = matcher.group();
                int col = matcher.start();
                int length = nbr.length();
                for( Symbol symbol : symbols){
                    if( symbol.touches(row, col, length)) {
                        sum += Integer.parseInt(nbr);
                        break;
                    }
                }
            }
            row++;
        }
        return sum;
    }
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day3.txt");
        List<Symbol> symbols = extractSymbols(fileContent);
        System.out.println("Part 1: " + sumSymbolledNumbers(fileContent, symbols));
        System.out.println("Part 2: " + sumGears(fileContent, symbols));
    }
}
