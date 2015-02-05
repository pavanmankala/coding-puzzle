package org.coding.puzzle.processor.string;

public abstract class ParseRule {
    private ParseRuleContext parseContext;

    public abstract boolean validate();

    public final void setParseContext(ParseRuleContext parseContext) {
        this.parseContext = parseContext;
    }

    public final ParseRuleContext getParseContext() {
        return parseContext;
    }

    public abstract char getRuleStartCondChar();

    public abstract char getRuleEndCondChar();
}
