package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileLoader {
    /**
     * Read a text file into a string array
     * @param filename name of file
     * @return list of file lines
     */
    public static List<String> readFile(String filename) {
        List<String> fileContent = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while( (line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
        }
        catch( IOException ioe ) {
            ioe.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Read a text file of integers separated by a delimiter into an Integer array
     * @param filename name of file
     * @param delimiter character separating each number
     * @return list of integer arrays. Each array holds the numbers on that line
     */
    public static List<List<Integer>> readFileAsIntegers(String filename, String delimiter) {
        List<String> asStrings = readFile(filename);
        List<List<Integer>> asIntegers = new ArrayList<>();
        for( String line : asStrings ){
            String[] numbers = line.split(delimiter);
            List<Integer> intList = Arrays.stream(numbers).mapToInt(Integer::parseInt).boxed().toList();
            asIntegers.add(intList);
        }
        return asIntegers;
    }
}
