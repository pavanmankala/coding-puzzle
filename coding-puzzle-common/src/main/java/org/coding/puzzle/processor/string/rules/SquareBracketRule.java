package org.coding.puzzle.processor.string.rules;


public class SquareBracketRule extends BracketMatchingRule {
    @Override
    public char getRuleStartCondChar() {
        return '[';
    }

    @Override
    public char getRuleEndCondChar() {
        return ']';
    }

    @Override
    public boolean checkValidity() {
        return false;
    }
}
