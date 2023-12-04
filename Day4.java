import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {
    private record Card(String id, List<Integer> winning, List<Integer> present) {
        public int matching() {
            return (int)present.stream().filter(winning::contains).count();
        }

        public int score() {
            // Count how many numbers are in both the winning and present lists.
            int count = matching();
            if( count == 0 ) return 0;
            return 1 << (count - 1);
        }
    }

    /**
     * Read a text file into a String array
     *
     * @return list of Strings holding each line of the file
     */
    private ArrayList<String> readFile() {
        ArrayList<String> fileContent = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input/day4.txt"));
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
     * Convert an input line into a Card
     * @param line input line
     * @return Card
     */
    private Card parseLine(String line) {
        // parts[0] = card ID, parts[1] = all numbers
        String[] parts = line.split(":");
        String id = parts[0];
        // parts[0] = winning numbers  parts[1] = card numbers
        parts = parts[1].split("\\|");
        // convert the two sets of whitespace separated numbers into lists using streams
        Pattern pattern = Pattern.compile("\\s+");
        List<Integer> winning =
                pattern.splitAsStream(parts[0].trim()).map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> present =
                pattern.splitAsStream(parts[1].trim()).map(Integer::valueOf).collect(Collectors.toList());
        return new Card( id, winning, present );
    }

    /**
     * increase a list element by a set amount. If the element does not exist, create it.
     * @param list list of Integers
     * @param index index of integer to increase
     * @param amount amount to increase the value by
     */
    private void increaseBy(List<Integer> list, int index, int amount) {
        if( index >= list.size() ){
            list.add(index, amount);
        } else {
            list.set(index, list.get(index) + amount);
        }
    }

    /**
     * I'm not going to even try and explain how this works ... It basically counts a pile
     * of cards that is created by some ridiculous rules in AOC-23
     * @param cards A list of all the cards
     * @return the number of cards in the created pile.
     */
    private int countPile(List<Card> cards) {
        List<Integer> pile = new ArrayList<>();

        int cardId = 0;
        for( Card current : cards ) {
            // The current card is the original
            increaseBy(pile, cardId, 1);
            // Find how many numbers match
            int count = current.matching();
            // For the number of cards that match, following the current card, add the number
            // of cards in the current cards pile.
            for (int idx = 0; idx < count; idx++) {
                increaseBy(pile, cardId + idx + 1, pile.get(cardId));
            }
            cardId++;
        }
        // Add all the pile counts together
        return pile.stream().reduce(0, Integer::sum);
    }

    public void run() {
        ArrayList<String> fileContent = readFile();
        // Convert each line of input into a Card record
        List<Card> cards = fileContent.stream().map(this::parseLine).toList();
        // Sum the scores of all the cards
        int part1 = cards.stream().mapToInt(Card::score).sum();

        System.out.println("Part 1:" + part1);
        System.out.println("Part 2: " + countPile(cards));
    }

}