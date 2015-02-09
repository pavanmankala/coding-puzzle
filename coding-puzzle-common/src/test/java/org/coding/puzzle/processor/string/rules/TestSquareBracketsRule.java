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

public class TestSquareBracketsRule {
    private final Map<RuleContext, Boolean> contexts = new HashMap<>();

    @Before
    public void perpareContexts() {
        contexts.put(new RuleContext("[()]".toCharArray()), true);
        contexts.put(new RuleContext("[{}]".toCharArray()), true);
        contexts.put(new RuleContext("[[]]".toCharArray()), true);
        contexts.put(new RuleContext("[[[]]]".toCharArray()), true);
        contexts.put(new RuleContext("[aaa]".toCharArray()), false);
    }

    @Test
    public void testBracesRule() {
        ParseRule sqBracketRule = new SquareBracketParseRule();

        for (Entry<RuleContext, Boolean> e : contexts.entrySet()) {
            RuleContext ctx = e.getKey();
            sqBracketRule.setParseContext(ctx);

            if (ctx.peekChar() == '[') {
                ctx.consumeChar();
            }

            Assert.assertTrue(e.getValue() ? sqBracketRule.validate() == ParseResult.VALID : sqBracketRule
                    .validate() == ParseResult.INVALID);
        }
    }
}
