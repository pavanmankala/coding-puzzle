package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.kohsuke.MetaInfServices;

@MetaInfServices(value = ParseRule.class)
public class ParenthesesParseRule extends BracketValidationRule {
    private static BracesParseRule BRACES = new BracesParseRule();

    @Override
    public char getRuleStartChar() {
        return '(';
    }

    @Override
    public char getRuleEndChar() {
        return ')';
    }

    @Override
    protected boolean isValidChar(char character) {
        return character == BRACES.getRuleStartChar();
    }
}
