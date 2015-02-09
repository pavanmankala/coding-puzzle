package org.coding.puzzle.processor.string;

import org.coding.puzzle.Processor;

/**
 * This class acts as a mediator between the {@link Processor} and
 * {@link ParseRule}s.
 * <p>
 * The {@link Processor} creates this object and hands over this to the
 * beginRule, and loops until the context is completed
 * 
 * @author p.mankala
 *
 */
public class RuleContext {
    private final char[] context;
    private int index = 0;
    private boolean newRule;
    private char newRuleChar;

    /**
     * Create rule context with the input string
     * 
     * @param string
     */
    public RuleContext(char[] string) {
        context = string == null ? new char[0] : string;
    }

    /**
     * Returns the next char from this context and chews the character
     * 
     * @return next character
     */
    public char nextChar() {
        return context[index++];
    }

    /**
     * Return the next char from this context but do not chew the character
     * 
     * @return next character
     */
    public char peekChar() {
        return context[index];
    }

    /**
     * Chews the one character at which this context pointing to.
     */
    public void consumeChar() {
        ++index;
    }

    /**
     * Returns if this context has more chars left
     * 
     * @return true/false
     */
    public boolean hasNextChar() {
        return context.length > 0 && index >= 0 && index < context.length;
    }

    /**
     * Conveys to the handler of this context that a new rule should be begun
     * <p>
     * See also: {@link #setNewRuleChar(char)}
     * 
     * @param newRule
     *            should a new rule be started
     */
    public void setNewRule(boolean newRule) {
        this.newRule = newRule;
    }

    /**
     * Returns true if a new rule be started
     * 
     * @return true/false
     */
    public boolean isNewRule() {
        return newRule;
    }

    /**
     * Returns the new Rule begin char
     * 
     * @return new rule char
     */
    public char getNewRuleChar() {
        return newRuleChar;
    }

    /**
     * If {@link #setNewRule(boolean)} is called, mention the rule char
     * accordingly.
     * 
     * @param newRuleChar
     *            new rule character
     */
    public void setNewRuleChar(char newRuleChar) {
        this.newRuleChar = newRuleChar;
    }
}
