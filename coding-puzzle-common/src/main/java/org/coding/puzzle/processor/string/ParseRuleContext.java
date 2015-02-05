package org.coding.puzzle.processor.string;

public class ParseRuleContext {
    private final char[] context;
    private int index = 0;

    public ParseRuleContext(char[] string) {
        context = string == null ? new char[0] : string;
    }

    public char nextChar() {
        return context[index++];
    }

    public char peekChar() {
        return context[index];
    }

    public void consumeChar() {
        index++;
    }

    public boolean hasNextChar() {
        return context.length > 0 && index >= 0 && index < context.length;
    }
}
