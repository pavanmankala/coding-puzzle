package org.coding.puzzle.processor.string;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.coding.puzzle.processor.string.rules.BracesParseRule;
import org.coding.puzzle.processor.string.rules.ParenthesesParseRule;
import org.coding.puzzle.processor.string.rules.SquareBracketParseRule;
import org.junit.Before;
import org.junit.Test;

public class TestStringProcessor {
    private final Map<String, Boolean> inputResultMap = new LinkedHashMap<>();

    @Before
    public void prepareInput() {
        inputResultMap.put("\t\t\t   \t\t\t\t", true);
        inputResultMap.put("\t\t\t ff  \t\t\t\t\n\t", false);
        inputResultMap.put("\t\t\t ({})  \t\t\t\t", true);
        inputResultMap.put("\t\t  [] \t \t", true);
        inputResultMap.put("()", true);
        inputResultMap.put("[]", true);
        inputResultMap.put("{}", true);
        inputResultMap.put("(()", false);
        inputResultMap.put(" {}} ", false);
        inputResultMap.put("({}) ", true);
        inputResultMap.put("({)} ", false);
        inputResultMap.put("({}) ", true);
        inputResultMap.put(" ([])", false);
        inputResultMap.put(" (()) ", false);
        inputResultMap.put("{[]} ", true);
        inputResultMap.put("{()} ", false);
        inputResultMap.put("r {{}} ", false);
        inputResultMap.put(" {{}} ", false);
        inputResultMap.put("[()]", true);
        inputResultMap.put(" [{}]", true);
        inputResultMap.put("[[]]", true);
        inputResultMap.put("[[[]]]", true);
        inputResultMap.put("[([])]", false);
        inputResultMap.put("[()()]", true);
        inputResultMap.put("{[][()()]}", true);
        inputResultMap.put("()() ", true);
        inputResultMap.put("{[()({[]})]}{}{}{}{[()()]}{}{}{}{}{}{}{}{}{[[]({})()()()]} ", true);
        inputResultMap.put("\t\t\t        ({[(){[]}[[{}()]()]]})({[[]{[[]()]}()]})({[[](){}][{}[]]"
                + "[()][{}][][][][][][][(){}()[]][[][][][][][][[[[[[]]]]]]][][]}{}{}{[]}{[[]()]}{}{})({[]})", true);
    }

    @Test
    public void testStringProcessor() {
        final Pattern trimPattern = Pattern.compile("^\\s*(.*?)\\s*$");
        for (Entry<String, Boolean> e : inputResultMap.entrySet()) {
            StringValidityProcessor processor;
            Matcher m = trimPattern.matcher(e.getKey());
            String trimmedString = m.matches() ? m.group(1) : " ";

            switch (trimmedString.length() == 0 ? ' ' : trimmedString.charAt(0)) {
                case '{':
                    processor = new StringValidityProcessor(new BracesParseRule());
                    break;
                case '[':
                    processor = new StringValidityProcessor(new SquareBracketParseRule());
                    break;
                case '(':
                default:
                    processor = new StringValidityProcessor(new ParenthesesParseRule());
                    break;
            }

            try {
                Assert.assertTrue("Error in processing string: " + e.getKey(), processor.process(e.getKey())
                        .resultValue() == e.getValue());
            } catch (RuntimeException ex) {
                System.err.println("Error in processing string: " + e.getKey());
                throw ex;
            }
        }
    }
}
