package Lexer;

import DataSource.DataSourceString;
import Exceptions.UnexpectedCharException;
import DataSource.IDataSource;
import static DataSource.DataSourceLine.NULL;
import static Lexer.TokenType.*;
import static java.lang.Character.*;
import java.io.IOException;
import java.util.HashMap;
import DataSource.Position;

public class Lexer {
    private IDataSource dataSource;

    public static final HashMap<String, TokenType> KEYWORDS;
    public static final HashMap<String, TokenType> SINGLE_SPECIAL;
    public static final HashMap<String, TokenType> DOUBLE_SPECIAL;

    public Lexer(IDataSource dataSource) {
        this.dataSource = dataSource;
    }
    public Token scanToken() throws RuntimeException, IOException, UnexpectedCharException {
        if (dataSource.isEOF())
            return new Token();

        consumeWhitespaces();

        char character = dataSource.peek();
        if (isDigit(character)) {
            return parseNumber();
        } else if (isLetter(character)) {
            return parseWord();
        } else if (character == '"') {
            return parseString();
        } else if (isSpecial(character)) {
            return parseSpecial();
        } else if (character == '#') {
            parseComment();
            return scanToken();
        }
        throw new UnexpectedCharException("Illegal character exception : " + character, dataSource.getCurrentPos());
    }

    private Token parseString() throws IOException {
        Position beg = dataSource.getCurrentPos();
        dataSource.consume();

        StringBuilder string = new StringBuilder();
        while (true) {
            char nextChar = dataSource.consume();
            if (nextChar == '"') {
                break;
            } else if (nextChar == '\\') {
                char escaped = dataSource.consume();
                string.append(escaped);
            } else
                string.append(nextChar);
        }
        return new Token(STRING_T, beg, string.toString());
    }

    private Token parseNumber() throws IOException, UnexpectedCharException {
        StringBuilder number = new StringBuilder();
        boolean dotFound = false;

        while (true) {
            char nextChar = dataSource.peek();

            if (isDigit(nextChar)) {
                number.append(nextChar);
                dataSource.consume();
            } else if (nextChar == '.') {
                if (dotFound)
                    throw new UnexpectedCharException("more than 1 '.' in number", dataSource.getCurrentPos());
                dotFound = true;
                number.append(nextChar);
                dataSource.consume();
                if (!isDigit(dataSource.peek()))
                    throw new UnexpectedCharException("'.' at the end of the number", dataSource.getCurrentPos());
            } else if (isLetter(nextChar)){
                throw new UnexpectedCharException(String.format("received '%c' when digit was expected", nextChar), dataSource.getCurrentPos());
            } else
                break;
        }
        Object value;
        if (dotFound){
            value = Double.parseDouble(number.toString());
        }else {
            value = Integer.parseInt(number.toString());
        }

        return new Token(NUMBER_T, dataSource.getCurrentPos(), value);
    }

    private Token parseWord() throws IOException {
        StringBuilder word = new StringBuilder();

        while (true) {
            char nextChar = dataSource.peek();
            if (isDigit(nextChar) || isLetter(nextChar)) {
                word.append(nextChar);
                dataSource.consume();
            } else
                break;
        }
        String parsedWord = word.toString();
        return new Token(KEYWORDS.getOrDefault(parsedWord, IDENTIFIER), dataSource.getCurrentPos(), parsedWord);
    }

    private Token parseSpecial() throws IOException {
        char firstChar = dataSource.consume();
        char nextChar = dataSource.peek();
        String combined = String.valueOf(firstChar) + nextChar;

        if (DOUBLE_SPECIAL.containsKey(combined)) {
            dataSource.consume();
            return new Token(DOUBLE_SPECIAL.get(combined), dataSource.getCurrentPos(), combined);
        }
        return new Token(SINGLE_SPECIAL.get(String.valueOf(firstChar)), dataSource.getCurrentPos(),  String.valueOf(firstChar));
    }

    private void parseComment() throws IOException {
        int begLine = dataSource.getLine();

        while (!dataSource.isEOF() && begLine == dataSource.getLine()){
            dataSource.consume();
        }
    }

    private boolean isSpecial(char character) {
        return SINGLE_SPECIAL.containsKey(String.valueOf(character)) || "!|&-".contains(String.valueOf(character));
    }

    private void consumeWhitespaces() throws IOException {
        while ("\n ".contains(String.valueOf(dataSource.peek()))) {
            dataSource.consume();
        }
    }

    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Lexer lexerFactory(String source){
        IDataSource dataSource = new DataSourceString(source);
        return new Lexer(dataSource);
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
        KEYWORDS.put("elseif", ELSEIF);
        KEYWORDS.put("return", RETURN);

        KEYWORDS.put("list", LIST);
        KEYWORDS.put("int", INT);
        KEYWORDS.put("void", VOID);
        KEYWORDS.put("double", DOUBLE);
        KEYWORDS.put("string", STRING_T);
        KEYWORDS.put("foreach", FOREACH);
        KEYWORDS.put("filter", FILTER);
        KEYWORDS.put("add", ADD_LIST);
        KEYWORDS.put("remove", REMOVE_LIST);

        DOUBLE_SPECIAL.put("!=", N_EQUAL);
        DOUBLE_SPECIAL.put("==", EQUAL);
        DOUBLE_SPECIAL.put(">=", GREATER_OR_EQUAL);
        DOUBLE_SPECIAL.put("<=", LESS_OR_EQUAL);
        DOUBLE_SPECIAL.put("||", OR);
        DOUBLE_SPECIAL.put("&&", AND);
        DOUBLE_SPECIAL.put("->", ARROW);
    }
}