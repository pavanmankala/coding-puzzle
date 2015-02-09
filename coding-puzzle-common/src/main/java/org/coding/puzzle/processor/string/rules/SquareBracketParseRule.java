package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.kohsuke.MetaInfServices;

/**
 * Square brackets parse rule
 * 
 * @author p.mankala
 *
 */
@MetaInfServices(value = ParseRule.class)
public class SquareBracketParseRule extends BracketValidationRule {
    private static final ParseRule SQ_BRACKET = new SquareBracketParseRule(), BRACES = new BracesParseRule(),
            PARENTHESES = new ParenthesesParseRule();

    @Override
    public char getRuleStartChar() {
        return '[';
    }

    @Override
    public char getRuleEndChar() {
        return ']';
    }

    @Override
    protected boolean isValidChar(char character) {
        // Square brackets can contains Braces, Parentheses or SquareBrackets
        return character == SQ_BRACKET.getRuleStartChar() || character == BRACES.getRuleStartChar()
                || character == PARENTHESES.getRuleStartChar();
    }
}
