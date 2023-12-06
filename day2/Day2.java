package day2;

import util.Day;
import util.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

public class Day2 implements Day {

    /**
     * Record to hold the data of a single game part
     * @param id Game ID
     * @param red number of red cubes
     * @param green number of green cubes
     * @param blue number of blue cubes
     */
    private record Game(int id, int red, int green, int blue){}

    /**
     * Take an array of game definitions and sum the id's of the valid games. For a game to be
     * valid, it's cube usage cannot exceed the maximum number provided in any one part.
     * @param gameDefs String array holding the game definitions, one per line
     * @param maxRed maximum number of red cubes that can be used
     * @param maxGreen maximum number of green cubes that can be used
     * @param maxBlue maximum number of blue cubes that can be used
     * @return the sum of the ID's of the valid games given a maximum for each cube colour.
     */
    private int sumValidGameIds(List<String> gameDefs, int maxRed, int maxGreen, int maxBlue) {
        int sum = 0;
        for( String gameDef : gameDefs ){
            // each definition starts with 'Game <id>:'
            String[] gameDesc = gameDef.split(":");
            // gameDesc[0] is the game ID, [1] is the game definition.
            int id = extractInteger(gameDesc[0].trim());
            ArrayList<Game> games = processGameDefinition(id, gameDesc[1].trim());

            // Ignore game if any counts exceed the max allowed
            boolean valid = true;
            for( Game check : games ){
                if( check.red() > maxRed || check.green() > maxGreen || check.blue() > maxBlue ) {
                    valid = false;
                    break;
                }
            }
            if( valid ) sum += games.get(0).id();
        }
        return sum;
    }

    /**
     * Take an array of game definitions and sum the powers of each game. The power is determined
     * by how many cubes of each colour will be required to make the game possible and multiplying
     * the counts together.
     * @param gameDefs String array holding the game definitions, one per line
     * @return the sum of the powers for each game.
     */
    private int sumPowers(List<String> gameDefs ){
        int sum = 0;

        for( String gameDef : gameDefs ){
            int minRed = 0, minGreen = 0, minBlue = 0;
            // each definition starts with 'Game <id>:'
            String[] gameDesc = gameDef.split(":");
            // gameDesc[0] is the game ID, [1] is the game definition.
            int id = extractInteger(gameDesc[0].trim());
            ArrayList<Game> games = processGameDefinition(id, gameDesc[1].trim());
            // Update the cubes required to play this game for each game part
            for( Game game : games ){
                minRed = max(minRed, game.red());
                minGreen = max(minGreen, game.green());
                minBlue = max(minBlue, game.blue());
            }
            // Calculate the power and sum add it to the sum.
            sum += minBlue * minGreen * minRed;
        }
        return sum;
    }

    /**
     * Take the definition for a game and build the record for each game part.
     * Game parts are separated by semicolons.
     * @param id The game ID
     * @param gameDef The string representation of a game
     * @return List of records that represent the game.
     */
    private ArrayList<Game> processGameDefinition(int id, String gameDef ) {
        ArrayList<Game> game = new ArrayList<>();
        String[] parts = gameDef.split(";");
        for( String part : parts ){
            game.add(processGamePart(id, part.trim()));
        }
        return game;
    }

    /**
     * Create a game record by extracting the counts for red, green and blue for a game
     * @param id game ID
     * @param part String definition of the game part
     * @return record of the game part
     */
    private Game processGamePart(int id, String part) {
        // Colour counts are separated by commas
        String[] colours = part.split(",");
        int red = 0, green = 0, blue = 0;
        for( String colour : colours ){
            String colourName =
                    colour.substring(colour.lastIndexOf(" ") + 1).toLowerCase();
            switch( colourName ) {
                case "red":
                    red = extractInteger(colour);
                    break;
                case "green":
                    green = extractInteger(colour);
                    break;
                case "blue":
                    blue = extractInteger(colour);
                    break;
            }
        }
        return new Game(id, red, green, blue);
    }

    /**
     * Extract the first integer from a string and return its value.
     * @param textID text which includes an integer number
     * @return the value of the first integer number found or 0 if there wasn't one.
     */
    private int extractInteger(String textID) {
        // Extract the game number
        int id = 0;
        Matcher matcher = Pattern.compile("\\d+").matcher(textID);
        if(matcher.find()) {
            id = Integer.parseInt(matcher.group());
        }
        return id;
    }

    public void run() {
        List<String> gameDefs = FileLoader.readFile("input/day2.txt");
        System.out.println("Part 1:" + sumValidGameIds(gameDefs, 12, 13, 14));
        System.out.println("Part 2:" + sumPowers(gameDefs));
    }

}
