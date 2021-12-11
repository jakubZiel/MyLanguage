package DataSource;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lexer.Lexer;

import static DataSource.DataSourceLine.NULL;
import java.io.*;

public class DataSourceIO implements IDataSource{

    private final PushbackInputStream source;
    private int current;
    private int line;
    private boolean EOF;

    public DataSourceIO(String filePath) throws IOException {
        FileInputStream stream = new FileInputStream(filePath);
        source = new PushbackInputStream(stream);
    }

    @Override
    public char consume() throws IOException {
        int consumed = source.read();
        if (consumed == 255){
            EOF = true;
            return NULL;
        }

        if ((char) consumed == '\n') {
            current = 0;
            line++;
        } else
            current++;

        return (char) consumed;
    }

    @Override
    public char peek() throws IOException {
        int peeked = source.read();
        source.unread(peeked);
        if (peeked == 255)
            return NULL;
        return (char) peeked;
    }

    @Override
    public boolean isEOF() {
        return EOF;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int getCurrent() {
        return current;
    }

    @Override
    public Position getCurrentPos() {
        return new Position(line, current);
    }

    public static void main(String[] args) throws IOException, UnexpectedCharException {
        var src = new DataSourceIO("/media/jzielins/SD/sem6/TKOM/project/src/test/resources/test2.list");
        Lexer l = new Lexer(src);
        Object tok;
        while(!src.isEOF()){
             tok = l.scanToken().type;
            System.out.println(tok);
        }
    }
}
