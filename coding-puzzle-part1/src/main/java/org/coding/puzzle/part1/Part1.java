package org.coding.puzzle.part1;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.coding.puzzle.CommandLineParser;
import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.processor.string.ParseRule;
import org.coding.puzzle.processor.string.StringValidator;
import org.coding.puzzle.processor.string.rules.SquareBracketParseRule;

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
    protected final PrintStream out, err;

    public Part1(String[] args, PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
        cliParser = new CommandLineParser(out, err);

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
                cliParser.printHelp(err);
                exit(ERR_PARSE_ARGS);
            } else if (cliParser.isAskedForHelp()) {
                exit(SUCCESS);
            }
        }

        validator = new StringValidator(getBeginRule());
    }

    protected ParseRule getBeginRule() {
        return new SquareBracketParseRule() {
            char endChar;

            @Override
            public char getRuleEndChar() {
                return endChar;
            }

            @Override
            public char getRuleStartChar() {
                char startChar = getParseContext().peekChar();
                endChar = 0;

                switch (startChar) {
                    case '{':
                        endChar = '}';
                        break;
                    case '[':
                        endChar = ']';
                        break;
                    case '(':
                        endChar = ')';
                        break;
                    default:
                        return 0;
                }

                return startChar;
            }
        };
    }

    public void execute() {
        final boolean unlimitedLines = !cliParser.isOptionGiven(NO_OF_LINES_ARG);
        Integer noOfLines = unlimitedLines ? -1 : cliParser.getOptionValue(NO_OF_LINES_ARG, Integer.class);

        // Prepare LineReader
        try (LineReader lr = createLineReader()) {
            // Iterate over line reader
            for (int i = 0; unlimitedLines || i < noOfLines; i++) {
                String line = null;

                try {
                    line = lr.readLine();
                } catch (IOException ioe) {
                    out.println("IO error trying to read input string!");
                    exit(ERR_READ_LINE);
                }

                if (line == null) {
                    // EOF reached
                    break;
                } else {
                    evalLine(i, line);
                }
            }
        } catch (FileNotFoundException fnfe) {
            err.println(fnfe.getMessage());
            exit(ERR_FILENOT_FND);
        } catch (IOException ioe) {
            err.println("Error while closing reader: " + ioe.getMessage());
            exit(ERR_CLOSE_FILE);
        } catch (Exception e) {
            err.println("Error: " + e.getMessage());
            exit(OTHER);
        }
    }

    protected LineReader createLineReader() throws FileNotFoundException, Exception {
        LineReader lr = cliParser.isOptionGiven(FILE_ARG) ? new FileLineReader(cliParser.getOptionValue(FILE_ARG,
                File.class)) : new SysInReader(out);
        return lr;
    }

    protected void evalLine(int lineNo, String line) {
        BooleanResult result = validator.process(line);
        out.println(result.resultValue() == true ? "True" : "False");
    }

    public static interface LineReader extends Closeable {
        public String readLine() throws IOException;
    }

    public static class FileLineReader implements LineReader {
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

    public static class SysInReader implements LineReader {
        private final BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
        private final PrintStream out;

        public SysInReader(PrintStream out) {
            this.out = out;
        }

        @Override
        public String readLine() throws IOException {
            out.print("Enter line: ");
            return buffReader.readLine();
        }

        @Override
        public void close() throws IOException {
            buffReader.close();
        }
    }

    protected static void exit(int status) {
        System.exit(status);
    }

    public static void main(String[] args) {
        new Part1(args, System.out, System.err).execute();
        exit(SUCCESS);
    }
}
