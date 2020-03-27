package ru.spbstu.icc;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

public class Cutter {
    private int startOfRange;
    private int endOfRange;

    public Cutter(int startOfRange, int endOfRange) {
        this.startOfRange = startOfRange;
        this.endOfRange = endOfRange;
    }

    private String changeLineW(String line){
        Pattern pattern = Pattern.compile(" ");
        String[] words = pattern.split(line);

        StringBuilder result = new StringBuilder();
        int x = Math.min(endOfRange, words.length);
        for (int i = startOfRange; i <= x; i++){
            result.append(words[i]).append(" ");
        }
        int y = result.length();
        if (y != 0) result.deleteCharAt(y - 1);
        return result.toString();
    }

    private String changeLineC(String line){
        StringBuilder result = new StringBuilder();
        int x = Math.min(endOfRange, line.length());
        for (int i = startOfRange; i <= x; i++){
            result.append(line.charAt(i));
        }
        return result.toString();
    }

    public String cut(boolean flag, File input) throws IOException {
        try (FileReader in = new FileReader(input);
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

    public String cut(boolean flag, List<String> input) {
            StringBuilder result = new StringBuilder();
            boolean firstLine = true;
            for (String line:input) {
                String changedLine = flag ? changeLineW(line) : changeLineC(line);
                if (!firstLine) result.append(System.lineSeparator());
                result.append(changedLine);
                firstLine = false;
            }
            return result.toString();
    }
}
