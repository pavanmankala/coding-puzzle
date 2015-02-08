package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.ParseRuleContext;

public abstract class BracketMatchingRule extends ParseRule {
    @Override
    public final ParseRuleState validate() {
        ParseRuleContext ctx = getParseContext();

        if (checkValidity()) {
            return ParseRuleState.VALID;
        } else if (ctx.hasNextChar() && ctx.peekChar() == getRuleEndChar()) {
            ctx.consumeChar();
            return ParseRuleState.CLOSED;
        } else {
            return ParseRuleState.INVALID;
        }
    }

    public abstract boolean checkValidity();
}
