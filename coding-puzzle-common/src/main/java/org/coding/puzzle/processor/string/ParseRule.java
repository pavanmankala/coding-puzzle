package org.coding.puzzle.processor.string;

/**
 * This is the base class for various parse rules in the code-puzzle. All the
 * parse rules are derived from this class.
 * 
 * <p>
 * 
 * A Parse rule chews {@link RuleContext} and determines if the context is
 * conforming to the current rule. It is upto the whim and fancies of the
 * ParseRules to access and {@link RuleContext}.
 * 
 * @author p.mankala
 *
 */
public abstract class ParseRule {
    public static enum ParseResult {
        VALID, INVALID, END
    }

    /**
     * {@link RuleContext} for this rule
     */
    private RuleContext parseContext;

    /**
     * Sets the {@link RuleContext} for this rule.
     * 
     * @param parseContext
     *            {@link RuleContext} for this rule
     */
    public final void setParseContext(RuleContext parseContext) {
        this.parseContext = parseContext;
    }

    /**
     * Gets the {@link RuleContext} of this rule. Child classes can access via
     * this method
     * 
     * @return {@link RuleContext} of this rule
     */
    protected final RuleContext getParseContext() {
        return parseContext;
    }

    /**
     * Validates this rule and returns the {@link ParseResult}, which indicated
     * the conformance
     * 
     * @return the {@link ParseResult} after validation
     */
    public abstract ParseResult validate();

    /**
     * The ParseRule begin character.
     * <p>
     * If a ParseRule, while chewing the {@link RuleContext}, senses this
     * character, should hand-over the rule processing to this {@link ParseRule}.
     * 
     * @return the begin character for this rule
     */
    public abstract char getRuleStartChar();

    /**
     * The ParseRule end character.
     * <p>
     * If this ParseRule, while chewing the {@link RuleContext}, senses this
     * character, should end it's processing and give the control to the
     * initiator of this {@link ParseRule}.
     * 
     * @return the begin character for this rule
     */
    public abstract char getRuleEndChar();
}
