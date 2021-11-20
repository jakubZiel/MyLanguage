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
    private static final HashMap<String, TokenType> SPECIAL_TOKENS;

    public Lexer(DataSource dataSource){
        this.dataSource = dataSource;
        tokens = new ArrayList<>();
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
        switch (getNextState(dataSource.peek())){
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

    private Token parseString() throws IOException {
        boolean continueParsing = true;
        dataSource.consume();

        StringBuilder string = new StringBuilder();
        while (continueParsing){
            char consumedChar = dataSource.consume();

            if (consumedChar == '"') {
                continueParsing = false;
            } else if (consumedChar == '\\') {
                if (dataSource.peek() == '"'){
                    string.append(dataSource.consume());
                } else {
                    throw new IOException("character other than \" after \\");
                }
            } else {
                string.append(consumedChar);
            }
        }

        return new Token(STRING_T, dataSource.getCurrentPos() ,string.toString());
    }

    private Token parseNumber() throws IOException {

        boolean continueParsing = true;

        StringBuilder number = new StringBuilder();
        boolean dotNotFound = true;

        while (continueParsing) {
            char consumedChar = dataSource.consume();

            if (isDigit(consumedChar) ) {
                number.append(consumedChar);
            } else if (consumedChar == '.' && dotNotFound && isDigit(dataSource.peek())) {
                number.append(consumedChar);
                dotNotFound = false;
            } else if (consumedChar == ' '){
                continueParsing = false;
            } else {
                throw new IOException("wrong number formatting");
            }
        }

        return new Token(NUMBER_T, dataSource.getCurrentPos(), dotNotFound ? Integer.parseInt(number.toString()) : Float.parseFloat(number.toString()));
    }

    private Token parserWord() throws IOException {
        boolean continueParsing = true;

        StringBuilder word = new StringBuilder();

        while (continueParsing) {
            char nextChar = dataSource.peek();
            if (isDigit(nextChar) || isLetter(nextChar)){
                word.append(nextChar);
                dataSource.consume();
            } else{
                continueParsing = false;
            }
        }
        String parsedWord = word.toString();

        return new Token(SPECIAL_TOKENS.getOrDefault(parsedWord, IDENTIFIER), dataSource.getCurrentPos(), parsedWord);
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
        SPECIAL_TOKENS = new HashMap<>();

        SPECIAL_TOKENS.put("(", PAREN_L);
        SPECIAL_TOKENS.put(")", PAREN_R);
        SPECIAL_TOKENS.put("{", CURLY_L);
        SPECIAL_TOKENS.put("}", CURLY_R);
        SPECIAL_TOKENS.put("<", ANGLE_L);
        SPECIAL_TOKENS.put(">", ANGLE_R);
        SPECIAL_TOKENS.put("[", SQUARE_L);
        SPECIAL_TOKENS.put("]", SQUARE_R);
        SPECIAL_TOKENS.put("->", ARROW);
        SPECIAL_TOKENS.put(".",  DOT);
        SPECIAL_TOKENS.put(",", COMA);
        SPECIAL_TOKENS.put(";", SEMICOLON);
        SPECIAL_TOKENS.put("=", ASSIGN);
        SPECIAL_TOKENS.put(String.valueOf(NULL), EOF);

        SPECIAL_TOKENS.put("while", WHILE);
        SPECIAL_TOKENS.put("if", IF);
        SPECIAL_TOKENS.put("else", ELSE);
        SPECIAL_TOKENS.put("return", RETURN);

        SPECIAL_TOKENS.put("list", LIST);
        SPECIAL_TOKENS.put("int", INT);
        SPECIAL_TOKENS.put("double", DOUBLE);
        SPECIAL_TOKENS.put("string", STRING_T);

        SPECIAL_TOKENS.put("!=", N_EQUAL);
        SPECIAL_TOKENS.put("==", EQUAL);
        SPECIAL_TOKENS.put(">=", GREATER_OR_EQUAL);
        SPECIAL_TOKENS.put("<=", LESS_OR_EQUAL);
        SPECIAL_TOKENS.put("||", OR);
        SPECIAL_TOKENS.put("&&", AND);

        SPECIAL_TOKENS.put("+", ADD);
        SPECIAL_TOKENS.put("-", SUBTRACT);
        SPECIAL_TOKENS.put("/", DIVIDE);
        SPECIAL_TOKENS.put("*", MULTIPLY);
        SPECIAL_TOKENS.put("%", MODULO);
    }
}
