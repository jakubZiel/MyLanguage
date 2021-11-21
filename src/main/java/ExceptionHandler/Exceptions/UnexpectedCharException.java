package ExceptionHandler.Exceptions;

import DataSource.Position;

public class UnexpectedCharException extends Exception{
    public final String message;
    public final Position position;

    public UnexpectedCharException (String message, Position position) {
        this.message = message;
        this.position = position;
    }
}

