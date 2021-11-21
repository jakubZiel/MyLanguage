package DataSource;

import java.io.IOException;

public interface IDataSource {
    char consume() throws IOException;
    char peek() throws IOException;

    boolean isEOF();
    int getLine();
    int getCurrent();
    Position getCurrentPos();
}
