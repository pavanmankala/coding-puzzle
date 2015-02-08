package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRuleContext;

public abstract class BracketValidationRule extends BracketMatchingRule {
    @Override
    public final boolean checkValidity() {
        ParseRuleContext ctx = getParseContext();
        char nextChar = ctx.peekChar();

        if (isValidChar(nextChar)) {
            ctx.consumeChar();
            ctx.setNewRule(true);
            return true;
        } else {
            ctx.setNewRule(false);
            return false;
        }
    }

    protected abstract boolean isValidChar(char character);
}
