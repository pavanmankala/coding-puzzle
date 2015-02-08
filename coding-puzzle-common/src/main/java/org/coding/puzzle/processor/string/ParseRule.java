package org.coding.puzzle.processor.string;

public abstract class ParseRule {
    public static enum ParseRuleState {
        VALID, INVALID, CLOSED
    }

    private ParseRuleContext parseContext;

    public abstract ParseRuleState validate();

    public final void setParseContext(ParseRuleContext parseContext) {
        this.parseContext = parseContext;
    }

    public final ParseRuleContext getParseContext() {
        return parseContext;
    }

    public abstract char getRuleStartChar();

    public abstract char getRuleEndChar();
}
