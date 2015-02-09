package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.RuleContext;

/**
 * Parser rule which ensures the brackets are matched.
 * 
 * @author p.mankala
 *
 */
public abstract class BracketMatchingRule extends ParseRule {
    @Override
    public final ParseResult validate() {
        RuleContext ctx = getParseContext();

        if (checkValidity()) {
            return ParseResult.VALID;
        } else if (ctx.hasNextChar() && ctx.peekChar() == getRuleEndChar()) {
            ctx.consumeChar();
            return ParseResult.END;
        } else {
            return ParseResult.INVALID;
        }
    }

    /**
     * Check the content validity of this {@link ParseRule}
     * 
     * @return true/false
     */
    protected abstract boolean checkValidity();
}
