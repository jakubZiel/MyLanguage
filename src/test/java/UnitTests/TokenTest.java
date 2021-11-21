package UnitTests;

import DataSource.DataSourceString;
import DataSource.IDataSource;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lexer.Lexer;
import static Lexer.Lexer.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.HashMap;
import Lexer.TokenType;

class TokenTest {
    public void testTokenSet(HashMap<String, TokenType> SET){
        SET.keySet().forEach(token -> {
            IDataSource ds = new DataSourceString(token);
            Lexer lx = new Lexer(ds);
            try {
                var scannedToken = lx.scanToken();
                assertEquals(scannedToken.type, SET.get(token));
                assertEquals(scannedToken.getValue(), token);
            } catch (IOException | UnexpectedCharException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testTokensKEYWORDS() {
        testTokenSet(KEYWORDS);
    }

    @Test
    public void testTokensSINGLE() {
        testTokenSet(SINGLE_SPECIAL);
    }

    @Test
    public void testTokensDOUBLE() {
        testTokenSet(DOUBLE_SPECIAL);
    }

    @Test
    public void testNumber() throws IOException, UnexpectedCharException {
        String number = "123453.34";
        IDataSource ds = new DataSourceString(number);
        Lexer lx = new Lexer(ds);

        var scannedToken = lx.scanToken();
        assertEquals(TokenType.NUMBER_T, scannedToken.type);
        assertEquals(number, scannedToken.getValue());

        String number2 = "12345221";
        IDataSource ds2 = new DataSourceString(number2);
        lx.setDataSource(ds2);
        var scannedToken2 = lx.scanToken();
        assertEquals(TokenType.NUMBER_T, scannedToken2.type);
        assertEquals(number2, scannedToken2.getValue());
    }

    @Test
    public void testWord() throws UnexpectedCharException, IOException {
        String word = "jakubzielinski";
        IDataSource ds = new DataSourceString(word);
        Lexer lx = new Lexer(ds);

        var scannedToken = lx.scanToken();
        assertEquals(TokenType.IDENTIFIER, scannedToken.type);
        assertEquals(word, scannedToken.getValue());
    }

    @Test
    public void testString() throws UnexpectedCharException, IOException {
        String string = "\"some string\"";
        String expected = "some string";
        IDataSource ds = new DataSourceString(string);
        Lexer lx = new Lexer(ds);

        var scannedToken = lx.scanToken();
        assertEquals(TokenType.STRING_T, scannedToken.type);
        assertEquals(expected, scannedToken.getValue());
    }

    @Test public void testStringError() {

        String source = "\"hello my friend";

        IDataSource ds = new DataSourceString(source);
        Lexer lx = new Lexer(ds);

        assertThrows(UnexpectedCharException.class, lx::scanToken);
    }

    @Test
    public void testComment() throws UnexpectedCharException, IOException {
        String comment = "   #  comment ";
        IDataSource ds = new DataSourceString(comment);
        Lexer lx = new Lexer(ds);

        var scannedToken = lx.scanToken();
        assertEquals(TokenType.COMMENT_T, scannedToken.type);
        assertEquals("comment", scannedToken.getValue());
    }
}