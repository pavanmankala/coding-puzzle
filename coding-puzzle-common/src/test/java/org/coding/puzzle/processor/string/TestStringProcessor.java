package org.coding.puzzle.processor.string;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

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
        inputResultMap.put("\t\t\t   \t\t\t\t", false);
        inputResultMap.put("\t\t\t  dd  \t\t\t\t", false);
        inputResultMap.put("\t\t\t   ff  \t\t\t\t\n\t", false);
        inputResultMap.put("\t\t\t ({})  \t\t\t\t", false);
        inputResultMap.put("\t\t  [] \t \t", false);
        inputResultMap.put("()", true);
        inputResultMap.put("[]", true);
        inputResultMap.put("{}", true);
        inputResultMap.put("(()", false);
        inputResultMap.put("{}}", false);
        inputResultMap.put("({})", true);
        inputResultMap.put("({)}", false);
        inputResultMap.put("({})", true);
        inputResultMap.put("([])", false);
        inputResultMap.put("(())", false);
        inputResultMap.put("{[]}", true);
        inputResultMap.put("{()}", false);
        inputResultMap.put("r {{}} ", false);
        inputResultMap.put("{{}}", false);
        inputResultMap.put("[()]", true);
        inputResultMap.put("[{}]", true);
        inputResultMap.put("[[]]", true);
        inputResultMap.put("[[[]]]", true);
        inputResultMap.put("[([])]", false);
        inputResultMap.put("[()()]", true);
        inputResultMap.put("{[][()()]}", true);
        inputResultMap.put("()()", true);
        inputResultMap.put("{[()({[]})]}{}{}{}{[()()]}{}{}{}{}{}{}{}{}{[[]({})()()()]}", true);
        inputResultMap
                .put("({[(){[]}[[{}()]()]]})({[[]{[[]()]}()]})({[[](){}][{}[]]"
                        + "[()][{}][][][][][][][(){}()[]][[][][][][][][[[[[[]]]]]]][][]}"
                        + "{}{}{[]}{[[]()]}{}{})({[]})", true);
    }

    @Test
    public void testStringProcessor() {
        for (Entry<String, Boolean> e : inputResultMap.entrySet()) {
            StringValidator processor;
            String arg = e.getKey();

            switch (arg.isEmpty() ? ' ' : arg.charAt(0)) {
                case '{':
                    processor = new StringValidator(new BracesParseRule());
                    break;
                case '[':
                    processor = new StringValidator(new SquareBracketParseRule());
                    break;
                case '(':
                default:
                    processor = new StringValidator(new ParenthesesParseRule());
                    break;
            }

            Assert.assertTrue("Error in processing string: " + e.getKey(),
                    processor.process(e.getKey()).resultValue() == e.getValue());
        }
    }
}
