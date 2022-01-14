package ExceptionHandling.Exceptions;

import DataSource.Position;

public class UnexpectedCharException extends LanguageException{
    public final String message;
    public final Position position;

    public UnexpectedCharException (String message, Position position) {
        this.message = message;
        this.position = position;
    }


    @Override
    public void printErrorMessage() {
        System.err.println(message);
        System.err.println(position);
    }
}

