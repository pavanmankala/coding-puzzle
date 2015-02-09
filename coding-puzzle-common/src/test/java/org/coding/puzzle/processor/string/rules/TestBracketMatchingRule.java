package org.coding.puzzle.processor.string.rules;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

import junit.framework.Assert;

import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.RuleContext;
import org.coding.puzzle.processor.string.ParseRule.ParseResult;
import org.junit.Before;
import org.junit.Test;

public class TestBracketMatchingRule {
    private final Map<RuleContext, Boolean> contexts = new LinkedHashMap<>();

    @Before
    public void perpareContexts() {
        contexts.put(new RuleContext("{}".toCharArray()), true);
        contexts.put(new RuleContext("{)".toCharArray()), false);
        contexts.put(new RuleContext("{]".toCharArray()), false);
        contexts.put(new RuleContext("[]".toCharArray()), true);
        contexts.put(new RuleContext("()".toCharArray()), true);
    }

    @Test
    public void testBracesRule() {
        Map<Character, ParseRule> rules = new HashMap<>();

        for (ParseRule rule : ServiceLoader.load(ParseRule.class)) {
            rules.put(rule.getRuleStartChar(), rule);
        }

        for (Entry<RuleContext, Boolean> e : contexts.entrySet()) {
            RuleContext ctx = e.getKey();
            ParseRule rule = rules.get(ctx.peekChar());

            Assert.assertNotNull(rule);

            rule.setParseContext(ctx);
            ctx.consumeChar();

            Assert.assertTrue(e.getValue() ? rule.validate() == ParseResult.END
                    : rule.validate() != ParseResult.END);
        }
    }
}
