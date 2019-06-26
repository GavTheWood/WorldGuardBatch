package de.eldoria.worldguardbatch.util;

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

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /**
     * Parse two string values and returns a Int range instance.
     *
     * @param boundMin lower bound
     * @param boundMax upper bound
     * @return new IntRange instance.
     */
    public static IntRange parseString(String boundMin, String boundMax) {
        int max;
        int min = 0;

        try {
            max = Integer.parseInt(boundMin);

        } catch (NumberFormatException e) {
            max = 0;
        }
        if (boundMax != null) {
            min = max;
            try {
                max = Integer.parseInt(boundMax);

            } catch (NumberFormatException e) {
                max = 0;
            }
        }
        return new IntRange(min, max);
    }

    @Override
    public Iterator<Integer> iterator() {

        return new IntIterator(min, max);
    }

    private class IntIterator implements Iterator<Integer> {

        private int min;
        private int max;

        public IntIterator(int min, int max) {
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
