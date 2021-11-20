package IntegerationTests;

import DataSource.DataSource;
import Lexer.Lexer;
import Lexer.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LexerDataSource {

    @Test
    void scanToken() throws IOException {
        String absPath = "/media/jzielins/SD/sem6/TKOM/project/src/test/resources/test.list";
        DataSource dataSource = new DataSource(absPath);

        Lexer lexer = new Lexer(dataSource);
        Token token;
        while (!dataSource.isEOF()) {
            token = lexer.scanToken();
            System.out.println(token.type);
        }

    }
}