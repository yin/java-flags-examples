package com.github.yin.flags.example;

import com.github.yin.flags.annotations.FlagDesc;
import com.github.yin.flags.Flag;
import com.github.yin.flags.Flags;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads file and prints it's output. This is an example of java-flags use.
 */
@FlagDesc("This class is an example how to print files")
public class ReadmeExample {
    /**
     * This file must be readable
     */
    @FlagDesc("Specifies path to input file")
    static final Flag<String> input = Flags.create("")
            .validator((String path) -> {
                if (Strings.isNullOrEmpty(path)) {
                    throw new Flags.ParseException("Input path is empty");
                } else if (!Files.isReadable(Paths.get(path))) {
                    throw new Flags.ParseException("Input path is not readable");
                }
            });

    private final String inputfile;

    public static void main(String[] args) {
        try {
            Flags.parse(args, ImmutableList.of("com.github.yin.flags.example"));
        } catch (Flags.ParseException e) {
            Flags.printUsage("com.github.yin.flags.example");
            System.exit(1);
            return;
        }
        ReadmeExample re = new ReadmeExample();
        re.run();
    }

    /**
     * Creates instance using java-flag provided values.
     */
    ReadmeExample() {
        this.inputfile = input.get();
    }

    /**
     * Creates instance suplied by external values.
     */
    ReadmeExample(String inputfile) {
        this.inputfile = inputfile;
    }

    public void run() {
        Path path = Paths.get(inputfile);
        try {
            Files.lines(path).forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
