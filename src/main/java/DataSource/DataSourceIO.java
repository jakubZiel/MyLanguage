package DataSource;
import Exceptions.UnexpectedCharException;
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
            line++;
            current = 0;
            return '\n';
        }

        if ((char) consumed == '\\') {
            char n = peek();
            if (n == 'n') {
                var nRead = source.read();
                line++;
                return '\n';
            }
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
}
