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
import org.coding.puzzle.processor.string.ParseRule.ParseResult;

/**
 * This class determines if a {@link String} conforms to a given rule in the
 * code puzzle.
 * 
 * @author p.mankala
 *
 */
public class StringValidator implements Processor<String, Boolean, BooleanResult> {
    /**
     * Static values for PASS or FAIL condition
     */
    private static final BooleanResult PASS = new BooleanResult(true), FAIL = new BooleanResult(false);

    /**
     * The beginning/recurring rule for the line
     */
    private final ParseRule beginRule;
    /**
     * The pattern object used to trim the input line
     */
    // String.trim() could be used, but it will not catch TAB characters
    private final Pattern trimPattern = Pattern.compile("^\\s*(.*?)\\s*$");
    /**
     * All available {@link ParseRule}s in the class-path will be stored, using
     * {@link ServiceLoader}
     */
    private final Map<Character, ParseRule> parseRules;

    /**
     * Create a StringValidator object using the passed in rule,
     * <code>beginRule</code>, as beginning/recurring rule
     * 
     * @param beginRule
     *            beginning/recurring rule for this object
     */
    public StringValidator(ParseRule beginRule) {
        this.beginRule = beginRule;
        Map<Character, ParseRule> allRules = new HashMap<>();

        for (ParseRule rule : ServiceLoader.load(ParseRule.class)) {
            allRules.put(rule.getRuleStartChar(), rule);
        }

        parseRules = Collections.unmodifiableMap(allRules);
    }

    /**
     * Processes the <code>input</code> String and determines if it conforms to
     * the rules. This method processes a single line of text. If the string
     * contains is multi-line, the result will be a false {@link BooleanResult}.
     * An empty line is treated as valid string.
     * 
     * @param input
     *            input {@link String} to be checked for conformance
     * @return a {@link BooleanResult}; The value of the result is true if the
     *         <code>input</code> conforms to rules, false otherwise
     */
    @Override
    public BooleanResult process(final String input) {
        String[] lines = input.split("\\r?\\n");
        ParseResult state = ParseResult.INVALID;

        // the passed-in input is a multi-line string, return FAIL
        if (lines.length > 1) {
            return FAIL;
        }

        String line = lines[0];
        Matcher m = trimPattern.matcher(line);

        if (!m.matches()) {
            return FAIL;
        } else {
            String trimmedString = m.group(1);
            // prepare context object
            RuleContext ctx = new RuleContext(trimmedString.toCharArray());

            // assign this context object to all rules; the rules chew context
            // object
            beginRule.setParseContext(ctx);
            for (ParseRule rule : parseRules.values()) {
                rule.setParseContext(ctx);
            }

            // Rule stack
            Stack<ParseRule> ruleStack = new Stack<>();

            // loop until the context is entirely chewed or till one of the
            // rules fails
            while (ctx.hasNextChar()) {
                state = ctx.nextChar() == beginRule.getRuleStartChar() ? ParseResult.VALID : ParseResult.INVALID;

                // if the string begin char is not conforming to beginRule
                // return FAIL
                if (state == ParseResult.INVALID) {
                    return FAIL;
                }

                ruleStack.push(beginRule);

                do {
                    ParseRule rule = ruleStack.peek();
                    state = rule.validate();

                    switch (state) {
                        case VALID:
                            if (ctx.isNewRule()) {
                                // Rule is valid, but we need to start new rule
                                // aka Rule switch
                                char newRuleBegin = ctx.getNewRuleChar();
                                ParseRule newRule = parseRules.get(newRuleBegin);
                                if (newRule != null) {
                                    ruleStack.push(newRule);
                                } else {
                                    state = ParseResult.INVALID;
                                }
                            }
                            break;
                        case INVALID:
                            // Rule is invalid
                            return FAIL;
                        case END:
                            // Rules has ended properly
                            ruleStack.pop();
                            break;
                    }
                } while (!ruleStack.isEmpty() && state != ParseResult.INVALID);
            }

            return PASS;
        }
    }
}
