package org.coding.puzzle.part2;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class Part2Test {
    @Test
    public void testPart2() {
        ByteArrayOutputStream os = new ByteArrayOutputStream(100);
        PrintStream out = new PrintStream(os);

        final Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("0:False", "sdfsdf");
        map.put("1:True", "({[[][]{}]})");
        map.put("2:False", "sdfasdf");
        map.put("3:False", "csvsnjvcnm");
        map.put("4:True", "({[[][]{}]})");
        map.put("5:True", "({[[][]{}]})");
        map.put("6:False", "({[[][]})");
        map.put("7:True", "({[[][]{}]})");
        map.put("8:False", "   ");
        map.put("9:False", "");
        map.put("10:False", "({}{}{}{[][][][][}{})");
        map.put("11:True", "({}{}{}{[][][][({[]})][(){}]}{[]}{[]}{[]}{[]})");
        map.put("12:True", "({[]})");
        map.put("13:False", "({[]}d)");
        map.put("14:False", "({]})");
        map.put("15:False", "({[])");
        map.put("16:False", "({}{}[][[][({[]})][(){}]}{[]}{[]}{[]})");
        map.put("17:False", "({}}{}{[][][)][(){}]}{[]}{[]}{[]}{[]})");
        map.put("18:True", "({}{}{}{[]}{[]}{[]}{[]})");

        new Part2(new String[] { "-n", "19" }, out, System.err) {
            @Override
            protected LineReader createLineReader() throws FileNotFoundException, Exception {
                return new LineReader() {
                    Iterator<String> iter = map.values().iterator();

                    @Override
                    public void close() throws IOException {
                    }

                    @Override
                    public String readLine() throws IOException {
                        if (iter.hasNext()) {
                            return iter.next();
                        } else {
                            return null;
                        }
                    }
                };
            }
        }.execute();

        for (String line : new String(os.toByteArray()).split("\\r?\\n")) {
            Assert.assertTrue(map.containsKey(line));
        }
    }
}
