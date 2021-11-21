package IntegerationTests;

import DataSource.DataSource;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lexer.Lexer;
import Lexer.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Lexer.TokenType;

import static Lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;

class LexerDataSource {

    String root = System.getProperty("user.dir");

    List<TokenType> scanFile(String filePath) throws IOException, UnexpectedCharException {
        DataSource dataSource = new DataSource(filePath);

        Lexer lexer = new Lexer(dataSource);
        ArrayList<Token> tokens = new ArrayList<>();
        while (!dataSource.isEOF()) {
            tokens.add(lexer.scanToken());
        }
        return tokens.stream().map(token -> token.type).collect(Collectors.toList());
    }

    @Test
    void testScanFile1() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
            INT, IDENTIFIER, PAREN_L, INT, IDENTIFIER, PAREN_R, CURLY_L,
            IF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R,
            RETURN, NUMBER_T, SEMICOLON,
            ELIF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R, RETURN, NUMBER_T, SEMICOLON,
            RETURN, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, ADD, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, SEMICOLON,
            CURLY_R, END_T
        );
        assertEquals(tokens, scanFile(root + "/src/test/resources/test1.list"));

    }

    @Test
    void testScanFile2() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
            IDENTIFIER, IDENTIFIER, PAREN_L, INT, IDENTIFIER, COMA, INT, IDENTIFIER, PAREN_R, CURLY_L,
            COMMENT_T,
            INT, IDENTIFIER, ASSIGN, IDENTIFIER, ADD, IDENTIFIER, SEMICOLON,
            CURLY_R, END_T
        );
        assertEquals(tokens, scanFile(root + "/src/test/resources/test2.list"));
    }

    @Test
    void testScanFile3() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
            INT, IDENTIFIER, PAREN_L, PAREN_R,  CURLY_L,
            INT, IDENTIFIER, ASSIGN, SUBTRACT, NUMBER_T, SEMICOLON,
            LIST, ANGLE_L, INT, ANGLE_R, IDENTIFIER, ASSIGN, SQUARE_L, NUMBER_T, COMA, NUMBER_T, COMA, NUMBER_T, COMA, NUMBER_T, SQUARE_R, SEMICOLON,
            IDENTIFIER, ASSIGN, IDENTIFIER, DOT, FILTER, PAREN_L, IDENTIFIER, ARROW, IDENTIFIER, ANGLE_R, NUMBER_T, PAREN_R, SEMICOLON,
            INT, IDENTIFIER, ASSIGN, IDENTIFIER, SQUARE_L, NUMBER_T, SQUARE_R, ADD, IDENTIFIER, SQUARE_L, NUMBER_T, SQUARE_R, SEMICOLON,
            RETURN, NUMBER_T, SEMICOLON,
            CURLY_R, END_T
        );
        assertEquals(tokens, scanFile(root + "/src/test/resources/test3.list"));
    }
}