package org.coding.puzzle;

public interface Processor<I, V, R extends Result<V>> {
    R process(I input);
}
