package DataSource;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataSource implements IDataSource {

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
            if (auxBuffer == null) {
                auxBuffer = fileReader.readLine();

                if (auxBuffer == null){
                    return NULL;
                }else if (auxBuffer.equals("")){
                    return '\n';
                }else
                    return auxBuffer.charAt(0);
            } else
                return  auxBuffer.equals("") ? '\n' : auxBuffer.charAt(0);
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

    public Position getCurrentPos(){
        return new Position(line, current);
    }
}