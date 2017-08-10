package com.github.yin.flags.example;

import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.github.yin.flags.annotations.FlagDesc;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Reads files and prints them word by word filtering them by a list of allowed words.
 * This is an example of java-flags use.
 */
@FlagDesc("This class is an example how to print files and filter them by list of words. "
        + "Usage: java com.github.yin.flags.example.ReadmeExample "
        + "[--filter <list-of-words>] <input1> <input2> ...")
public class ReadmeExample {
    /**
     * This file must be readable
     */
    @FlagDesc("Path to file containing a list of words to filter the inputs by")
    static final Flag<String> filter = Flags.create("")
            .validator((String path) -> {
                if (path == null || path.isEmpty()) {
                    throw new Flags.ParseException("Input path is empty");
                } else if (!Files.isRegularFile(Paths.get(path))) {
                    throw new Flags.ParseException("Input path is not regular file");
                } else if (!Files.isReadable(Paths.get(path))) {
                    throw new Flags.ParseException("Input path is not readable");
                }
            });

    private final Path filterFile;

    public static void main(String[] args) {
        List<String> nonFlags;
        try {
            // List<String> nonFlags = Flags.parse(args, ImmutableList.of("com.github.yin.flags.example"));
            nonFlags = Flags.parse(args, ImmutableList.of("com.github.yin.flags.example"));
        } catch (Flags.ParseException e) {
            printUsageAndExit(e.getMessage());
            return;
        }
        if (nonFlags.size() == 0) {
            printUsageAndExit("No input files specified in arguments");
            return;
        }
        // TODO yin: Default value is not validated, implement required flags
        ReadmeExample re = new ReadmeExample();
        for (String a : nonFlags) {
            re.process(Paths.get(a));
        }
    }

    private static void printUsageAndExit(String message) {
        System.err.println(message);
        Flags.printUsage("com.github.yin.flags.example");
        System.exit(1);
    }

    /**
     * Creates instance using java-flag provided values.
     */
    ReadmeExample() {
        this.filterFile = Paths.get(filter.get());
    }

    /**
     * Creates instance supplied by external values.
     */
    ReadmeExample(Path inputfile) {
        this.filterFile = inputfile;
    }

    public void init() throws IOException {
        Object[] objects = Files.lines(filterFile)
                .map(line -> line.split("\\W+"))
                .flatMap(Arrays::stream)
                .toArray();
        System.out.println(objects);
    }

    public void process(Path input) {
        try {
            Files.lines(input).forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
