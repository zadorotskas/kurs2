package ru.spbstu.icc;


import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CutUtilityTest {
    private String ls = System.lineSeparator();
    private String fs = File.separator;
    private String inputFile = "src" + fs + "test" + fs + "file1.txt";
    private String outputFile =  "src" + fs + "test" + fs + "file2.txt";
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private PrintStream output = new PrintStream(out);


    @Test
    public void cutWithFiles() throws IOException{
        CutUtility.main(new String[]{"-w", "-o", outputFile, "2-4", inputFile});
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        String line = reader.readLine();
        assertEquals( "обрабатывает входные данные", line);
        line = reader.readLine();
        assertEquals("каждой строки выдаёт", line);
        line = reader.readLine();
        assertEquals("строки согласно занадному", line);
    }


    @Test
    public void cutWithConsole(){
        System.setOut(output);
        ByteArrayInputStream in;
        in = new ByteArrayInputStream(("тестируем ввод данных с консоли" + ls +
                "а также вывод данных на консоль"+ ls +
                "/stop").getBytes());
        System.setIn(in);
        CutUtility.main(new String[]{"-c", "2-20"});
        assertEquals("No input file name. Enter the data. Use /stop - to stop entering." + ls +
                "стируем ввод данных" + ls +
                "также вывод данных " + ls +
                "It's done" + ls, out.toString());
    }

    @Test
    public void cutRangeError1(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "100-1"});
        assertEquals("Incorrect range. End of range should be more than start of range." + ls, out.toString());
    }

    @Test
    public void cutRangeError2(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "oneTwoThree"});
        assertEquals("Incorrect range. Use int-int, int- or -int." + ls, out.toString());
    }

    @Test
    public void cutCLError(){
        System.setErr(output);
        CutUtility.main(new String[]{});
        assertEquals("Argument \"Range\" is required" + ls +
                "Command line: cut [-c|-w] [-o ofile] range [file]" + ls +
                " Range          : Output range (number-number)" + ls +
                " Input          : Input file name" + ls +
                " -c CharsIndent : Indentation in chars (default: false)" + ls +
                " -o Output      : Output file name" + ls +
                " -w WordsIndent : Indentation in words (default: false)" + ls, out.toString());
    }
}