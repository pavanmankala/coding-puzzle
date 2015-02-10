package org.coding.puzzle;

import junit.framework.Assert;

import org.junit.Test;

public class CliTester {
    @Test
    public void testCli() {
        CommandLineParser parser = new CommandLineParser(System.err);
        parser.addOption('m', true, true, "myName-mine", "Listen! We of the Spear-Danes in the days of yore, of"
                + " those clan-kings heard of their glory how those nobles performed"
                + " courageous deeds Often Scyld, Scef's son, from enemy hosts from many"
                + " peoples seized mead-benches Equivalent to java.lang.Character.isWhitespace");
        parser.addOption('n', true, true, "no-of-args", "number of Arguments", Integer.class);

        if (!parser.parse(new String[] { "--myName-mine=Hello123", "--no-of-args", "1005", })) {
            parser.printHelp(System.err);
        } else {
            parser.printHelp(System.out);
        }

        Assert.assertTrue("Hello123".equals(parser.getOptionValue('m')));
        Assert.assertTrue(new Integer(1005).equals(parser.getOptionValue('n')));
    }
}
