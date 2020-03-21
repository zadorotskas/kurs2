package ru.spbstu.icc;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CutUtility {

    @Option(name = "-c", metaVar = "CharsIndent", usage = "Indentation in chars")
    private boolean charsIndent;

    @Option(name = "-w", metaVar = "WordsIndent", usage = "Indentation in words", forbids = {"-c"})
    private boolean wordsIndent;

    @Option(name = "-o", metaVar = "OutputName", usage = "Output file name")
    private String outputFileName;

    @Argument(metaVar = "InputName", usage = "Input file name")
    private String inputFileName;

    @Argument(required = true, metaVar = "Range", index = 1, usage = "Output range (number-number)")
    private String range;

    public static void main(String[] args){
        new CutUtility().doMain(args);
    }

    int startOfRange = 0;
    int endOfRange = 0;
    private boolean checkRange(String range) {
        Pattern pattern1 = Pattern.compile("^(\\d*-\\d*)$");
        Matcher matcher = pattern1.matcher(range);
        if (!matcher.find()) {
            System.err.println("Incorrect range. Use int-int, int- or -int.");
            return false;
        }
        Pattern pattern2 = Pattern.compile("-");
        String[] ranges = pattern2.split(range);
        startOfRange = ranges[0].length() > 0 ? Integer.parseInt(ranges[0]) : 0;
        endOfRange = ranges[1].length() > 0 ? Integer.parseInt(ranges[1]) : Integer.MAX_VALUE;
        if (endOfRange < startOfRange) {
            System.err.println("Incorrect range. End of range should be more than start of range.");
            return false;
        }
        return true;
    }

    private void doMain(String[] args){
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Command line: cut [-c|-w] [-o ofile] [file] range");
            parser.printUsage(System.err);
            return;
        }

        if (!checkRange(range)) return;

        Cutter cutter = new Cutter(inputFileName, startOfRange, endOfRange);
        try {
            String res = cutter.cut(wordsIndent);
            if (outputFileName == null) {
                System.out.println(res);
            } else {
                try (FileWriter out = new FileWriter(outputFileName);
                     BufferedWriter writer = new BufferedWriter(out)) {
                    writer.write(res);
                }
            }
            System.out.println("It's done");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
