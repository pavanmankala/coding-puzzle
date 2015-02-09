package org.coding.puzzle.processor.string.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.RuleContext;
import org.coding.puzzle.processor.string.ParseRule.ParseResult;
import org.junit.Before;
import org.junit.Test;

public class TestParenthesesRule {
    private final Map<RuleContext, Boolean> contexts = new HashMap<>();

    @Before
    public void perpareContexts() {
        contexts.put(new RuleContext("(())".toCharArray()), false);
        contexts.put(new RuleContext("([])".toCharArray()), false);
        contexts.put(new RuleContext("aaaa".toCharArray()), false);
        contexts.put(new RuleContext("({})".toCharArray()), true);
    }

    @Test
    public void testBracesRule() {
        ParseRule parenthesesRule = new ParenthesesParseRule();

        for (Entry<RuleContext, Boolean> e : contexts.entrySet()) {
            RuleContext ctx = e.getKey();
            parenthesesRule.setParseContext(ctx);

            if (ctx.peekChar() == '(') {
                ctx.consumeChar();
            }

            Assert.assertTrue("Error in evaluating rule",
                    e.getValue() ? parenthesesRule.validate() == ParseResult.VALID
                            : parenthesesRule.validate() == ParseResult.INVALID);
        }
    }
}
