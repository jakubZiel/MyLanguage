package IntegerationTests;

import DataSource.DataSourceString;
import DataSource.IDataSource;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lex.Lexer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Lex.Token;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceStringLexer {
    @Test
    void testStringSource() throws UnexpectedCharException, IOException {

        String source =
                "int main(){" +
                "int a = 34;" +
                "while(a > 30){" +
                "a = a - 1;" +
                "} " +
                "}";

        IDataSource ds = new DataSourceString(source);

        Lexer lexer = new Lexer(ds);
        ArrayList<Token> tokens = new ArrayList<>();
        while (!ds.isEOF()) {
            tokens.add(lexer.scanToken());
        }

        List<String> lexemes = List.of(
                "int", "main", "(", ")", "{",
                "int", "a", "=", "34", ";",
                "while", "(", "a", ">", "30", ")", "{",
                "a", "=", "a", "-", "1", ";",
                "}",
                "}",
                "\0"
        );
        assertEquals(tokens.stream().map(t -> (String) t.getValue()).collect(Collectors.toList()), lexemes);
    }
}