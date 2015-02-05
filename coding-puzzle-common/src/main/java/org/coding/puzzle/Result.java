package org.coding.puzzle;

public interface Result<V> {
    V resultValue();

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
