package de.eldoria.worldguardbatch.util;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class IntRange implements Iterable<Integer> {
    private int min;
    private int max;

    /**
     * Creates a new Int Range object.
     *
     * @param min smallest value
     * @param max largest value
     */
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Get the min value.
     *
     * @return integer
     */
    public int getMin() {
        return min;
    }

    /**
     * Get the max value.
     *
     * @return integer
     */
    public int getMax() {
        return max;
    }

    /**
     * Parse two string values and returns a Int range instance.
     *
     * @param boundMin lower bound
     * @param boundMax upper bound
     * @return new IntRange instance.
     * @throws NumberFormatException when input is not a int.
     */
    public static IntRange parseString(String boundMin, String boundMax) throws NumberFormatException {
        int max;
        int min = 0;

        try {
            max = Integer.parseInt(boundMin);

        } catch (NumberFormatException e) {
            throw e;
        }
        if (boundMax != null) {
            min = max;
            try {
                max = Integer.parseInt(boundMax);

            } catch (NumberFormatException e) {
                throw e;
            }
        }
        return new IntRange(min, max);
    }

    /**
     * Iterator to iterate over range.
     *
     * @return iterator
     */
    @Nonnull
    public Iterator<Integer> iterator() {

        return new IntIterator(min, max);
    }

    private class IntIterator implements Iterator<Integer> {

        private int min;
        private int max;

        IntIterator(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean hasNext() {

            return min < max;
        }

        @Override
        public Integer next() {
            return min++;
        }
    }
}
