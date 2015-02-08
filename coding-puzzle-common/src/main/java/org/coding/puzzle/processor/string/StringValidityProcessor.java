package org.coding.puzzle.processor.string;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coding.puzzle.Processor;
import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.processor.string.ParseRule.ParseRuleState;

public class StringValidityProcessor implements
        Processor<String, Boolean, BooleanResult> {
    private final ParseRule beginRule;
    private final Pattern trimPattern = Pattern.compile("^\\s*(.*)\\s*$");
    private final Map<Character, ParseRule> parseRules;

    public StringValidityProcessor(ParseRule beginRule) {
        this.beginRule = beginRule;
        Map<Character, ParseRule> allRules = new HashMap<>();

        for (ParseRule rule : ServiceLoader.load(ParseRule.class)) {
            allRules.put(rule.getRuleStartChar(), rule);
        }

        parseRules = Collections.unmodifiableMap(allRules);
    }

    @Override
    public BooleanResult process(final String input) {
        BooleanResult pass = new BooleanResult(true), fail = new BooleanResult(
                false);
        Matcher m = trimPattern.matcher(input);

        if (!m.matches()) {
            return fail;
        } else {
            String trimmedString = m.group(1);
            ParseRuleContext ctx = new ParseRuleContext(
                    trimmedString.toCharArray());
            Stack<ParseRule> ruleStack = new Stack<>();
            ParseRuleState state = ParseRuleState.VALID;
            ruleStack.push(beginRule);

            for (ParseRule rule : parseRules.values()) {
                rule.setParseContext(ctx);
            }

            while (!ruleStack.isEmpty() && state != ParseRuleState.INVALID) {
                ParseRule rule = ruleStack.peek();
                state = rule.validate();
                switch(state) {
                    case VALID:
                    case INVALID:
                    case CLOSED:
                        ruleStack.pop();
                }
            }
        }

        return pass;
    }
}
