package org.coding.puzzle.processor.string;

public abstract class ParseRule {
    private final ParseRule intern;

    public ParseRule(ParseRule rule) {
        intern = rule;
    }

    public boolean checkValidity() {
        return intern.checkValidity();
    }

    public abstract char getRuleStartCondChar();
}
