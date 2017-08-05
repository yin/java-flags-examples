package com.github.yin.flags;

@FlagDesc("This class is an example how to print files")
public class ReadmeExample {
    /** This file must be readable */
    @FlagDesc("Specifies path to input file")
    static final Flag<String> input = Flags.string("")
	.validator((String path) -> Files.readable(Paths.get(path)) );

    private final String inputfile;

    public static void main(String[] args) {
	Flags.init(args);
	ReadmeExample re = new ReadmeExample();

	if (!re.check()) {
	    Flags.printUsage();
	    System.exit(1);
	    return;
	}

	re.run();
    }

    /** Creates instance using java-flag provided values. */
    ReadmeExample() {
	this.inputfile = input.get();
    }

    /** Creates instance suplied by external values. */
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
