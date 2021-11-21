package DataSource;

import static DataSource.DataSource.NULL;

public class DataSourceString implements IDataSource {

    private final String source;
    int line = 1;
    int current;
    boolean EOF;

    public DataSourceString(String source){
        this.source = source;
    }

    @Override
    public char consume() {
        if (current > source.length() - 1) {
            EOF = true;
            return NULL;
        }
        if (source.charAt(current) == '\n')
            line++;
        return source.charAt(current++);
    }

    @Override
    public char peek() {
        if (isEOF() || current > source.length() - 1) return NULL;
        return  source.charAt(current);
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
