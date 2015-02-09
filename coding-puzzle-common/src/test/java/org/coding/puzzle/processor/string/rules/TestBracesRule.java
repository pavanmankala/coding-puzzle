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

public class TestBracesRule {
    private final Map<RuleContext, Boolean> contexts = new HashMap<>();

    @Before
    public void perpareContexts() {
        contexts.put(new RuleContext("{()}".toCharArray()), false);
        contexts.put(new RuleContext("{{}}".toCharArray()), false);
        contexts.put(new RuleContext("{(})".toCharArray()), false);
        contexts.put(new RuleContext("aaa".toCharArray()), false);
        contexts.put(new RuleContext("{[]} ".toCharArray()), true);
    }

    @Test
    public void testBracesRule() {
        ParseRule bracesRule = new BracesParseRule();

        for (Entry<RuleContext, Boolean> e : contexts.entrySet()) {
            RuleContext ctx = e.getKey();
            bracesRule.setParseContext(ctx);

            if (ctx.peekChar() == '{') {
                ctx.consumeChar();
            }

            Assert.assertTrue(e.getValue() ? bracesRule.validate() == ParseResult.VALID
                    : bracesRule.validate() == ParseResult.INVALID);
        }
    }
}
