package DataSource;

import java.io.IOException;

public class InteractiveDataSource implements IDataSource {

    @Override
    public char consume() throws IOException {
        return 0;
    }

    @Override
    public char peek() throws IOException {
        return 0;
    }

    @Override
    public boolean isEOF() {
        return false;
    }

    @Override
    public int getLine() {
        return 0;
    }

    @Override
    public int getCurrent() {
        return 0;
    }

    @Override
    public Position getCurrentPos() {
        return null;
    }
}
