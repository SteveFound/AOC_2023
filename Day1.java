import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1 {
    /**
     * Read a text file into a String array
     *
     * @return list of Strings holding each line of the file
     */
    private ArrayList<String> readFile() {
        ArrayList<String> fileContent = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input/day1.txt"));
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
     * Find the first and last digits in a line and return them as a 2 digit integer.
     * @param line string containing the digit(s)
     * @return a 2 digit integer.
     */
    private int processDigits(String line){
        int first = 0, last = 0;

        // Find the first digit
        Matcher matcher = Pattern.compile("\\d").matcher(line);
        if ( matcher.find() ) {
            first = Integer.parseInt(matcher.group());
            last = first;
        }

        // Find the last digit
        matcher = Pattern.compile("(\\d)(?!.*\\d)").matcher(line);
        if(matcher.find()) {
            last = Integer.parseInt(matcher.group());
        }

        return (first * 10) + last;
    }

    /**
     * Looks for the first and last word representations of digits or digits and returns them as a 2
     * digit number. The problem with the method I have used is that each line has to be checked 40 times.
     * 20 times for the first digit and 20 times for the last digit... which is pretty awful.
     *
     * @param line the line which may contain the digits
     * @return a 2 digit number as an integer
     */
    private int processTextAndDigits(String line) {
        Map<String, Integer> digits = Stream.of(new Object[][] {
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
        ArrayList<String> fileContent = readFile();
        int sum = 0;
        int sum2 = 0;
        for( String line : fileContent ){
            sum += processDigits(line);
            sum2 += processTextAndDigits(line);
        }
        System.out.println("Digits Sum : " + sum);
        System.out.println("Text and Digits sum : " + sum2);
    }
}
