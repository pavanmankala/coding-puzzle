package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.kohsuke.MetaInfServices;

@MetaInfServices(value = ParseRule.class)
public class BracesParseRule extends BracketValidationRule {
    private static SquareBracketParseRule SQ_BRACKETS = new SquareBracketParseRule();

    @Override
    public char getRuleStartChar() {
        return '{';
    }

    @Override
    public char getRuleEndChar() {
        return '}';
    }

    @Override
    protected boolean isValidChar(char character) {
        return character == SQ_BRACKETS.getRuleStartChar();
    }
}
