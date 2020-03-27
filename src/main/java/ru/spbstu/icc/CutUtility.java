package ru.spbstu.icc;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CutUtility {

    @Option(name = "-c", metaVar = "CharsIndent", usage = "Indentation in chars")
    private boolean charsIndent;

    @Option(name = "-w", metaVar = "WordsIndent", usage = "Indentation in words", forbids = {"-c"})
    private boolean wordsIndent;

    @Option(name = "-o", metaVar = "Output", usage = "Output file name")
    private File out;

    @Argument(required = true, metaVar = "Range", usage = "Output range (number-number)")
    private String range;

    @Argument(metaVar = "Input", index = 1, usage = "Input file name")
    private File input;


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
            System.err.println("Command line: cut [-c|-w] [-o ofile] range [file]");
            parser.printUsage(System.err);
            return;
        }

        if (!checkRange(range)) return;
        Cutter cutter = new Cutter(startOfRange, endOfRange);


        try {
            String res;
            if (input == null) {
                Scanner scan = new Scanner(System.in);
                System.out.println("No input file name. Enter the data. Use /stop - to stop entering.");

                List<String> inputLines = new ArrayList<>();
                String inputString = "";
                while (!inputString.equals("/stop")) {
                    inputString = scan.nextLine();
                    inputLines.add(inputString);
                }
                inputLines.remove(inputLines.size() - 1);

                res = cutter.cut(wordsIndent, inputLines);
            } else {
                res = cutter.cut(wordsIndent, input);
            }


            if (out == null) {
                System.out.println(res);
            } else {
                try (FileWriter output = new FileWriter(out);
                     BufferedWriter writer = new BufferedWriter(output)) {
                    writer.write(res);
                }
            }


            System.out.println("It's done");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
