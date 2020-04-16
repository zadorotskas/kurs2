package ru.spbstu.icc;


import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CutUtilityTest {
    private String ls = System.lineSeparator();
    private Path inputFile =  Paths.get("src","test", "resources", "file1.txt");
    private Path outputFile =  Paths.get("src","test", "resources", "file2.txt");
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private PrintStream output = new PrintStream(out);


    @Test
    public void cutWithFiles() throws IOException{
        CutUtility.main(new String[]{"-w", "-o", outputFile.toString(), "2_3", inputFile.toString()});
        BufferedReader reader = new BufferedReader(new FileReader(outputFile.toString()));
        String line = reader.readLine();
        assertEquals( "обрабатывает входные", line);
        line = reader.readLine();
        assertEquals("каждой строки", line);
        line = reader.readLine();
        assertEquals("строки согласно", line);
        reader.close();
    }


    @Test
    public void cutWithConsole(){
        System.setOut(output);
        ByteArrayInputStream in;
        in = new ByteArrayInputStream(("тестируем ввод данных с консоли" + ls +
                "а также вывод данных на консоль"+ ls +
                "/stop").getBytes());
        System.setIn(in);
        CutUtility.main(new String[]{"-c", "2_20"});
        assertEquals("No input file name. Enter the data. Use /stop - to stop entering." + ls +
                "стируем ввод данных" + ls +
                "также вывод данных " + ls +
                "It's done" + ls, out.toString());
        System.setOut(System.out);
        System.setIn(System.in);
    }


    @Test
    public void cutRangeError1(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "100_1"});
        assertEquals("Incorrect range. End of range should be more than start of range." + ls, out.toString());
        System.setErr(System.err);
    }


    @Test
    public void cutRangeError2(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "oneTwoThree"});
        assertEquals("Incorrect range. Use int_int, int_ or _int." + ls, out.toString());
        System.setErr(System.err);
    }


    @Test
    public void cutRangeError3(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "one_3"});
        assertEquals("Incorrect range. Use int_int, int_ or _int." + ls, out.toString());
        System.setErr(System.err);
    }


    @Test
    public void cutRangeError4(){
        System.setErr(output);
        CutUtility.main(new String[]{"-w", "one1_ten"});
        assertEquals("Incorrect range. Use int_int, int_ or _int." + ls, out.toString());
        System.setErr(System.err);
    }


    @Test
    public void cutNoEndOfRange() throws IOException{
        CutUtility.main(new String[]{"-w", "-o", outputFile.toString(), "2_", inputFile.toString()});
        BufferedReader reader = new BufferedReader(new FileReader(outputFile.toString()));
        String line = reader.readLine();
        assertEquals( "обрабатывает входные данные", line);
        line = reader.readLine();
        assertEquals("каждой строки выдаёт занадному диапазону", line);
        line = reader.readLine();
        assertEquals("строки согласно", line);
        reader.close();
    }

    @Test
    public void cutNoStartOfRange() throws IOException{
        CutUtility.main(new String[]{"-w", "-o", outputFile.toString(), "_3", inputFile.toString()});
        BufferedReader reader = new BufferedReader(new FileReader(outputFile.toString()));
        String line = reader.readLine();
        assertEquals( "Программа построчно обрабатывает входные", line);
        line = reader.readLine();
        assertEquals("и для каждой строки", line);
        line = reader.readLine();
        assertEquals("часть этой строки согласно", line);
        reader.close();
    }
}