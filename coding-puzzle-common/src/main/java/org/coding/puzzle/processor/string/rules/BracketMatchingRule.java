package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.ParseRuleContext;

public abstract class BracketMatchingRule extends ParseRule {
    @Override
    public final boolean validate() {
        ParseRuleContext ctx = getParseContext();

        return checkValidity() || ctx.hasNextChar() && ctx.nextChar() == getRuleEndCondChar();
    }

    public abstract boolean checkValidity();
}
