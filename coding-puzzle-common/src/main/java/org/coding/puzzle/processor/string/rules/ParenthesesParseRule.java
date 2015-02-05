package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRuleContext;

public class ParenthesesParseRule extends BracketMatchingRule {
    private static BracesRule BRACES = new BracesRule();

    @Override
    public char getRuleStartCondChar() {
        return '(';
    }

    @Override
    public char getRuleEndCondChar() {
        return ')';
    }

    @Override
    public boolean checkValidity() {
        ParseRuleContext ctx = getParseContext();
        char nextChar = ctx.peekChar();
        return nextChar == BRACES.getRuleStartCondChar();
    }
}
