package org.coding.puzzle.processor.string.rules;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.RuleContext;

/**
 * Bracket content validation rule, i.e. determine if the content for the rule
 * is valid
 * 
 * @author p.mankala
 *
 */
public abstract class BracketValidationRule extends BracketMatchingRule {
    @Override
    protected final boolean checkValidity() {
        RuleContext ctx = getParseContext();
        char nextChar = ctx.peekChar();

        if (isValidChar(nextChar)) {
            // The character is valid, check the context and begin new rule
            ctx.consumeChar();
            ctx.setNewRule(true);
            ctx.setNewRuleChar(nextChar);
            return true;
        } else {
            ctx.setNewRule(false);
            return false;
        }
    }

    /**
     * Is the <code>character</code> valid inside this {@link ParseRule}
     * 
     * @param character
     *            character for which validity has to be checked
     * @return true/false
     */
    protected abstract boolean isValidChar(char character);
}
