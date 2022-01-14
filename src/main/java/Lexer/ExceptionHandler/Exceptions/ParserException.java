package Lexer.ExceptionHandler.Exceptions;

import DataSource.Position;

public class ParserException extends Exception {
    public final String message;
    public final Position position;

    public ParserException (String message, Position position) {
        this.message = message;
        this.position = position;
    }
}
