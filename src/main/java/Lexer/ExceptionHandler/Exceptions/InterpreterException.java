package Lexer.ExceptionHandler.Exceptions;

import DataSource.Position;

public class InterpreterException extends Exception {
    public final String message;
    public final Position position;

    public InterpreterException (String message, Position position) {
        this.message = message;
        this.position = position;
    }
}
