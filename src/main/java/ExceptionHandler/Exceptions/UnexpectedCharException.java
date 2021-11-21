package ExceptionHandler.Exceptions;

import org.javatuples.Pair;

public class UnexpectedCharException extends Exception{
    public final String message;
    public final Pair<Integer, Integer> position;

    public UnexpectedCharException (String message, Pair<Integer, Integer> position) {
        this.message = message;
        this.position = position;
    }
}

