package ExceptionHandling.Exceptions;

import DataSource.Position;

public class ParserException extends LanguageException{
    public final String message;
    public final Position position;

    public ParserException (String message, Position position) {
        this.message = message;
        this.position = position;
    }

    @Override
    public void printErrorMessage() {
        System.err.println(message);
        System.err.println(position);
    }
}
