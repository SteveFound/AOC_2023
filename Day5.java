import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day5 {
    /**
     * Create a Mapper to map a source value to a destination value and vice versa
     * @param source  Sourcr value
     * @param dest    Destination value
     * @param range   Range which is the same for both source and dest
     */
    record Mapper(long source, long dest, long range) {
        /**
         * Checks if a value is a valid source value
         * @param value value to test
         * @return true if value is a valid source value
         */
        private boolean inSourceRange(long value) {
            return value >= source && value < source + range;
        }

        /**
         * Checks if a value is a valid destination value
         * @param value value to test
         * @return true if value is a valid destination value
         */
        private boolean inDestRange(long value) {
            return value >= dest && value < dest + range;
        }

        /**
         * map a source value to it's destination
         * @param value source value
         * @return destination value
         */
        public long mapToDest(long value) {
            long diff = value - source;
            return inSourceRange(value) ? dest + diff : value ;
        }

        /**
         * Map a destination value to it's source value
         * @param value destination value
         * @return source value
         */
        public long mapToSource(long value) {
            long diff = value - dest;
            return inDestRange(value) ? source + diff : value;
        }
    }

    record SeedRange(long source, long range) {
        /**
         * Checks if a value is part of this seed range
         * @param value value to check
         * @return true if value is in range
         */
        public boolean inRange(long value) {
            return value >= source && value < source + range;
        }
    }

    class Mapping {
        List<Mapper> mappers = new ArrayList<>();

        public void addMapper(Mapper mapper) {
            mappers.add(mapper);
        }

        /**
         * Map a source value to it's destination via the mappers
         * @param value value to map
         * @return mapped destination value
         */
        public long mapToDest(long value) {
            long mapped = value;
            for (Mapper mapper : mappers) {
                mapped = mapper.mapToDest(value);
                if( mapped != value ) return mapped;
            }
            return value;
        }

        /**
         * Map a destination value to it's source via the mappers
         * @param value the value to map
         * @return mapped source value
         */
        public long mapToSource(long value) {
            long mapped = value;
            for (Mapper mapper : mappers) {
                mapped = mapper.mapToSource(value);
                if( mapped != value ) return mapped;
            }
            return value;
        }

        /**
         * Find the mapper with the highest destination range and return the maximum destination value
         * @return maximum destination value handled by all mappers in this mapping
         */
        public long getMax() {
            Mapper mapper = mappers.stream().max(Comparator.comparing(x -> x.dest() + x.range())).get();
            return mapper.dest() + mapper.range();
        }

        @Override
        public String toString() {
            return mappers.toString();
        }
    }

    /**
     * read a file into an array of strings
     * @return list of strings
     */
    private ArrayList<String> readFile() {
        ArrayList<String> fileContent = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input/day5.txt"));
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

    private List<Mapping> createMappings(List<String> fileInput) {
        List<Mapping> mappings = new ArrayList<>();
        String line;
        Mapping currMapping = new Mapping();
        mappings.add(currMapping);
        // This bit of code is awful.
        for (int lineIndex = 3; lineIndex < fileInput.size(); lineIndex++) {
            line = fileInput.get(lineIndex);
            if (line.isEmpty()) {
                // don't care about the mapping title
                lineIndex++;
                currMapping = new Mapping();
                mappings.add(currMapping);
            } else {
                String[] values = line.split("\\s+");
                currMapping.addMapper(new Mapper(
                        Long.parseLong(values[1]), Long.parseLong(values[0]), Long.parseLong(values[2])
                ));
            }
        }
        return mappings;
    }

    private long part1() {
        List<String> lines = readFile();
        String seedLine = lines.get(0).split(":")[1].trim();
        long[] seeds = Arrays.stream(seedLine.split("\s+")).mapToLong(Long::parseLong).toArray();
        List<Mapping> maps = createMappings(lines);

        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            long next = seed;
            for (int i = 0; i < maps.size(); i++) {
                next = maps.get(i).mapToDest(next);
            }
            if (next < min) min = next;
        }
        return min;
    }

    private long part2() {
        List<String> lines = readFile();
        String seedLine = lines.get(0).split(":")[1].trim();
        long[] seeds = Arrays.stream(seedLine.split("\s+")).mapToLong(Long::parseLong).toArray();
        List<SeedRange> seedList = new ArrayList<>();
        for (int i = 0; i + 1 < seeds.length; i += 2) {
            seedList.add(new SeedRange(seeds[i], seeds[i + 1]));
        }
        List<Mapping> maps = createMappings(lines);

        // Get the maximum location value
        long max = maps.get(maps.size() - 1).getMax();
        for (int location = 0; location < max; location++) {
            long mappedLocation = location;
            // Map the location back to it's seed number
            for (int j = maps.size() - 1; j >= 0; j--) {
                mappedLocation = maps.get(j).mapToSource(mappedLocation);
            }
            // If that seed number is in range for any seeds... bingo.
            for (SeedRange seed : seedList) {
                if (seed.inRange(mappedLocation)) return location;
            }
        }
        // Something has gone badly wrong !!!
        return -1;
    }

    public void run() {
        System.out.println(part1());
        System.out.println(part2());
    }
}