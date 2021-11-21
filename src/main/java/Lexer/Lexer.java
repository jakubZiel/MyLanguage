package Lexer;

import DataSource.DataSource;
import ExceptionHandler.Exceptions.UnexpectedCharException;

import DataSource.IDataSource;
import static DataSource.DataSource.NULL;
import static Lexer.LexerState.*;
import static Lexer.TokenType.*;
import static java.lang.Character.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private final IDataSource dataSource;
    private final ArrayList<Token> tokens;
    private static final HashMap<String, TokenType> KEYWORDS;
    private static final HashMap<String, TokenType> SINGLE_SPECIAL;
    private static final HashMap<String, TokenType> DOUBLE_SPECIAL;

    public Lexer(DataSource dataSource) {
        this.dataSource = dataSource;
        tokens = new ArrayList<>();
    }

    private LexerState getNextState() throws RuntimeException, IOException {
        if (dataSource.isEOF())
            return END;

        consumeWhitespaces();

        char character = dataSource.peek();
        if (isDigit(character)) {
            return NUMBER;
        } else if (isLetter(character)) {
            return WORD;
        } else if (character == '"') {
            return STRING;
        } else if (isSpecial(character)) {
            return SPECIAL;
        } else if (character == '#') {
            return COMMENT;
        }
        throw new IllegalArgumentException();
    }

    public Token scanToken() throws IOException, UnexpectedCharException {
        switch (getNextState()) {
            case NUMBER:
                return parseNumber();
            case STRING:
                return parseString();
            case WORD:
                return parseWord();
            case SPECIAL:
                return parseSpecial();
            case COMMENT:
                return parseComment();
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
        while (continueParsing) {
            char nextChar = dataSource.consume();

            if (nextChar == '"') {
                continueParsing = false;
            } else if (nextChar == '\\') {

                string.append('\\');
                string.append(dataSource.consume());
            } else {
                string.append(nextChar);
            }
        }
        return new Token(STRING_T, dataSource.getCurrentPos(), string.toString());
    }

    private Token parseNumber() throws IOException, UnexpectedCharException {
        boolean continueParsing = true;

        StringBuilder number = new StringBuilder();
        boolean dotNotFound = true;

        while (continueParsing) {
            char nextChar = dataSource.peek();

            if (isDigit(nextChar)) {
                number.append(nextChar);
                dataSource.consume();
            } else if (nextChar == '.' && dotNotFound && isDigit(dataSource.peek())) {
                number.append(nextChar);
                dotNotFound = false;
                dataSource.consume();
            } else if (isLetter(nextChar)){
                throw new UnexpectedCharException(String.format("received %c when digit was expected", nextChar), dataSource.getCurrentPos());
            } else
                continueParsing = false;
        }
        return new Token(NUMBER_T, dataSource.getCurrentPos(), dotNotFound ? Integer.parseInt(number.toString()) : Float.parseFloat(number.toString()));
    }

    private Token parseWord() throws IOException {
        boolean continueParsing = true;

        StringBuilder word = new StringBuilder();

        while (continueParsing) {
            char nextChar = dataSource.peek();
            if (isDigit(nextChar) || isLetter(nextChar)) {
                word.append(nextChar);
                dataSource.consume();
            } else {
                continueParsing = false;
            }
        }
        String parsedWord = word.toString();

        return new Token(KEYWORDS.getOrDefault(parsedWord, IDENTIFIER), dataSource.getCurrentPos(), parsedWord);
    }

    private Token parseSpecial() throws IOException {
        char firstChar = dataSource.consume();
        char nextChar = dataSource.peek();

        if (">|&=".contains(String.valueOf(nextChar))){
            dataSource.consume();
            String special = String.valueOf(firstChar) + nextChar;
            return new Token(DOUBLE_SPECIAL.get(special), dataSource.getCurrentPos(),  special);
        }
        return new Token(SINGLE_SPECIAL.get(String.valueOf(firstChar)), dataSource.getCurrentPos(),  String.valueOf(firstChar));
    }

    private Token parseComment() throws IOException {

        int begLine = dataSource.getLine();

        while (begLine == dataSource.getLine()){
            dataSource.consume();
        }
        return new Token(COMMENT_T, dataSource.getCurrentPos(), "comment");
    }

    private boolean isSpecial(char character) {
        return SINGLE_SPECIAL.containsKey(String.valueOf(character)) || "!|&-".contains(String.valueOf(character));
    }

    private void consumeWhitespaces() throws IOException {
        while ("\n ".contains(String.valueOf(dataSource.peek()))) {
            dataSource.consume();
        }
    }

    static {
        KEYWORDS = new HashMap<>();
        SINGLE_SPECIAL = new HashMap<>();
        DOUBLE_SPECIAL = new HashMap<>();

        SINGLE_SPECIAL.put("(", PAREN_L);
        SINGLE_SPECIAL.put(")", PAREN_R);
        SINGLE_SPECIAL.put("{", CURLY_L);
        SINGLE_SPECIAL.put("}", CURLY_R);
        SINGLE_SPECIAL.put("<", ANGLE_L);
        SINGLE_SPECIAL.put(">", ANGLE_R);
        SINGLE_SPECIAL.put("[", SQUARE_L);
        SINGLE_SPECIAL.put("]", SQUARE_R);
        SINGLE_SPECIAL.put(".", DOT);
        SINGLE_SPECIAL.put(",", COMA);
        SINGLE_SPECIAL.put(";", SEMICOLON);
        SINGLE_SPECIAL.put("=", ASSIGN);
        SINGLE_SPECIAL.put("!", NOT);
        SINGLE_SPECIAL.put(String.valueOf(NULL), END_T);

        SINGLE_SPECIAL.put("+", ADD);
        SINGLE_SPECIAL.put("-", SUBTRACT);
        SINGLE_SPECIAL.put("/", DIVIDE);
        SINGLE_SPECIAL.put("*", MULTIPLY);
        SINGLE_SPECIAL.put("%", MODULO);

        KEYWORDS.put("while", WHILE);
        KEYWORDS.put("if", IF);
        KEYWORDS.put("else", ELSE);
        KEYWORDS.put("elseif", ELIF);
        KEYWORDS.put("return", RETURN);

        KEYWORDS.put("list", LIST);
        KEYWORDS.put("int", INT);
        KEYWORDS.put("double", DOUBLE);
        KEYWORDS.put("string", STRING_T);
        KEYWORDS.put("foreach", FOREACH);
        KEYWORDS.put("filter", FILTER);
        KEYWORDS.put("add", ADD_LIST);
        KEYWORDS.put("remove", REMOVE_LIST);
        KEYWORDS.put("print", PRINT);

        DOUBLE_SPECIAL.put("!=", N_EQUAL);
        DOUBLE_SPECIAL.put("==", EQUAL);
        DOUBLE_SPECIAL.put(">=", GREATER_OR_EQUAL);
        DOUBLE_SPECIAL.put("<=", LESS_OR_EQUAL);
        DOUBLE_SPECIAL.put("||", OR);
        DOUBLE_SPECIAL.put("&&", AND);
        DOUBLE_SPECIAL.put("->", ARROW);
    }
}