package org.coding.puzzle.part1;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.coding.puzzle.CommandLineParser;
import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.processor.string.StringValidator;
import org.coding.puzzle.processor.string.rules.ParenthesesParseRule;

public class Part1 {
    private static final char NO_OF_LINES_ARG = 'n', FILE_ARG = 'f';

    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser(System.err);

        parser.addOption(NO_OF_LINES_ARG, false, true, "no-of-lines",
                "<Integer> The number of lines to be checked for conformance", Integer.class);
        parser.addOption(FILE_ARG, false, true, "read-from-file", "<File> Read lines from file", File.class);

        boolean parseSuccess = false;
        try {
            parseSuccess = parser.parse(args);
        } catch (RuntimeException e) {
            // e.printStackTrace();
        } finally {
            if (!parseSuccess) {
                parser.printHelp(System.err);
                System.exit(1);
            }
        }

        StringValidator processor = new StringValidator(new ParenthesesParseRule());

        final boolean limitedLines = parser.isOptionGiven(NO_OF_LINES_ARG);
        Integer noOfLines = limitedLines ? parser.getOptionValue(NO_OF_LINES_ARG, Integer.class) : Integer.MAX_VALUE;

        // Prepare LineReader
        try (LineReader lr = parser.isOptionGiven(FILE_ARG) ? new FileLineReader(parser.getOptionValue(FILE_ARG,
                File.class)) : new SysInReader()) {
            // Iterate over line reader
            for (int i = 1; limitedLines && i <= noOfLines; i++) {
                String line = null;

                try {
                    line = lr.readLine();
                } catch (IOException ioe) {
                    System.out.println("IO error trying to read input string!");
                    System.exit(1);
                }

                if (line == null) {
                    // EOF reached
                    break;
                } else {
                    BooleanResult result = processor.process(line);
                    System.out.println(i + ":" + (result.resultValue() == true ? "True" : "False"));
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
            parser.printHelp(System.err);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("Error while closing reader: " + ioe.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    static interface LineReader extends Closeable {
        public String readLine() throws IOException;
    }

    static class FileLineReader implements LineReader {
        private final BufferedReader buffReader;

        public FileLineReader(File file) throws FileNotFoundException {
            buffReader = new BufferedReader(new FileReader(file));
        }

        @Override
        public String readLine() throws IOException {
            return buffReader.readLine();
        }

        @Override
        public void close() throws IOException {
            buffReader.close();
        }
    }

    static class SysInReader implements LineReader {
        private final BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

        @Override
        public String readLine() throws IOException {
            System.out.print("Enter line: ");
            return buffReader.readLine();
        }

        @Override
        public void close() throws IOException {
            buffReader.close();
        }
    }
}
