package ru.spbstu.icc;

import javax.imageio.IIOException;
import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Cutter {
    private final String inputFile;
    private final int startOfRange;
    private final int endOfRange;

    public Cutter(String in, int startOfRange, int endOfRange) {
        this.inputFile = in;
        this.startOfRange = startOfRange;
        this.endOfRange = endOfRange;
    }

    private String changeLineW(String line){
        Pattern pattern = Pattern.compile(" ");
        String[] words = pattern.split(line);

        StringBuilder result = new StringBuilder();
        int x = Math.min(endOfRange, words.length);
        for (int i = startOfRange; i < x; i++){
            result.append(words[i]).append(" ");
        }
        int y = result.length();
        if (y != 0) result.deleteCharAt(y - 1);
        return result.toString();
    }

    private String changeLineC(String line){
        StringBuilder result = new StringBuilder();
        int x = Math.min(endOfRange, line.length());
        for (int i = startOfRange; i < x; i++){
            result.append(line.charAt(i));
        }
        return result.toString();
    }

    public String cut(boolean flag) throws IOException {
        try (FileReader in = new FileReader(inputFile);
        BufferedReader reader = new BufferedReader(in)) {
            StringBuilder result = new StringBuilder();
            String line = reader.readLine();
            boolean firstLine = true;
            while (line != null) {
                String changedLine = flag ? changeLineW(line) : changeLineC(line);
                if (!firstLine) result.append(System.lineSeparator());
                result.append(changedLine);
                line = reader.readLine();
                firstLine = false;
            }
            return result.toString();
        }
    }
}
