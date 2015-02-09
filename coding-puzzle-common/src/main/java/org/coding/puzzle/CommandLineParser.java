package org.coding.puzzle;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A quickly-written class to parse command line arguments
 * 
 * @author p.mankala
 *
 */
public class CommandLineParser {
    private final Map<Character, Option> charOptions = new LinkedHashMap<>();
    private final Map<String, Option> longNameOptions = new LinkedHashMap<>();
    private final Map<Option, Class<?>> optionTypes = new HashMap<>();
    private final Map<Class<?>, Converter<?>> converters = new HashMap<>();
    private final Set<Option> mandatoryOptions = new HashSet<>(), allOptions = new LinkedHashSet<>();
    private final Pattern shortArg = Pattern.compile("-(\\p{Alpha})"), longArg = Pattern
            .compile("--(\\p{Alnum}[\\p{Alnum}-]+)(=(.*))?");
    private final Map<Option, Object> parsedArgs = new HashMap<>();
    private final PrintStream internalErrStream;

    public CommandLineParser(PrintStream internalErrStream) {
        this.internalErrStream = internalErrStream;
        Converter<String> stringConverter = new Converter<String>() {
            @Override
            public String convert(String argument) throws ConversionException {
                return argument;
            }

            @Override
            public String errorMessage() {
                return "";
            }
        };
        Converter<Integer> intConverter = new Converter<Integer>() {
            private String errMessage;

            @Override
            public Integer convert(String argument) throws ConversionException {
                errMessage = "Unable to convert Argument - " + argument + ", to integer";
                try {
                    return Integer.parseInt(argument);
                } catch (RuntimeException e) {
                    throw new ConversionException(e);
                }
            }

            @Override
            public String errorMessage() {
                return errMessage;
            }
        };
        Converter<Long> longConverter = new Converter<Long>() {
            private String errMessage;

            @Override
            public Long convert(String argument) throws ConversionException {
                errMessage = "Unable to convert Argument - " + argument + ", to long";
                try {
                    return Long.parseLong(argument);
                } catch (RuntimeException e) {
                    throw new ConversionException(e);
                }
            }

            @Override
            public String errorMessage() {
                return errMessage;
            }
        };
        Converter<Float> floatConverter = new Converter<Float>() {
            private String errMessage;

            @Override
            public Float convert(String argument) throws ConversionException {
                errMessage = "Unable to convert Argument - " + argument + ", to float";
                try {
                    return Float.parseFloat(argument);
                } catch (RuntimeException e) {
                    throw new ConversionException(e);
                }
            }

            @Override
            public String errorMessage() {
                return errMessage;
            }
        };

        addConverter(String.class, stringConverter);
        addConverter(Integer.class, intConverter);
        addConverter(int.class, intConverter);
        addConverter(Long.class, longConverter);
        addConverter(long.class, longConverter);
        addConverter(Float.class, floatConverter);
        addConverter(float.class, floatConverter);
    }

    public <V> void addConverter(Class<V> clazz, Converter<V> converter) {
        converters.put(clazz, converter);
    }

    public void addOption(boolean mandatory, boolean hasValue, String longName, String description) {
        addOption((char) 0, mandatory, hasValue, longName, description, String.class);
    }

    public void addOption(char singleChar, boolean mandatory, boolean hasValue, String longName, String description) {
        addOption(singleChar, mandatory, hasValue, longName, description, String.class);
    }

    public void addOption(char singleChar, boolean mandatory, boolean hasValue, String longName, String description,
            Class<?> clazz) {
        if (!Character.isAlphabetic(singleChar)) {
            singleChar = 0;
        }

        if (longName == null) {
            longName = "";
        }

        if (singleChar == 0 && longName.isEmpty()) {
            throw new RuntimeException("option is not alpha and longName is null");
        }

        Option option = new Option(singleChar, hasValue, longName, description);

        if (singleChar != 0) {
            if (!charOptions.containsKey(singleChar)) {
                charOptions.put(singleChar, option);
            } else {
                throw new RuntimeException("Option already exists");
            }
        }

        if (!longName.isEmpty()) {
            if (!longNameOptions.containsKey(longName)) {
                longNameOptions.put(longName, option);
            } else {
                throw new RuntimeException("Long Option already exists");
            }
        }

        optionTypes.put(option, clazz);

        if (mandatory) {
            mandatoryOptions.add(option);
        }

        allOptions.add(option);
    }

