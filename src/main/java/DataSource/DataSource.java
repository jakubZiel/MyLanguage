package DataSource;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataSource {

    public static final char NULL = '\0';
    private final BufferedReader fileReader;
    private int line;
    private boolean EOF;
    private int current;
    private String buffer;
    private String auxBuffer;

    public DataSource(String filePath) throws IOException {
        fileReader = Files.newBufferedReader(Paths.get(filePath));
        EOF = false;
    }

    public char consume() throws IOException {
        if (EOF) throw new EOFException();

        if (auxBuffer != null){
            buffer = auxBuffer;
            line++;
            current = 0;
            auxBuffer = null;
        }else if (buffer == null || current > buffer.length() - 1){
            buffer = fileReader.readLine();
            line++;
            current = 0;
        }

        if (buffer == null){
            EOF = true;
            return NULL;
        }else if (buffer.equals("")) {
            return '\n';
        }else
            return buffer.charAt(current++);
    }

    public char peek() throws IOException{
        if (EOF) return NULL;

        if (buffer == null || current > buffer.length() - 1){
            auxBuffer = fileReader.readLine();

            if (auxBuffer == null){
                return NULL;
            }else if (auxBuffer.equals("")){
                return '\n';
            }else
                return auxBuffer.charAt(0);
        }

        return  buffer.charAt(current);
    }

    public boolean isEOF() {
        return EOF;
    }

    public int getCurrent() {
        return current;
    }

    public int getLine() {
        return line;
    }

    public Pair<Integer, Integer> getCurrentPos(){
        return new Pair<>(line, current);
    }

    public static void main(String[] args) throws IOException {
        String absPath = "/media/jzielins/SD/sem6/TKOM/project/src/main/resources/test.list";
        DataSource dataSource = new DataSource(absPath);
        while (!dataSource.isEOF()){
            //System.out.printf("cur = %c peek = %c at (l, c) = (%d, %d)\n", dataSource.advance(), dataSource.peek(), dataSource.getLine(), dataSource.getCurrent() );
            System.out.printf("%c", dataSource.consume());
        }

        System.out.println("char : " + '\f');
    }
}