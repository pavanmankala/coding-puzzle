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

/**
 * The Part 1 of the coding puzzle processes the input lines from both cmd-line
 * or from a file. This is not multi-threaded
 * 
 * <pre>
 * HOW TO RUN:
 * ===========
 * java -jar &lt;jar_file_name_incl_path&gt; [-n &lt;numberOfLinesToBeRead&gt;] [-f &lt;file&gt;]
 * </pre>
 * 
 * <ul>
 * <li>If both the optional parameters are not given, the the program will run
 * infinitely until it is terminated by Ctrl-C</li>
 * 
 * <li>If both the arguments are given and are valid, only the given number of
 * lines (value of -n parameter) are processed from file</li>
 * 
 * <li>If only -n parameter is specified, then the given number of lines are
 * processed from cmd-line (stdin)</li>
 * 
 * <li>If only -f parameter is specified, all the lines from the file are
 * processed</li>
 * </ul>
 * 
 * @author p.mankala
 *
 */
public class Part1 {
    protected static final char NO_OF_LINES_ARG = 'n', FILE_ARG = 'f';
    /**
     * Error codes for this part 1 application
     */
    protected static final int SUCCESS = 0, ERR_PARSE_ARGS = 1, ERR_READ_LINE = 2, ERR_CLOSE_FILE = 3,
            ERR_FILENOT_FND = 4, OTHER = 5;

    protected final CommandLineParser cliParser;
    protected final StringValidator validator;

    public Part1(String[] args) {
        cliParser = new CommandLineParser(System.err);

        cliParser.addOption(NO_OF_LINES_ARG, false, true, "no-of-lines",
                "<Integer> The number of lines to be checked for conformance", Integer.class);
        cliParser.addOption(FILE_ARG, false, true, "read-from-file", "<File> Read lines from file", File.class);

        boolean parseSuccess = false;
        try {
            parseSuccess = cliParser.parse(args);
        } catch (RuntimeException e) {
            // e.printStackTrace();
        } finally {
            if (!parseSuccess) {
                cliParser.printHelp(System.err);
                System.exit(ERR_PARSE_ARGS);
            }
        }

        validator = new StringValidator(new ParenthesesParseRule());
    }

    public void execute() {
        final boolean unlimitedLines = !cliParser.isOptionGiven(NO_OF_LINES_ARG);
        Integer noOfLines = unlimitedLines ? -1 : cliParser.getOptionValue(NO_OF_LINES_ARG, Integer.class);

        // Prepare LineReader
        try (LineReader lr = cliParser.isOptionGiven(FILE_ARG) ? new FileLineReader(cliParser.getOptionValue(FILE_ARG,
                File.class)) : new SysInReader()) {
            // Iterate over line reader
            for (int i = 0; unlimitedLines || i < noOfLines; i++) {
                String line = null;

                try {
                    line = lr.readLine();
                } catch (IOException ioe) {
                    System.out.println("IO error trying to read input string!");
                    System.exit(ERR_READ_LINE);
                }

                if (line == null) {
                    // EOF reached
                    break;
                } else {
                    evalLine(i, line);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println(fnfe.getMessage());
            cliParser.printHelp(System.err);
            System.exit(ERR_FILENOT_FND);
        } catch (IOException ioe) {
            System.err.println("Error while closing reader: " + ioe.getMessage());
            System.exit(ERR_CLOSE_FILE);
        }
    }

    protected void evalLine(int lineNo, String line) {
        BooleanResult result = validator.process(line);
        System.out.println(result.resultValue() == true ? "True" : "False");
    }

    protected static interface LineReader extends Closeable {
        public String readLine() throws IOException;
    }

    protected static class FileLineReader implements LineReader {
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

    protected static class SysInReader implements LineReader {
        private final BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

        public SysInReader() {
        }

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

    public static void main(String[] args) {
        new Part1(args).execute();
        System.exit(SUCCESS);
    }
}
