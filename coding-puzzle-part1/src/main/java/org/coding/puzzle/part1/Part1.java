package org.coding.puzzle.part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.coding.puzzle.CommandLineParser;
import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.processor.string.StringValidityProcessor;
import org.coding.puzzle.processor.string.rules.ParenthesesParseRule;

public class Part1 {
    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser(System.err);

        parser.addOption('n', true, true, "no-of-lines", "The number of lines to be checked for conformance");

        if (!parser.parse(args)) {
            parser.printHelp(System.err);
            System.exit(1);
        }

        Integer noOfLines = (Integer) parser.getOption('n');
        StringValidityProcessor processor = new StringValidityProcessor(new ParenthesesParseRule());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 1; i <= noOfLines; i++) {
            System.out.print("Enter line: ");
            String line = null;

            try {
                line = br.readLine();
            } catch (IOException ioe) {
                System.out.println("IO error trying to read input string!");
                System.exit(1);
            }

            BooleanResult result = processor.process(line);
            System.out.println(i + ":" + (result.resultValue() == true ? "True" : "False"));
        }
    }

    public static void main_1(String[] args) {
        StringValidityProcessor processor = new StringValidityProcessor(new ParenthesesParseRule());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int i = 1;

        while (true) {
            System.out.print("Enter line: ");
            String line = null;

            try {
                line = br.readLine();
            } catch (IOException ioe) {
                System.out.println("IO error trying to read input string!");
                System.exit(1);
            }

            BooleanResult result = processor.process(line);
            System.out.println(i++ + ":" + (result.resultValue() == true ? "True" : "False"));
        }
    }
}
