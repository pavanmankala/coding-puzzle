package org.coding.puzzle;

/**
 * A Result is a Wrapper for the value returned by the {@link Processor}
 * 
 * @author p.mankala
 *
 * @param <V>
 *            Type of result
 */
public interface Result<V> {
    V resultValue();

    /**
     * A boolean result.
     * 
     * @author p.mankala
     *
     */
    public static class BooleanResult implements Result<Boolean> {
        private final boolean result;

        public BooleanResult(boolean result) {
            this.result = result;
        }

        @Override
        public Boolean resultValue() {
            return result;
        }
    }
}
