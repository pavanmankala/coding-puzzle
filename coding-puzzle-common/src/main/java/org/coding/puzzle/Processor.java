package org.coding.puzzle;

/**
 * A processor processess an Input and returns a {@link Result}
 * 
 * @author p.mankala
 *
 * @param <I>
 *            Input type
 * @param <V>
 *            Result value type
 * @param <R>
 *            Result tye
 */
public interface Processor<I, V, R extends Result<V>> {
    /**
     * Process the <code>input</code> and return {@link Result}
     * 
     * @param input
     *            input for this processor
     * @return Returns the result from this processor
     */
    R process(I input);
}
