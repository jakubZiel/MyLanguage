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
    private final String root = System.getProperty("user.dir");

    List<Token> scanFile(String filePath) throws IOException, UnexpectedCharException {
        DataSource dataSource = new DataSource(filePath);

        Lexer lexer = new Lexer(dataSource);
        ArrayList<Token> tokens = new ArrayList<>();
        while (!dataSource.isEOF()) {
            tokens.add(lexer.scanToken());
        }
        return tokens;
    }

    List<TokenType> getTokenTypes(String filePath) throws IOException, UnexpectedCharException {
        return scanFile(filePath).stream().map(token -> token.type).collect(Collectors.toList());
    }

    List<String> getLexemes(String filePath) throws IOException, UnexpectedCharException {
        return scanFile(filePath).stream().map(token -> (String) token.getValue()).collect(Collectors.toList());
    }

    @Test
    void testTokenTypesFile1() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
                INT, IDENTIFIER, PAREN_L, INT, IDENTIFIER, PAREN_R, CURLY_L,
                IF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R, CURLY_L,
                RETURN, NUMBER_T, SEMICOLON,
                CURLY_R,
                ELIF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R, RETURN, NUMBER_T, SEMICOLON,
                RETURN, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, ADD, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, SEMICOLON,
                CURLY_R, END_T
        );
        assertEquals(tokens, getTokenTypes(root + "/src/test/resources/test1.list"));
    }

    @Test
    void testTokenTypesFile2() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
                IDENTIFIER, IDENTIFIER, PAREN_L, INT, IDENTIFIER, COMA, INT, IDENTIFIER, PAREN_R, CURLY_L,
                PRINT, PAREN_L, STRING_T, PAREN_R, SEMICOLON,
                INT, IDENTIFIER, ASSIGN, IDENTIFIER, ADD, IDENTIFIER, SEMICOLON,
                CURLY_R, END_T
        );
        assertEquals(tokens, getTokenTypes(root + "/src/test/resources/test2.list"));
    }

    @Test
    void testTokenTypesFile3() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
                INT, IDENTIFIER, PAREN_L, PAREN_R,  CURLY_L,
                INT, IDENTIFIER, ASSIGN, SUBTRACT, NUMBER_T, SEMICOLON,
                LIST, ANGLE_L, INT, ANGLE_R, IDENTIFIER, ASSIGN, SQUARE_L, NUMBER_T, COMA, NUMBER_T, COMA, NUMBER_T, COMA, NUMBER_T, SQUARE_R, SEMICOLON,
                IDENTIFIER, ASSIGN, IDENTIFIER, DOT, FILTER, PAREN_L, IDENTIFIER, ARROW, IDENTIFIER, ANGLE_R, NUMBER_T, PAREN_R, SEMICOLON,
                INT, IDENTIFIER, ASSIGN, IDENTIFIER, SQUARE_L, NUMBER_T, SQUARE_R, ADD, IDENTIFIER, SQUARE_L, NUMBER_T, SQUARE_R, SEMICOLON,
                RETURN, NUMBER_T, SEMICOLON,
                CURLY_R, END_T
        );
        assertEquals(tokens, getTokenTypes(root + "/src/test/resources/test3.list"));
    }

    @Test
    void testLexemesFile1() throws UnexpectedCharException, IOException{
        List<String> lexemes = List.of(
                "int", "fibb", "(", "int", "n", ")", "{",
                "if", "(", "n", "<", "2", ")", "{",
                "return", "1", ";",
                "}",
                "elseif", "(", "n", "<", "3", ")",
                "return", "0", ";",
                "return", "fibb", "(", "n", "-", "1", ")", "+", "fibb", "(", "n", "-", "2", ")", ";",
                "}",
                "\0"
        );
        assertEquals(lexemes, getLexemes(root + "/src/test/resources/test1.list"));
    }

    @Test
    void testLexemesFile3() throws UnexpectedCharException, IOException{
        List<String> lexemes = List.of(
                "int", "main", "(", ")", "{",
                "int", "a", "=", "-", "1234", ";",
                "list", "<", "int", ">", "intList", "=", "[", "1", ",", "2", ",", "3", ",", "4", "]", ";",
                "intList", "=",  "intList", ".", "filter", "(", "ele", "->", "ele", ">", "3", ")", ";",
                "int", "result", "=", "intList", "[", "0", "]", "+", "intList", "[", "1", "]", ";",
                "return", "0", ";",
                "}",
                "\0"
        );
        assertEquals(lexemes, getLexemes(root + "/src/test/resources/test3.list"));
    }
}