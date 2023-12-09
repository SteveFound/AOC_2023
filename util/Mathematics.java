package util;

import java.util.List;
import java.util.Map;

public class Mathematics {
    /**
     * Recursively find the greatest common divisor of two numbers
     * @param first first number
     * @param second second number
     * @return biggest number that divides into both numbers
     */
    public static long gcd(long first, long second) {
        if (first == 0 || second == 0) return first + second;

        long high = Math.max(Math.abs(first), Math.abs(second));
        long low = Math.min(Math.abs(first), Math.abs(second));
        return gcd(high % low, low);
    }

    /**
     * find the lowest common multiple of two numbers
     * @param first first number
     * @param second second number
     * @return smallest number that both numbers divide into exactly
     */
    public static long lcm(long first, long second) {
        return (first * second) / gcd(first, second);
    }

    /**
     * find the lowest common multiple for all numbers in a list
     * @param numbers number list
     * @return the lowest number that all numbers in a list divide into exactly
     */
    public static long lcm(List<Long> numbers) {
        return numbers.stream().reduce(1L, Mathematics::lcm);
    }
}
