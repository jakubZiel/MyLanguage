package Exceptions;

import Interpreter.ExecuteVisitor;

public class InterpreterException extends LanguageException {
    public final String message;
    public ExecuteVisitor lastContext;

    public InterpreterException (String message) {
        this.message = message;
    }

    public InterpreterException(String message, ExecuteVisitor lastContext){
        this.message = message;
        this.lastContext = lastContext;
    }

    @Override
    public void printErrorMessage() {
        ExecuteVisitor current = lastContext;
        System.err.println("\n\n" + message);
        while (current != null){
            System.err.println("at " + current.getCalledFunction().getIdentifier());
            current = current.getParentContext();
        }
    }
}
