package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.kohsuke.MetaInfServices;

/**
 * Parentheses parse rule
 * 
 * @author p.mankala
 *
 */
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
        // Parentheses can contain only Braces
        return character == BRACES.getRuleStartChar();
    }
}
