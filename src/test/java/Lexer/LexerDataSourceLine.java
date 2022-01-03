package Lexer;

import ExceptionHandler.Exceptions.UnexpectedCharException;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Lexer.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;
import DataSource.DataSourceIO;
class LexerDataSourceLine {
    private final String root = System.getProperty("user.dir");

    List<Token> scanFile(String filePath) throws IOException, UnexpectedCharException {
        DataSourceIO dataSource = new DataSourceIO(filePath);

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

    List<Object> getLexemes(String filePath) throws IOException, UnexpectedCharException {
        return scanFile(filePath).stream().map(token -> token.getValue()).collect(Collectors.toList());
    }

    @Test
    void testTokenTypesFile1() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
                INT, IDENTIFIER, PAREN_L, INT, IDENTIFIER, PAREN_R, CURLY_L,
                IF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R, CURLY_L,
                RETURN, NUMBER_T, SEMICOLON,
                CURLY_R,
                ELSEIF, PAREN_L, IDENTIFIER, ANGLE_L, NUMBER_T, PAREN_R, RETURN, NUMBER_T, SEMICOLON,
                RETURN, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, ADD, IDENTIFIER, PAREN_L, IDENTIFIER, SUBTRACT, NUMBER_T, PAREN_R, SEMICOLON,
                CURLY_R, END_T
        );
        assertEquals(tokens, getTokenTypes(root + "/src/test/resources/test1.list"));
    }

    @Test
    void testTokenTypesFile2() throws UnexpectedCharException, IOException {
        List<TokenType> tokens = List.of(
                VOID, IDENTIFIER, PAREN_L, INT, IDENTIFIER, COMA, INT, IDENTIFIER, PAREN_R, CURLY_L,
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
    /*
    @Test
    void testLexemesFile1() throws UnexpectedCharException, IOException{
        List<String> lexemes = List.of(
                "int", "fibb", "(", "int", "n", ")", "{",
                "if", "(", "n", "<", "2.0", ")", "{",
                "return", "1.0", ";",
                "}",
                "elseif", "(", "n", "<", "3.0", ")",
                "return", "0.0", ";",
                "return", "fibb", "(", "n", "-", "1.0", ")", "+", "fibb", "(", "n", "-", "2.0", ")", ";",
                "}",
                "\0"
        );
        assertEquals(lexemes, getLexemes(root + "/src/test/resources/test1.list"));
    }

    @Test
    void testLexemesFile3() throws UnexpectedCharException, IOException{
        List<String> lexemes = List.of(
                "int", "main", "(", ")", "{",
                "int", "a", "=", "-", "1234.0", ";",
                "list", "<", "int", ">", "intList", "=", "[", "1.0", ",", "2.0", ",", "3.0", ",", "4.0", "]", ";",
                "intList", "=",  "intList", ".", "filter", "(", "ele", "->", "ele", ">", "3.0", ")", ";",
                "int", "result", "=", "intList", "[", "0.0", "]", "+", "intList", "[", "1.0", "]", ";",
                "return", "0.0", ";",
                "}",
                "\0"
        );
        assertEquals(lexemes, getLexemes(root + "/src/test/resources/test3.list"));
    }
     */
}