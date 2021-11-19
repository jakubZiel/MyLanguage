package Lexer;

import DataSource.DataSource;
import static DataSource.DataSource.NULL;
import static Lexer.LexerState.*;
import static Lexer.TokenType.*;
import static java.lang.Character.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private final DataSource dataSource;
    private final ArrayList<Token> tokens;
    private static final HashMap<String, TokenType> TOKEN_TYPES;

    public Lexer(DataSource dataSource){
        this.dataSource = dataSource;
        tokens = new ArrayList<Token>();
    }

    public void scanTokens() throws IOException {
        while (dataSource.isEOF()){
            tokens.add(scanToken());
        }
        tokens.add(new Token());
    }

    private LexerState getNextState(char character) throws RuntimeException{

        if (isDigit(character)) {
            return NUMBER;
        }else if (isLetter(character)){
            return WORD;
        }else if (character ==  '"'){
            return STRING;
        }else if (isSpecial(character)){
            return SPECIAL;
        }
        throw new IllegalArgumentException();
    }


    public Token scanToken() throws IOException {
        switch (getNextState(dataSource.advance())){
            case NUMBER:
                return parseNumber();
            case STRING:
                return parseString();
            case WORD:
                return parseWord();
            case SPECIAL:
                return parseSpecial();
            case END:
                return new Token();
            default:
                throw new IOException("EOF exception");
        }
    }

    private Token parseString(){
        return new Token();
    }

    private Token parseNumber(){
        return new Token();
    }

    private Token parseSpecial(){
        return new Token();
    }

    private Token parseWord(){
        return new Token();
    }

    private boolean isSpecial(char character){
        return isDefined(character);
    }

    static {
        TOKEN_TYPES = new HashMap<String, TokenType>();

        TOKEN_TYPES.put("(", PAREN_L);
        TOKEN_TYPES.put(")", PAREN_R);
        TOKEN_TYPES.put("{", CURLY_L);
        TOKEN_TYPES.put("}", CURLY_R);
        TOKEN_TYPES.put("<", ANGLE_L);
        TOKEN_TYPES.put(">", ANGLE_R);
        TOKEN_TYPES.put("[", SQUARE_L);
        TOKEN_TYPES.put("]", SQUARE_R);
        TOKEN_TYPES.put("->", ARROW);
        TOKEN_TYPES.put(".",  DOT);
        TOKEN_TYPES.put(",", COMA);
        TOKEN_TYPES.put(";", SEMICOLON);
        TOKEN_TYPES.put("=", ASSIGN);
        TOKEN_TYPES.put(String.valueOf(NULL), EOF);

        TOKEN_TYPES.put("while", WHILE);
        TOKEN_TYPES.put("if", IF);
        TOKEN_TYPES.put("else", ELSE);
        TOKEN_TYPES.put("return", RETURN);

        TOKEN_TYPES.put("list", LIST);
        TOKEN_TYPES.put("int", INT);
        TOKEN_TYPES.put("double", DOUBLE);
        TOKEN_TYPES.put("string", STRING_T);

        TOKEN_TYPES.put("!=", N_EQUAL);
        TOKEN_TYPES.put("==", EQUAL);
        TOKEN_TYPES.put(">=", GREATER_OR_EQUAL);
        TOKEN_TYPES.put("<=", LESS_OR_EQUAL);
        TOKEN_TYPES.put("||", OR);
        TOKEN_TYPES.put("&&", AND);

        TOKEN_TYPES.put("+", ADD);
        TOKEN_TYPES.put("-", SUBTRACT);
        TOKEN_TYPES.put("/", DIVIDE);
        TOKEN_TYPES.put("*", MULTIPLY);
        TOKEN_TYPES.put("%", MODULO);
    }
}