    public boolean parse(String[] cliArgs) {
        Iterator<String> argsIter = Arrays.asList(cliArgs).iterator();

        while (argsIter.hasNext()) {
            String arg = argsIter.next();
            Matcher sam = shortArg.matcher(arg);
            Matcher lam = longArg.matcher(arg);

            if (sam.matches()) {
                String shortArgStr = sam.group(1);
                if (shortArgStr.length() == 1 && charOptions.containsKey(shortArgStr.charAt(0))) {
                    Option o = charOptions.get(shortArgStr.charAt(0));

                    if (o.hasValue) {
                        addParsedValue(arg, argsIter.hasNext() ? argsIter.next() : null, o);
                    }
                    continue;
                }
            } else if (lam.matches()) {
                String longArgStr = lam.group(1);
                if (longNameOptions.containsKey(longArgStr)) {
                    Option o = longNameOptions.get(longArgStr);

                    if (o.hasValue) {
                        String value = lam.group(3);
                        value = value == null ? (argsIter.hasNext() ? argsIter.next() : null) : value;
                        addParsedValue(arg, value, o);
                    }
                    continue;
                }
            }

            internalErrStream.println("Invalid arg: " + arg);
            return false;
        }

        boolean noerr = true;
        for (Option o : mandatoryOptions) {
            if (!parsedArgs.containsKey(o)) {
                internalErrStream.println("Argument not specified: --" + o.longName);
                noerr = false;
            }
        }

        return noerr;
    }

    public void printHelp(PrintStream ps) {
        int longOpt = 0, description = 30;

        for (Option opt : allOptions) {
            longOpt = Math.max(longOpt, opt.longName.length());
        }

        char[] blank = new char[16 + longOpt];
        Arrays.fill(blank, ' ');
        String descSpaces = new String(blank);

        ps.println();
        StringBuilder buff = new StringBuilder();

        buff.append("Usage:\n");

        for (Option opt : allOptions) {
            buff.append("     ");

            if (opt.singleChar == 0) {
                buff.append("  , ");
            } else {
                buff.append("-" + opt.singleChar + ", ");
            }

            buff.append("--" + opt.longName);

            for (int i = 0; i < longOpt - opt.longName.length(); i++) {
                buff.append(" ");
            }

            buff.append("     ");

            StringBuilder b = new StringBuilder();
            int line = 1;

            for (String word : opt.description.split("\\s")) {
                b.append(word);

                if ((b.length() - (descSpaces.length() * (line - 1))) / description >= line) {
                    b.append("\n");
                    b.append(descSpaces);
                    line++;
                } else {
                    b.append(" ");
                }
            }

            buff.append(b.toString());
            buff.append("\n");
        }

        ps.append(buff.toString());
    }

    public Object getOption(char singleChar) {
        Option o = charOptions.get(singleChar);

        if (o == null) {
            return null;
        } else {
            return parsedArgs.get(o);
        }
    }

    public Object getOption(String longName) {
        Option o = longNameOptions.get(longName);

        if (o == null) {
            return null;
        } else {
            return parsedArgs.get(o);
        }
    }

    private void addParsedValue(String arg, String valueArg, Option opt) {
        Class<?> clazz = optionTypes.get(opt);
        Converter<?> converter = converters.get(clazz);

        if (valueArg != null) {
            if (parsedArgs.containsKey(opt)) {
                String err = "argument " + arg + " already specified";
                internalErrStream.println(err);
                throw new RuntimeException(err);
            }

            try {
                Object obj = converter.convert(valueArg);
                parsedArgs.put(opt, obj);
            } catch (ConversionException ce) {
                String err = "Invalid type specified for option: " + arg + "; " + converter.errorMessage();
                internalErrStream.println(err);
                throw new RuntimeException(err);
            }
        } else {
            String err = "No value specified to option: " + arg;
            internalErrStream.println(err);
            throw new RuntimeException(err);
        }
    }

    private static class Option {
        private final char singleChar;
        private final boolean hasValue;
        private final String longName, description;

        public Option(char singleChar, boolean hasValue, String longName, String description) {
            this.singleChar = singleChar;
            this.longName = longName;
            this.description = description;
            this.hasValue = hasValue;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Option) {
                Option other = (Option) obj;
                if (other.singleChar == singleChar) {
                    if (longName == null && other.longName == null) {
                        return true;
                    } else {
                        return longName.equals(other.longName);
                    }
                }

                return false;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = singleChar;
            if (longName != null) {
                hash += 31 * longName.hashCode();
            }
            return hash;
        }
    }

    public static interface Converter<V> {
        public V convert(String argument) throws ConversionException;

        public String errorMessage();
    }

    public static class ConversionException extends RuntimeException {
        public ConversionException(RuntimeException e) {
            super(e);
        }
    }
}
